package cn.com.chaoba.rxjavademo.creatingobserver;

import android.os.Bundle;
import android.view.View;

import java.util.Random;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class CreateAndRangeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("Create");
        mRButton.setText("Range");
    }

    @Override
    protected void onLeftClick(View v) {
        createObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("onComplete!");
            }

            @Override
            public void onError(Throwable e) {
                log("onError:" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                log("onNext:" + integer);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        rangeObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                log(i);
            }
        });
    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 0; i < 5; i++) {
                        int temp = new Random().nextInt(10);
                        if (temp > 8) {
                            //if value>8, we make an error
                            subscriber.onError(new Throwable("value >8"));//手动调用onError
                            break;
                        } else {
                            subscriber.onNext(temp);
                        }
                        // on error,complete the job
                        if (i == 4) {
                            subscriber.onCompleted();
                        }
                    }
                }
            }
        });
    }
    /**
     * range(n,m):从n开始，输入m个值
     */
    private Observable<Integer> rangeObserver() {
        return Observable.range(10, 5); //10、11、12、13、14
    }


}
