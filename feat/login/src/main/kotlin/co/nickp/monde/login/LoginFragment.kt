package co.nickp.monde.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import co.nickp.monde.android.findAncestor
import co.nickp.monde.arch.StoreImpl
import co.nickp.monde.login.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!! // valid from onCreateView to onDestroyView

  private val store by lazy {
    val component: LoginComponent = findAncestor()
    StoreImpl(LoginLogic.update, LoginForm(), component.loginEnv())
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentLoginBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.email.doAfterTextChanged {
      store.send(EnteredEmail(it.toString()))
      binding.login.isEnabled = !binding.email.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()
    }
    binding.password.doAfterTextChanged {
      store.send(EnteredPassword(it.toString()))
      binding.login.isEnabled = !binding.email.text.isNullOrBlank() && !binding.password.text.isNullOrEmpty()
    }
    binding.login.setOnClickListener { store.send(ClickedLogin) }
    binding.register.setOnClickListener { store.send(ClickedRegister) }
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }
}
