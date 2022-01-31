package com.omisoft.myapplication.mvvm.utils.test

import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

class RxObservables {
    companion object {
        fun getFlowable(): Flowable<Int> {
            return Flowable.create({ emitter ->
                repeat(10_000_000) { count ->
                    emitter.onNext(count)
                }
            }, BackpressureStrategy.LATEST)
        }

        fun getCompletable(): Completable {
            return Completable.create { completable ->
                try {
//               TODO: Do some work
                    Handler(Looper.myLooper()!!).postDelayed({
                        completable.onComplete()
                    }, 4000)
                } catch (error: Exception) {
                    completable.onError(error)
                }
            }
        }

        fun getMaybe(): Maybe<String> {
            return Maybe.create { maybe ->
                try {
//               TODO: Do some work
                    Handler(Looper.myLooper()!!).postDelayed({
                        maybe.onComplete()
                        maybe.onSuccess("Success")
                    }, 4000)
                } catch (error: Exception) {
                    maybe.onError(error)
                }
            }
        }
    }
}