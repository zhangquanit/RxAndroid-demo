package com.rxbinding.demo;

import android.view.View;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static com.rxbinding.demo.Preconditions.checkNotNull;

public class RxView {


    public static Observable<Object> clicks(@NonNull final View view) {
        checkNotNull(view, "view == null");
        return new ViewClickObservable(view);

    }

    public static Observable<CharSequence> textChanges(@NonNull TextView editText) {
        checkNotNull(editText, "view == null");
        return new TextChangeObservable(editText);
    }

}
