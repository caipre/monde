package co.nickp.monde.login

import co.nickp.monde.arch.Asserting
import co.nickp.monde.arch.Effect
import co.nickp.monde.arch.map
import co.nickp.monde.arch.rx2.toEffect
import co.nickp.monde.conduit.adapter.toResult
import co.nickp.monde.conduit.schemas.GenericErrorModel
import co.nickp.monde.conduit.schemas.User
import co.nickp.monde.conduit.schemas.UserResponse
import co.nickp.monde.conduit.schemas.fake
import co.nickp.monde.login.LoginLogic.update
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.TimeUnit

class RegistrationTest {
  private val moshi = Moshi.Builder().build()

  @Test
  fun `registration - happy path`() {
    val scheduler = TestScheduler()
    val env = LoginEnv(
        register = {
          Single.just(Response.success(200, UserResponse.fake))
              .delay(100, TimeUnit.MILLISECONDS, scheduler)
              .toEffect()
              .map { it.toResult(moshi.adapter(GenericErrorModel::class.java)) }
              .map(::GotRegisterResponse)
        },
        navigate = { Effect.from { } }
    )

    val givenModel = RegisterForm(Username.fake, Email.fake, Password.fake)
    Asserting.on(update, env, givenModel, scheduler = scheduler) {
      run(ClickedRegister)
      assertThat(model)
          .isEqualTo(Registering(Username.fake, Email.fake, Password.fake))
      scheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
      assertThat(model)
          .isEqualTo(Authenticated(User.fake))
    }
  }

  @Test
  fun `registration - unhappy path`() {
    val scheduler = TestScheduler()
    val error = moshi.adapter(GenericErrorModel::class.java).toJson(GenericErrorModel.fake)
    val env = LoginEnv(register = {
      Single.just(Response.error<UserResponse>(
              500, ResponseBody.create(null, error)))
          .delay(100, TimeUnit.MILLISECONDS, scheduler)
          .toEffect()
          .map { it.toResult(moshi.adapter(GenericErrorModel::class.java)) }
          .map(::GotRegisterResponse)
    })

    val givenModel = RegisterForm(Username.fake, Email.fake, Password.fake)
    Asserting.on(update, env, givenModel, scheduler) {
      run(ClickedRegister)
      assertThat(model)
          .isEqualTo(Registering(Username.fake, Email.fake, Password.fake))
      scheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
      assertThat(model)
          .isEqualTo(RegisterForm(Username.fake, Email.fake, Password.fake, GenericErrorModel.fake.errors.body))
    }
  }
}
