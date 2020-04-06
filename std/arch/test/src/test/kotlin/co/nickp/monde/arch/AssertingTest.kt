package co.nickp.monde.arch

import co.nickp.monde.arch.Next.Companion.next
import co.nickp.monde.arch.Next.Companion.send
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AssertingTest {
  private val update: Reducer<String, String, () -> Effect<String>> =
      { _, ev, en ->
        when (ev) {
          "event" -> send(Effect.of("sent"))
          "envent" -> send(en())
          else -> next(ev)
        }
      }
  private val env = { Effect.of("envffect") }
  private val model = "initial model"

  @Test
  fun `updates the model`() {
    Asserting.on(update, env, model) {
      send("data")
      assertThat(model).matches("data")
    }
  }

  @Test
  fun `executes effects`() {
    Asserting.on(update, env, model) {
      send("event")
      recv("sent")
    }
  }

  @Test
  fun `respects the env`() {
    Asserting.on(update, env, model) {
      send("envent")
      recv("envffect")
    }
  }

  @Test(expected = AssertionError::class)
  fun `throws if test has unhandled effects`() {
    Asserting.on(update, env, model) {
      send("event")
    }
  }

  @Test
  fun `allows running effects without handling events`() {
    Asserting.on(update, env, model) {
      run("event")
    }
  }

  @Test(expected = AssertionError::class)
  fun `throws if test was expecting events that never arrived`() {
    Asserting.on(update, env, model) {
      send("data")
      recv("whoops")
    }
  }
}
