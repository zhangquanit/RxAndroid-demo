package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

public class DefaultIfEmptyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("empty");
        mLButton.setOnClickListener(e -> emptyObserver().subscribe(i -> log("empty:" + i)));
        mRButton.setText("notEmpty");
        mRButton.setOnClickListener(e -> notEmptyObserver().subscribe(i -> log("notEmpty:" + i)));
    }

    /**
     * defaultIfEmpty(T)
     * 如果源Observable没有发射过数据（未调用onNext），则发射T
     *
     * @return
     */
    private Observable<Integer> emptyObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                //无数据发射，未调用onNext
                subscriber.onCompleted();
            }
        }).defaultIfEmpty(10);
    }

    private Observable<Integer> notEmptyObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1); //有数据发射，则不会发射defaultIfEmpty(T)中的参数T
                subscriber.onCompleted();
            }
        }).defaultIfEmpty(10);
    }
}


