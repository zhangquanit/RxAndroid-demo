package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;
import android.view.View;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

public class TimeIntervalTimeStampActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("timeInterval");
        mRButton.setText("timeStamp");
    }

    @Override
    protected void onLeftClick(View v) {
        super.onLeftClick(v);
        timeIntervalObserver().subscribe(new Action1<TimeInterval<Integer>>() {
            @Override
            public void call(TimeInterval<Integer> i) {
                log("timeInterval:" + i.getValue() + "-" + i.getIntervalInMilliseconds());
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        super.onRightClick(v);
        timeStampObserver().subscribe(new Action1<Timestamped<Integer>>() {
            @Override
            public void call(Timestamped<Integer> i) {
                log("timeStamp:" + i.getValue() + "-" + i.getTimestampMillis());
            }
        });
    }

    /**
     * timeInterval
     * TimeInterval操作符拦截原始Observable发射的数据项，替换为两个连续发射物之间流逝的时间长度。
     * 也就是说这个使用这个操作符后发射的不再是原始数据，而是原始数据发射的时间间隔。
     * 新的Observable的第一个发射物表示的是在观察者订阅原始Observable到原始Observable发射它的第一项数据之间流逝的时间长度。
     * 不存在与原始Observable发射最后一项数据和发射onCompleted通知之间时长对应的发射物。timeInterval默认在immediate调度器上执行，你可以通过传参数修改。
     *
     * @Override public void onNext(T args) {
     * long nowTimestamp = scheduler.now();
     * subscriber.onNext(new TimeInterval<T>(nowTimestamp - lastTimestamp, args));
     * lastTimestamp = nowTimestamp;
     * }
     */
    private Observable<TimeInterval<Integer>> timeIntervalObserver() {
        return createObserver().timeInterval();
//        timeInterval:0-1010
//        timeInterval:1-1002
//        timeInterval:2-1000
//        timeInterval:3-1001

    }

    /**
     * timestamp
     * 将源Observable发射的数据包装成timestamp，发送给订阅者。Timestamped(T，timestampMillis)
     *
     @Override public void onNext(T t) {
     o.onNext(new Timestamped<T>(scheduler.now(), t));
     }
      * @return
     */
    private Observable<Timestamped<Integer>> timeStampObserver() {
        return createObserver().timestamp();
    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i <= 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread());
    }
}
