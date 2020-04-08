package co.nickp.monde.arch.rx2

import co.nickp.monde.arch.Effect
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

fun <T> Completable.toEffect(): Effect<T> = Effect {
  subscribe(object : CompletableObserver {
    override fun onComplete() = it.onComplete()
    override fun onSubscribe(d: Disposable) = it.onStart(AutoCloseable(d::dispose))
    override fun onError(e: Throwable) = it.onError(Error(e))
  })
}

fun <T> Maybe<T>.toEffect(): Effect<T> = Effect {
  subscribe(object : MaybeObserver<T> {
    override fun onComplete() = it.onComplete()
    override fun onSubscribe(d: Disposable) = it.onStart(AutoCloseable(d::dispose))
    override fun onSuccess(t: T) = it.onValue(t)
    override fun onError(e: Throwable) = it.onError(Error(e))
  })
}

fun <T> Observable<T>.toEffect(): Effect<T> = Effect {
  subscribe(object : Observer<T> {
    override fun onComplete() = it.onComplete()
    override fun onSubscribe(d: Disposable) = it.onStart(AutoCloseable(d::dispose))
    override fun onNext(t: T) = it.onValue(t)
    override fun onError(e: Throwable) = it.onError(Error(e))
  })
}

fun <T> Single<T>.toEffect(): Effect<T> = Effect {
  subscribe(object : SingleObserver<T> {
    override fun onSubscribe(d: Disposable) = it.onStart(AutoCloseable(d::dispose))
    override fun onSuccess(t: T) { it.onValue(t); it.onComplete() }
    override fun onError(e: Throwable) = it.onError(Error(e))
  })
}
