package co.nickp.monde.arch

data class Next<out Model, Event>(
    val model: Model?,
    val effects: List<Effect<Event>>
) {
  constructor(model: Model) : this(model, emptyList())

  companion object {
    fun <Model, Event> next(model: Model): Next<Model, Event> =
        Next(model, emptyList())

    fun <Model, Event> next(model: Model, event: Event): Next<Model, Event> =
        Next(model, listOf(Effect.of(event)))

    fun <Model, Event> next(model: Model, effect: Effect<Event>): Next<Model, Event> =
        Next(model, listOf(effect))

    fun <Model, Event> send(event: Event): Next<Model, Event> =
        Next(null, listOf(Effect.of(event)))

    fun <Model, Event> send(effect: Effect<Event>): Next<Model, Event> =
        Next(null, listOf(effect))

    fun <Model, Event> send(effects: List<Effect<Event>>): Next<Model, Event> =
        Next(null, effects)

    fun <Model, Event> unchanged(): Next<Model, Event> =
        Next(null, emptyList())

    fun <Model, Event, T> fire(event: T): Next<Model, Event> =
        Next(null, listOf(Effect.of(event).fire()))

    val illegal: Nothing
      get() = throw AssertionError("illegal state")
  }

  fun <T> map2(fn: (Event) -> T): Next<Model, T> = Next(model, effects.map { it.map(fn) })
}
