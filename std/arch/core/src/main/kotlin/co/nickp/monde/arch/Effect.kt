package co.nickp.monde.arch

data class Subscriber<in T>(
    val onStart: (AutoCloseable) -> Unit = { },
    val onComplete: () -> Unit = { },
    val onError: (Error) -> Unit = { },
    val onValue: (T) -> Unit
) {
  constructor(fn: (T) -> Unit) : this(onValue = fn)
}

data class Effect<out T>(
    val exec: (Subscriber<T>) -> Unit
) {
  companion object {
    fun <T> of(value: T, vararg values: T): Effect<T> = Effect {
      it.onStart(AutoCloseable { })
      (listOf(value) + values).forEach(it.onValue)
      it.onComplete()
    }

    fun <T> from(fn: ((T) -> Unit) -> Unit): Effect<T> = Effect {
      it.onStart(AutoCloseable { })
      fn(it.onValue)
      it.onComplete()
    }
  }

  fun exec(fn: (T) -> Unit) {
    this.exec.invoke(Subscriber(fn))
  }
}

fun <T, U> Effect<T>.map(fn: (T) -> U): Effect<U> =
    Effect {
      this.exec(
          Subscriber(
              it.onStart,
              it.onComplete,
              it.onError,
              { t -> it.onValue(fn(t)) }
          )
      )
    }

val Effect.Companion.nothing: Effect<Nothing>
  get() = Effect.of(null).fire()

fun <T> Effect<T>.fire(): Effect<Nothing> = this.map(::absurd)

@Suppress("UNUSED_PARAMETER")
private fun <T> absurd(ignored: T): Nothing = throw AssertionError("absurd")

