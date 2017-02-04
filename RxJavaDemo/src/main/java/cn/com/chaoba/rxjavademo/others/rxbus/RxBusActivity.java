package cn.com.chaoba.rxjavademo.others.rxbus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.com.chaoba.rxjavademo.BaseActivity;
import cn.com.chaoba.rxjavademo.others.rxbus.event.UserEvent;
import rx.functions.Action1;

/**
 * @author 张全
 */

public class RxBusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("registEvent");
        mRButton.setText("ChildActivity");
    }

    @Override
    protected void onLeftClick(View v) {
        registEvent();
    }

    @Override
    protected void onRightClick(View v) {
       startActivity(new Intent(this,ChildActivity.class));
    }

    private void registEvent() {
        RxBus.getInstance().addSubscribe(this, UserEvent.class, new Action1<UserEvent>() {
            @Override
            public void call(UserEvent userEvent) {
                log(userEvent);
                Toast.makeText(getApplicationContext(), userEvent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
    }

}
