package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

public class SampleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("sample");
        mLButton.setOnClickListener(e -> sampleObserver().subscribe(i -> log("sample:" + i)));
        mRButton.setText("throttleFirst");
        mRButton.setOnClickListener(e -> throttleFirstObserver().subscribe(i -> log("throttleFirst:" + i)));
    }

    /**
     * Sample操作符会定时地发射源Observable最近发射的数据，其他的都会被过滤掉，等效于ThrottleLast操作符
     * @return
     */
    private Observable<Integer> sampleObserver() {
        return createObserver().sample(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * throttleFirst(long time, TimeUnit unit)
     * 每隔time时间发射这个时间段内源Observable发射的第一条数据
     * @return
     */
    private Observable<Integer> throttleFirstObserver() {
        return createObserver().throttleFirst(1000, TimeUnit.MILLISECONDS);
    }


    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
    }

}
