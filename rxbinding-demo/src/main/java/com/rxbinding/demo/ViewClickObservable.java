package com.rxbinding.demo;

import android.view.View;

import io.reactivex.Observer;

import static com.rxbinding.demo.Preconditions.checkMainThread;

public class ViewClickObservable extends RxBindingObservable<Object> {
    private View view;

    public ViewClickObservable(View view) {
        this.view = view;
    }
    public  void setView(View view){
        this.view=view;
    }


    @Override
    protected void addListener(final Observer observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(view, observer);
        observer.onSubscribe(listener);
        view.setOnClickListener(listener);
    }

    static final class Listener extends io.reactivex.android.MainThreadDisposable implements View.OnClickListener {
        private final View view;
        private final Observer<? super Object> observer;

        Listener(View view, Observer<? super Object> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onClick(View v) {
            if (!isDisposed()) {
                observer.onNext(Notification.INSTANCE);
            }
        }

        @Override
        protected void onDispose() {
//            view.setOnClickListener(null);
        }
    }
}
