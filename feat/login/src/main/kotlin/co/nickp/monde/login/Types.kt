package co.nickp.monde.login

data class Username(
    val username: String
) {
  companion object {
    val fake = Username("someuser")
  }
}

data class Email(
    val email: String
) {
  companion object {
    val fake = Email("example@example.org")
  }
}

data class Password(
    private val password: String
) {
  val plaintext: String = password
  override fun toString(): String = "(password redacted)"

  companion object {
    val fake = Password("P@ssword")
  }
}

