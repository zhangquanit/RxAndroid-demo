package com.rxbinding.demo;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class TextChangeObservable extends RxViewObservable<CharSequence> {
    public TextChangeObservable(final View view) {
        super(view);
    }

    @Override
    void addViewListener(View view) {
        TextView editText = (TextView) view;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPublisher.onNext(s.toString());
            }
        });
    }

    public RxViewObservable<CharSequence> debounce(long timeout, TimeUnit unit) {
        mObservable = mObservable.debounce(timeout, unit);
        return this;
    }

}
