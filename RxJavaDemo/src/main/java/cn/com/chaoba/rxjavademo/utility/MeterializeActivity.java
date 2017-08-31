package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;
import android.view.View;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Notification;
import rx.Observable;
import rx.functions.Action1;

public class MeterializeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("meterialize");
        mRButton.setText("deMeterialize");
    }

    @Override
    protected void onLeftClick(View v) {
        super.onLeftClick(v);
        meterializeObserver().subscribe(new Action1<Notification<Integer>>() {
            @Override
            public void call(Notification<Integer> notification) {
                log("meterialize:" + notification.getValue() + " type" + notification.getKind());
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        super.onRightClick(v);
        deMeterializeObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                log("deMeterialize:" + i);
            }
        });
    }

    /**
     * materialize
     * 将发射的数据转换为Notification后发送给订阅者
     *
     * @return
     */
    private Observable<Notification<Integer>> meterializeObserver() {
        return Observable.just(1, 2, 3).materialize();
    }

    /**
     * 将发射的Notification转换为原始数据
     * @return
     */
    private Observable<Integer> deMeterializeObserver() {
        return meterializeObserver().dematerialize();
    }

}
