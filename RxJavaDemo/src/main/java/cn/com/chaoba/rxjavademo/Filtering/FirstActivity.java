package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.BlockingObservable;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("First");
        mLButton.setOnClickListener(e -> firstObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                log("First:" + i);
            }
        }));
        mRButton.setText(" Blocking");
        mRButton.setOnClickListener(e -> {
            //在返回满足条件的数据前 将处于阻塞状态
            Integer first = filterObserver().first(new Func1<Integer, Boolean>() {
                @Override
                public Boolean call(Integer i) {
                    return i > 1;
                }
            });
            log("blocking:" + first);
        });
    }

    private Observable<Integer> firstObserver() {
        return Observable.just(0, 1, 2, 3, 4, 5).first(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer>1;
            }
        });
    }

    private BlockingObservable<Integer> filterObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!subscriber.isUnsubscribed()) {
                        log("onNext:" + i);
                        subscriber.onNext(i);
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        }).toBlocking();
    }
}
