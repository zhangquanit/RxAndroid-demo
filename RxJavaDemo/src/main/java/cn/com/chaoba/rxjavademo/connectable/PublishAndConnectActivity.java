package cn.com.chaoba.rxjavademo.connectable;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

public class PublishAndConnectActivity extends BaseActivity {
    Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectableObservable<Long> obs = publishObserver();

        Action1 action2 = new Action1<Long>() {

            @Override
            public void call(Long o) {
                log("action2:" + o);
            }
        };
        Action1 action1 = new Action1<Long>() {

            @Override
            public void call(Long o) {
                log("action1:" + o);
                if (o == 3) obs.subscribe(action2);
            }
        };

        obs.subscribe(action1);

        mLButton.setText("start");
        mLButton.setOnClickListener(e -> mSubscription = obs.connect()); //connect 开始发射数据
        mRButton.setText("stop");
        mRButton.setOnClickListener(e -> {
            if (mSubscription != null) {
                mSubscription.unsubscribe();
            }
        });
    }

    /**
     * publish
     * 将普通的Observable转换为可连接的Observable。
     * 可连接的Observable (connectable Observable)与普通的Observable差不多，不过它并不会在被订阅时开始发射数据，
     * 而是直到使用了connect操作符时才会开始。用这种方法，你可以在任何时候让一个Observable开始发射数据。
     *
     * @return
     */
    private ConnectableObservable<Long> publishObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.publish();
    }


}
