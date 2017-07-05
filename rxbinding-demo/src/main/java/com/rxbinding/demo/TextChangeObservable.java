package com.rxbinding.demo;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import io.reactivex.Observer;

public class TextChangeObservable extends RxBindingObservable<CharSequence> {
    private TextView mEditText;

    public TextChangeObservable(TextView editText) {
        mEditText = editText;
    }

    @Override
    protected void addListener(final Observer observer) {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                observer.onNext(s.toString());
            }
        });
    }


}
