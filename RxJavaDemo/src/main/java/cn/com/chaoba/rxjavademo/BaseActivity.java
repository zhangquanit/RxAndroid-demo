package cn.com.chaoba.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class BaseActivity extends Activity {

    protected Button mLButton, mRButton;
    protected TextView mResultView;
    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mLButton = (Button) findViewById(R.id.left);
        mRButton = (Button) findViewById(R.id.right);
        mLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftClick(v);
            }
        });
        mRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick(v);
            }
        });
        mResultView = (TextView) findViewById(R.id.result);
        TAG = getLocalClassName();
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

    protected void  onLeftClick(View v){

    }
    protected  void onRightClick(View v){

    }

}
