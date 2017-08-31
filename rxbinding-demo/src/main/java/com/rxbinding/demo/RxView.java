package com.rxbinding.demo;

import android.view.View;
import android.widget.TextView;

import io.reactivex.annotations.NonNull;

import static com.rxbinding.demo.Preconditions.checkNotNull;

public class RxView {

    /**
     * 点击事件
     *
     * @param view
     * @return
     */
    public static ViewClickObservable clicks(@NonNull View view) {
        checkNotNull(view, "view == null");
        System.out.println("RxView改变");
        return new ViewClickObservable(view);

    }

    /**
     * 搜索框输入变化监听
     *
     * @param editText
     * @return
     */
    public static TextChangeObservable textChanges(@NonNull TextView editText) {
        checkNotNull(editText, "view == null");
        return new TextChangeObservable(editText);
    }

}
