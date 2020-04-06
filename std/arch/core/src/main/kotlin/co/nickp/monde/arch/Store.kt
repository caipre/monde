package co.nickp.monde.arch

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

interface Store<Model, Event> : AutoCloseable {
  val model: Model
  fun send(event: Event)
  fun subscribe(fn: (Model) -> Unit): AutoCloseable
}

class StoreImpl<Model, Event, Env>(
    private val update: Reducer<Model, Event, Env>,
    private var _model: Model,
    private val env: Env
) : Store<Model, Event> {
  override val model: Model
    get() = _model

  override fun send(event: Event) {
    check(!closed) { "send $event (store closed)" }
    logger.debug { "$event $model" }
    val next = update(_model, event, env)
    logger.debug { "$event $model ${next.model}" }
    next.model?.let {
      _model = it
      subscribers.values.forEach { it(_model) }
    }
    next.effects.forEach { it.exec { if (closed) drop(it) else send(it) } }
  }

  private fun drop(event: Event) {
    logger.debug { "drop $event (store closed)" }
  }

  private val subscribers = mutableMapOf<Int, (Model) -> Unit>()
  override fun subscribe(fn: (Model) -> Unit): AutoCloseable {
    subscribers[fn.hashCode()] = fn
    return AutoCloseable { subscribers.remove(fn.hashCode()) }
  }

  private var closed = false
  override fun close() {
    subscribers.clear()
    closed = true
  }
}
