package com.rxbinding.demo;

import android.view.View;

import java.util.concurrent.TimeUnit;

public class ViewClickObservable extends RxViewObservable<Object> {
    private static final Object DATA = new Object();

    public ViewClickObservable(View view) {
        super(view);
    }

    @Override
    void addViewListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublisher.onNext(DATA);
            }
        });
    }

    public RxViewObservable<Object> throttleFirst(long windowDuration, TimeUnit unit) {
        mObservable = mObservable.throttleFirst(windowDuration, unit);
        return this;
    }


}
