package com.rxbinding.demo;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.LambdaObserver;

import static com.rxbinding.demo.Preconditions.checkMainThread;


public abstract class RxBindingObservable<T> extends Observable<T> {
        protected LambdaObserver lambdaObserver;
    private Observer<? super T> wrappedObserver;

    @Override
    public void subscribe(Observer<? super T> observer) {
        super.subscribe(observer);
        System.out.println("subscribesubscribesubscribesubscribe " + observer);
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

        System.out.println("11 subscribeActual " + observer);
        if (!checkMainThread(observer)) {
            return;
        }

//        if (null == lambdaObserver) {
//            lambdaObserver = (LambdaObserver) observer;
//            lambdaObserver.onError = new OnErrorConsumer(lambdaObserver.onError);
//            addListener(observer);
//        }
        addListener(observer);
    }


    final class WrappedObserver<T>
            implements Observer<T>, Disposable {
        private Observer<? super T> actual;
        Disposable s;

        public WrappedObserver(Observer<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Disposable s) {
            System.out.println("onSubscribe ");
            if (DisposableHelper.validate(this.s, s)) {
                this.s = s;
                actual.onSubscribe(this);
            }
        }

        @Override
        public void onNext(@NonNull T t) {
            System.out.println("onNext " + t);
            actual.onNext(t);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            System.out.println("onError ");
            actual.onError(e);
            subscribe(wrappedObserver);
        }

        @Override
        public void onComplete() {
            System.out.println("onComplete ");
            actual.onComplete();
        }

        @Override
        public void dispose() {
            System.out.println("dispose ");
//            s.dispose();
        }

        @Override
        public boolean isDisposed() {
            System.out.println("isDisposed ");
            return s.isDisposed();
        }
    }

    class OnErrorConsumer implements Consumer<Throwable> {
        private Consumer<Throwable> onError;

        public OnErrorConsumer(Consumer<Throwable> onError) {
            this.onError = onError;
        }

        @Override
        public void accept(Throwable error) throws Exception {
            System.out.println("sssss");
            try {
                onError.accept(error);
            } finally {
                subscribe(lambdaObserver);
            }
        }
    }


    protected abstract void addListener(Observer observer);

}
