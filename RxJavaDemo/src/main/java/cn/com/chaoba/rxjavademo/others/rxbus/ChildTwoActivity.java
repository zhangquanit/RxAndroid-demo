package cn.com.chaoba.rxjavademo.others.rxbus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.com.chaoba.rxjavademo.R;
import cn.com.chaoba.rxjavademo.others.rxbus.event.PushEvent;
import cn.com.chaoba.rxjavademo.others.rxbus.event.UserEvent;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author 张全
 */

public class ChildTwoActivity extends Activity {
    protected TextView mResultView;
    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mResultView = (TextView) findViewById(R.id.result);
        TAG = getLocalClassName();
        Button mLButton = (Button) findViewById(R.id.left);
        Button mRButton = (Button) findViewById(R.id.right);
        mLButton.setText("UserEvent");
        mRButton.setText("PushEvent");

        regist();

        mLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEvent userEvent = new UserEvent(1, "张三");
                RxBus.getInstance().post(userEvent);
            }
        });
        mRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserChildEvent userEvent = new UserChildEvent(1, "张三");
//                RxBus.getInstance().post(userEvent);
                PushEvent pushEvent = new PushEvent(2, "李四");
                RxBus.getInstance().post(pushEvent);
            }
        });
    }

    private void regist() {
        Subscription subscription = RxBus.getInstance().doSubscribe(UserEvent.class, new Action1<UserEvent>() {
            @Override
            public void call(UserEvent userEvent) {
                log(userEvent);
            }
        });
        RxBus.getInstance().addSubscription(this,subscription);
    }

    protected void log(Object s) {
        Log.d(TAG, String.valueOf(s));
        Observable.just(s).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                mResultView.setText(mResultView.getText() + "\n" + o);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
    }
}
