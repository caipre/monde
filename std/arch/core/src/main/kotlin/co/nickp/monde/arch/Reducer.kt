package co.nickp.monde.arch

import co.nickp.monde.arch.Next.Companion.send
import co.nickp.monde.arch.Next.Companion.unchanged

typealias Init<Model, Event, Env> = (Env) -> First<Model, Event>
typealias Reducer<Model, Event, Env> = (Model, Event, Env) -> Next<Model, Event>
typealias Transducer<InModel, InEvent, Model, Event, Env> = (InModel, InEvent, Env) -> Next<Model, Event>

fun <Model, Event> identity() =
    { _: Any, _: Any, _: Any -> unchanged<Model, Event>() }

fun <Model, Event, Env> concat(
    first: Reducer<Model, Event, Env>,
    second: Reducer<Model, Event, Env>,
    vararg rest: Reducer<Model, Event, Env>
): Reducer<Model, Event, Env> =
    listOf(first, second, *rest).reduce(::concat2)

private fun <Model, Event, Env> concat2(
    first: Reducer<Model, Event, Env>,
    second: Reducer<Model, Event, Env>
): Reducer<Model, Event, Env> =
    { model, event, env ->
      val fst = first(model, event, env)
      val snd = second(fst.model ?: model, event, env)
      val fs = fst.effects.plus(snd.effects)
      when {
        snd.model != null ->
          Next(snd.model, fs)
        fst.model != null ->
          Next(fst.model, fs)
        fs.isNotEmpty() ->
          send(fs)
        else -> unchanged()
      }
    }

fun <OuterModel, InnerModel, OuterEvent, InnerEvent, OuterEnv, InnerEnv> Reducer<InnerModel, InnerEvent, InnerEnv>.pullback(
    model: Prism<OuterModel, InnerModel>,
    event: Prism<OuterEvent, InnerEvent>,
    env: (OuterEnv) -> InnerEnv
): Reducer<OuterModel, OuterEvent, OuterEnv> = { mo, ev, en ->
  model.extract(mo)?.let { m ->
    event.extract(ev)?.let { e ->
      val inner = this@pullback(m, e, env(en))
      when {
        inner.model != null ->
          Next(model.embed(inner.model), inner.effects.map { it.map(event.embed) })
        inner.effects.isNotEmpty() ->
          send(inner.effects.map { it.map(event.embed) })
        else -> unchanged()
      }
    } ?: unchanged()
  } ?: unchanged()
}

fun <OuterModel, InnerModel, OuterEvent, InnerEvent, OuterEnv, InnerEnv> Reducer<OuterModel, OuterEvent, OuterEnv>.pushforward(
    model: Lens<OuterModel, InnerModel>,
    event: Prism<OuterEvent, InnerEvent>,
    env: (OuterEnv) -> InnerEnv
): Reducer<InnerModel, InnerEvent, InnerEnv> = TODO()

fun <OuterModel, InnerModel, OuterEvent, InnerEvent, OuterEnv, InnerEnv> Reducer<InnerModel, InnerEvent, InnerEnv>.pullback(
    model: Lens<OuterModel, InnerModel>,
    event: Prism<OuterEvent, InnerEvent>,
    env: (OuterEnv) -> InnerEnv
): Reducer<OuterModel, OuterEvent, OuterEnv> = { mo, ev, en ->
  event.extract(ev)?.let { e ->
    val inner = this@pullback(model.get(mo), e, env(en))
    when {
      inner.model != null ->
        Next(model.set(mo, inner.model), inner.effects.map { it.map(event.embed) })
      inner.effects.isNotEmpty() ->
        send(inner.effects.map { it.map(event.embed) })
      else -> unchanged()
    }
  } ?: unchanged()
}

inline fun <Model, Event, Env, reified EventA> Reducer<Model, Event, Env>.forward(
    type: EventA,
    crossinline fn: (Model, EventA) -> Event?
): Reducer<Model, Event, Env> =
    concat(this, { model, event, env ->
      if (event is EventA) fn(model, event)?.let(::send) ?: unchanged()
      else unchanged()
    })

inline fun <reified InModel, reified InEvent, Model, Event, Env> delegatingTo(
    crossinline delegate: Transducer<InModel, InEvent, Model, Event, Env>
): Reducer<Model, Event, Env> where InModel : Model, InEvent : Event =
    { model, event, env ->
      when (model) {
        is InModel ->
          when (event) {
            is InEvent ->
              delegate(model, event, env)
                  .map2 { it }
            else -> null
          }
        else -> null
      } ?: unchanged()
    }
