package co.nickp.monde.arch

data class First<out Model, Event>(
    val model: Model,
    val effects: List<Effect<Event>>
) {
  constructor(model: Model) : this(model, emptyList())
  constructor(model: Model, event: Event) : this(model, listOf(Effect.of(event)))
  constructor(model: Model, effect: Effect<Event>) : this(model, listOf(effect))
  constructor(model: Model, vararg effects: Effect<Event>) : this(model, effects.asList())
}
