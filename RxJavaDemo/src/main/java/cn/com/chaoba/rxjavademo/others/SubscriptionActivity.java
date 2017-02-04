package cn.com.chaoba.rxjavademo.others;

import android.os.Bundle;
import android.view.View;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * @author 张全
 */

public class SubscriptionActivity extends BaseActivity {
    /**
     * CompositeSubscription 可以把 Subscription 收集到一起，方便 Activity 销毁时取消订阅，防止内存泄漏。
     */
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("regist");
        mRButton.setText("unregist");
    }

    private Subscription subscribe1;
    @Override
    protected void onLeftClick(View v) {
         subscribe1 = Observable.just(1).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("subscribe1:" + integer);
            }
        });
        compositeSubscription.add(subscribe1);

        Subscription subscribe2 = Observable.just("one").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log("subscribe2:" + s);
            }
        });
        compositeSubscription.add(subscribe2);

        log("compositeSubscription.isUnsubscribed()="+compositeSubscription.isUnsubscribed());
    }

    @Override
    protected void onRightClick(View v) {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        log("compositeSubscription.isUnsubscribed()="+compositeSubscription.isUnsubscribed());
        log("subscribe1.isUnsubscribed()="+subscribe1.isUnsubscribed());
    }
}
