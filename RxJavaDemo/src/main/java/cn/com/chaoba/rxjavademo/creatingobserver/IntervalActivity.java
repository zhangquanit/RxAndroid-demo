package cn.com.chaoba.rxjavademo.creatingobserver;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class IntervalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<Long> observable = interval();

        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                log("onCompleted" );
            }

            @Override
            public void onError(Throwable e) {
                log("onError:" + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                log("interval:" + aLong);
            }

        };
        mLButton.setText("Interval");
        mRButton.setText("UnSubsCribe");
        mLButton.setOnClickListener(e -> observable.subscribe(subscriber));
        mRButton.setOnClickListener(e -> subscriber.unsubscribe());
    }

    /**
     *  Interval所创建的Observable对象会从0开始，每隔固定的时间发射一个数字。
     *  需要注意的是这个对象是运行在computation Scheduler,所以如果需要在view中显示结果，要在主线程中订阅。
     */
    private Observable<Long> interval() {
        return Observable.interval(1,1, TimeUnit.SECONDS)  //延迟1秒开始，每隔1秒发射一个数字，数字从0开始
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //RxComputationScheduler
                        System.out.println(Thread.currentThread().getName());
                    }
                })
        //interva operates by default on the computation Scheduler,so observe on main Thread
        .observeOn(AndroidSchedulers.mainThread());
    }

}
