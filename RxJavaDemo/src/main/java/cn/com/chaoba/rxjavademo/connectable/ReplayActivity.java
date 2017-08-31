package cn.com.chaoba.rxjavademo.connectable;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

public class ReplayActivity extends BaseActivity {
    Subscription mSubscription;
    ConnectableObservable<Long> obs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Action1 action2 = o -> log("action2:" + o);
        Action1 action1 = o -> {
            log("action1:" + o);
            if ((long) o == 5) obs.subscribe(action2);
        };


        mLButton.setText("relayCount");
        mLButton.setOnClickListener(e -> {
            obs = relayCountObserver();
            obs.subscribe(action1);
            log("relayCount");
            mSubscription = obs.connect(); // 调用Connect操作符开始发射数据
        });
        mRButton.setText("relayTime");
        mRButton.setOnClickListener(e -> {
            obs = relayTimeObserver();
            obs.subscribe(action1);
            log("relayTime");
            mSubscription = obs.connect();
        });
    }

    /**
     * ConnectableObservable和普通的Observable最大的区别就是，调用Connect操作符开始发射数据，后面的订阅者会丢失之前发射过的数据。
     * 使用Replay操作符返回的ConnectableObservable 会缓存订阅者订阅之前已经发射的数据，这样即使有订阅者在其发射数据开始之后进行订阅也能收到之前发射过的数据。Replay操作符能指定缓存的大小或者时间，这样能避免耗费太多内存。
     *
     * @return
     */
    private ConnectableObservable<Long> relayCountObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.replay(2);
    }

    private ConnectableObservable<Long> relayTimeObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.replay(3, TimeUnit.SECONDS);
    }

}
