package co.nickp.monde

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.nickp.monde.arch.Effect
import co.nickp.monde.conduit.schemas.User
import co.nickp.monde.conduit.schemas.UserResponse
import co.nickp.monde.login.GotLoginResponse
import co.nickp.monde.login.GotRegisterResponse
import co.nickp.monde.login.LoginComponent
import co.nickp.monde.login.LoginEnv
import co.nickp.monde.login.LoginFragment
import com.github.michaelbull.result.Ok

class MainActivity : AppCompatActivity(), LoginComponent {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, LoginFragment())
          .commitNow()
    }
  }

  override fun loginEnv() = LoginEnv(
      login = { request -> Effect.of(GotLoginResponse(Ok(UserResponse(User(request.user.email, "token", "username", null, ""))))) },
      register = { request -> Effect.of(GotRegisterResponse(Ok(UserResponse(User(request.user.email, "token", "username", null, ""))))) },
      navigate = { user -> Effect.from { println(user) } }
  )
}
