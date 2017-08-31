package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Func1;

public class SkipUntilSkipWhileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("skipUntil");
        mLButton.setOnClickListener(e -> skipUntilObserver().subscribe(i -> log("skipUntil:" + i)));
        mRButton.setText("skipWhile");
        mRButton.setOnClickListener(e -> skipWhileObserver().subscribe(i -> log("skipWhile:" + i)));
    }

    /**
     * observable1.skipUntil(observble2)
     * 直到observable2发射数据前，observable1发射的数据都会被丢弃
     *
     * @return
     */
    private Observable<Long> skipUntilObserver() {
        //2，3，4，..............
        return Observable.interval(1, TimeUnit.SECONDS).skipUntil(Observable.timer(3, TimeUnit.SECONDS));
    }

    /**
     * observable.skipWhile(func1)
     * 满足func1条件的数据都会被丢弃
     * @return
     */
    private Observable<Long> skipWhileObserver() {
        return Observable.interval(1, TimeUnit.SECONDS).skipWhile(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return aLong < 5;  //5，6，7，8，........
            }
        });
    }
}


