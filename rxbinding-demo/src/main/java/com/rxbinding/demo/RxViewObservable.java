package com.rxbinding.demo;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.subjects.PublishSubject;

public abstract class RxViewObservable<T> implements Disposable {
    protected LambdaObserver<T> mObserver;
    protected Disposable mDisposable;
    protected Observable<T> mObservable;
    protected PublishSubject<T> mPublisher;
    private Handler mMainHandler;

    public RxViewObservable(View view) {
        addViewListener(view);
        mPublisher = PublishSubject.create();
        mObservable = mPublisher;
    }

    public RxViewObservable observeOn(Scheduler scheduler) {
        mObservable = mObservable.observeOn(scheduler);
        return this;
    }

    public RxViewObservable doOnNext(Consumer<T> consumer) {
        mObservable = mObservable.doOnNext(consumer);
        return this;
    }

    public final Disposable subscribe(Consumer<? super T> onNext) {
        return subscribe(onNext, null, null, null);
    }

    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        return subscribe(onNext, onError, null, null);
    }

    private final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                                       Action onComplete) {
        return subscribe(onNext, onError, onComplete, null);
    }

    private final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                                       Action onComplete, Consumer<? super Disposable> onSubscribe) {
        if (null == onNext) {
            onNext = Functions.emptyConsumer();
        }
        if (null == onError) {
            onError = Functions.emptyConsumer();
        }
        if (null == onComplete) {
            onComplete = Functions.EMPTY_ACTION;
        }
        if (null == onSubscribe) {
            onSubscribe = Functions.emptyConsumer();
        }
        LambdaObserver<T> ls = new LambdaObserver<T>(onNext, onError, onComplete, onSubscribe);
        return subscribe(ls);
    }

    private final Disposable subscribe(LambdaObserver<T> observer) {
        mObserver = observer;
        subscribeOnMainThread();
        return this;
    }

    private void subscribeOnMainThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            if (null == mMainHandler) {
                mMainHandler = new Handler(Looper.getMainLooper());
            }
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    doSubscribeActual();
                }
            });
        } else {
            doSubscribeActual();
        }
    }

    private void doSubscribeActual() {
        mDisposable = mObservable.subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                mObserver.onNext(t);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mObserver.onError(throwable);
                mObserver.lazySet(mDisposable); //避免打上Disposed标志，否则之后无法正常回调
                subscribeOnMainThread(); //因为有可能在子线程产生error，需要确保在主线程重订阅，
            }
        });
    }


    @Override
    public void dispose() {
        if (null != mDisposable) {
            mDisposable.dispose();
        }
    }

    @Override
    public boolean isDisposed() {
        if (null != mDisposable) {
            return mDisposable.isDisposed();
        }
        return false;
    }

    abstract void addViewListener(View view);
}
