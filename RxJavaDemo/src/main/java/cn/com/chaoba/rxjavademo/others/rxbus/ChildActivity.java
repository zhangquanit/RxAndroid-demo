package cn.com.chaoba.rxjavademo.others.rxbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.com.chaoba.rxjavademo.R;
import cn.com.chaoba.rxjavademo.others.rxbus.event.PushEvent;
import cn.com.chaoba.rxjavademo.others.rxbus.event.UserEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author 张全
 */

public class ChildActivity extends Activity {
    protected TextView mResultView;
    protected String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mResultView = (TextView) findViewById(R.id.result);
        TAG = getLocalClassName();
        Button  mLButton = (Button) findViewById(R.id.left);
        Button mRButton = (Button) findViewById(R.id.right);
        mLButton.setText("UserEvent");
        mRButton.setText("ChildTwoAct");

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
               startActivity(new Intent(ChildActivity.this,ChildTwoActivity.class));
            }
        });
    }
    private void regist(){
        RxBus.getInstance().addSubscribe(this, PushEvent.class, new Action1<PushEvent>() {
            @Override
            public void call(PushEvent pushEvent) {
                log(pushEvent);
            }
        });
        RxBus.getInstance().addSubscribe(this, UserEvent.class, new Action1<UserEvent>() {
            @Override
            public void call(UserEvent pushEvent) {
                log(pushEvent);
            }
        });
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
