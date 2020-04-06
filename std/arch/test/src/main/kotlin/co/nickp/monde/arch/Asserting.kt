package co.nickp.monde.arch

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage

class Asserting<Model, Event, Env> private constructor(
    private var _model: Model,
    private val update: Reducer<Model, Event, Env>,
    private val env: Env
) : AutoCloseable {
  val model: Model
    get() = _model

  companion object {
    fun <Model, Event, Env> on(
        update: Reducer<Model, Event, Env>,
        env: Env,
        model: Model,
        block: Asserting<Model, Event, Env>.() -> Unit
    ) = Asserting(model, update, env).use { block(it) }
  }

  private val effects: MutableList<Effect<Event>> = mutableListOf()

  fun run(
      vararg events: Event,
      until: (Model) -> Boolean = { false }
  ): Asserting<Model, Event, Env> {
    for (event in events) {
      val next = update(model, event, env)
      next.model?.let { _model = it }
      effects.addAll(next.effects)
      if (until(model)) break
      while (effects.isNotEmpty()) {
        val effect = effects.removeAt(0)
        effect.exec { run(it, until = until) }
        if (until(model)) break
      }
    }
    return this
  }

  fun send(vararg events: Event): Asserting<Model, Event, Env> {
    for (event in events) {
      val next = update(model, event, env)
      next.model?.let { _model = it }
      effects.addAll(next.effects)
    }
    return this
  }

  fun recv(vararg events: Event): Asserting<Model, Event, Env> {
    val events = events.toMutableList()
    while (events.isNotEmpty()) {
      assertThat(effects).isNotEmpty()
      val effect = effects.removeAt(0)
      val expect = events.removeAt(0)
      effect.exec {
        assertThat(it).isEqualTo(expect)
        send(it)
      }
    }
    return this
  }

  override fun close() {
    assertWithMessage(
        """
        There were ${effects.size} outstanding effect(s) when the test completed:
        
            ${effects.joinToString(separator = "\n  ")}
          
        Effects that lead to events must be handled by calling `run` or `recv` for the
        event. For example, if a `ClickedSubmit` event leads to a `GotResponse` event,
        the following code will produce an error because of the unhandled event:
        
            send(ClickedSubmit) // Error: GotResponse must be handled
            assertThat(model)
                .isEqualTo(expected)

        To run effects without handling them, use `run`:
        
            run(ClickedSubmit)  // Ok: Explicitly run until no effects remain
            assertThat(model)
                .isEqualTo(expected)
        
        To explicitly declare the expected event(s), use `recv`:
        
            send(ClickedSubmit)
            recv(GotResponse)   // Ok: Explicitly declare that GotResponse is expected
            assertThat(model)
                .isEqualTo(expected)
        
        Refer to the documentation for `run` and `recv` for more details.
        """.trimIndent()
    ).that(effects).isEmpty()
  }
}
