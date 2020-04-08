package co.nickp.monde.login

import co.nickp.monde.arch.Effect
import co.nickp.monde.arch.Next.Companion.next
import co.nickp.monde.arch.Next.Companion.pass
import co.nickp.monde.arch.Reducer
import co.nickp.monde.arch.Transducer
import co.nickp.monde.arch.concat
import co.nickp.monde.arch.delegatingTo
import co.nickp.monde.arch.nothing
import co.nickp.monde.conduit.schemas.GenericErrorModel
import co.nickp.monde.conduit.schemas.LoginUser
import co.nickp.monde.conduit.schemas.LoginUserRequest
import co.nickp.monde.conduit.schemas.NewUser
import co.nickp.monde.conduit.schemas.NewUserRequest
import co.nickp.monde.conduit.schemas.User
import co.nickp.monde.conduit.schemas.UserResponse
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

// model
sealed class LoginModel
data class LoginForm(
    val email: Email? = null,
    val password: Password? = null,
    val errors: List<String>? = null
) : LoginModel()

data class Authenticating(
    val email: Email,
    val password: Password
) : LoginModel()

data class RegisterForm(
    val username: Username? = null,
    val email: Email? = null,
    val password: Password? = null,
    val errors: List<String>? = null
) : LoginModel()

data class Registering(
    val username: Username,
    val email: Email,
    val password: Password
) : LoginModel()

data class Authenticated(
    val user: User
) : LoginModel()

// event
sealed class LoginEvent
object ClickedLogin : LoginEvent()
object ClickedRegister : LoginEvent()
data class EnteredUsername(val username: String) : LoginEvent()
data class EnteredEmail(val email: String) : LoginEvent()
data class EnteredPassword(val password: String) : LoginEvent()
data class GotLoginResponse(val result: Result<UserResponse, GenericErrorModel>) : LoginEvent()
data class GotRegisterResponse(val result: Result<UserResponse, GenericErrorModel>) : LoginEvent()

// env
interface LoginComponent {
  fun loginEnv(): LoginEnv
}

data class LoginEnv(
    val login: (LoginUserRequest) -> Effect<GotLoginResponse> = { Effect.nothing },
    val register: (NewUserRequest) -> Effect<GotRegisterResponse> = { Effect.nothing },
    val navigate: (User) -> Effect<Nothing> = { Effect.nothing }
)

// logic
object LoginLogic {
  val update: Reducer<LoginModel, LoginEvent, LoginEnv>
    get() = concat(
        delegatingTo(login),
        delegatingTo(authenticating),
        delegatingTo(register),
        delegatingTo(registering),
        delegatingTo(authenticated)
    )

  private val login: Transducer<LoginForm, LoginEvent, LoginModel, LoginEvent, LoginEnv> =
      { model, event, env ->
        when (event) {
          is EnteredEmail ->
            next(model.copy(email = Email(event.email)))

          is EnteredPassword ->
            next(model.copy(password = Password(event.password)))

          ClickedLogin -> {
            check(model.email != null && model.password != null)
            next(
                Authenticating(model.email, model.password),
                env.login(LoginUserRequest(LoginUser(model.email.email, model.password.plaintext)))
            )
          }

          ClickedRegister ->
            next(RegisterForm(email = model.email, password = model.password))

          else -> pass()
        }
      }

  private val authenticating: Transducer<Authenticating, LoginEvent, LoginModel, LoginEvent, LoginEnv> =
      { model, event, env ->
        when (event) {
          is GotLoginResponse -> {
            when (event.result) {
              is Ok ->
                next<LoginModel, LoginEvent>(Authenticated(event.result.value.user), env.navigate(event.result.value.user))
              is Err ->
                next(LoginForm(model.email, model.password, event.result.error.errors.body))
            }
          }

          else -> pass()
        }
      }

  private val register: Transducer<RegisterForm, LoginEvent, LoginModel, LoginEvent, LoginEnv> =
      { model, event, env ->
        when (event) {
          is EnteredUsername ->
            next(model.copy(username = Username(event.username)))

          is EnteredEmail ->
            next(model.copy(email = Email(event.email)))

          is EnteredPassword ->
            next(model.copy(password = Password(event.password)))

          ClickedLogin ->
            next(LoginForm(model.email, model.password))

          ClickedRegister -> {
            check(model.username != null && model.email != null && model.password != null)
            next(
                Registering(model.username, model.email, model.password),
                env.register(NewUserRequest(NewUser(model.username.username, model.email.email, model.password.plaintext)))
            )
          }

          else -> pass()
        }
      }

  private val registering: Transducer<Registering, LoginEvent, LoginModel, LoginEvent, LoginEnv> =
      { model, event, env ->
        when (event) {
          is GotRegisterResponse ->
            when (event.result) {
              is Ok ->
                next<LoginModel, LoginEvent>(Authenticated(event.result.value.user), env.navigate(event.result.value.user))
              is Err ->
                next(RegisterForm(model.username, model.email, model.password, event.result.error.errors.body))
            }

          else -> pass()
        }
      }

  private val authenticated: Transducer<Authenticated, LoginEvent, LoginModel, LoginEvent, LoginEnv> =
      { _, _, _ -> pass() }
}
