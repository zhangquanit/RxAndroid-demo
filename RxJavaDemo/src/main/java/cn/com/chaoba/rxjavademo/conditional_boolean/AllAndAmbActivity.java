package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Func1;

public class AllAndAmbActivity extends BaseActivity {
    boolean tag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("all");
        mLButton.setOnClickListener(e -> allObserver().subscribe(i -> log("all:" + i)));
        mRButton.setText("amb");
        mRButton.setOnClickListener(e -> ambObserver().subscribe(i -> log("amb:" + i)));
    }

    /**
     * 判断发射的所有数据是否满足条件，都满足条件，订阅者会收到true，如果不满足条件，订阅者会收到false
     *
     * @return
     */
    private Observable<Boolean> allObserver() {
        Observable<Integer> just;
        if (tag) {
            just = Observable.just(1, 2, 3, 4, 5);
        } else {
            just = Observable.just(1, 2, 3, 4, 5, 6);
        }
        tag = true;
        return just.all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 6;
            }
        });
    }

    /**
     * amb(observable1,observable2,observable3)
     * 订阅者只会接收到第一个发射数据的observable发射的数据
     * @return
     */
    private Observable<Integer> ambObserver() {
        Observable<Integer> delay3 = Observable.just(1, 2, 3).delay(3000, TimeUnit.MILLISECONDS);
        Observable<Integer> delay2 = Observable.just(4, 5, 6).delay(2000, TimeUnit.MILLISECONDS);
        Observable<Integer> delay1 = Observable.just(7, 8, 9).delay(1000, TimeUnit.MILLISECONDS); //最先发射数据，则只会接收到observable3发射的数据
        return Observable.amb(delay1, delay2, delay3);
    }
}


