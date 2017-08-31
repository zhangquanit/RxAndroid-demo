package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Func1;

public class TakeUntilTakeWhileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("takeUntil");
        mLButton.setOnClickListener(e -> takeUntilObserver().subscribe(i -> log("takeUntil:" + i)));
        mRButton.setText("takeWhile");
        mRButton.setOnClickListener(e -> takeWhileObserver().subscribe(i -> log("takeWhile:" + i)));
    }

    /**
     * observable1.takUtil(observable2)
     * 当observable2发射数据时，observable1停止发射，与skipUtil(observable2)相反
     *
     * @return
     */
    private Observable<Long> takeUntilObserver() {
        //observable2延迟3秒发射，因此observable1只能发射0，1
        return Observable.interval(1, TimeUnit.SECONDS).takeUntil(Observable.timer(3, TimeUnit.SECONDS));
    }

    /**
     * observable.takUtil(condition)
     * observable只发射满足condition条件的数据，与skipWhile(condition)相反
     * @return
     */
    private Observable<Long> takeWhileObserver() {
        //observable只发射满足条件的数据
        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return aLong < 5;
            }
        });
    }
}


