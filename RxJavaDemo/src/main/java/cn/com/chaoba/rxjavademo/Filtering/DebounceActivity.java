package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DebounceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("throttleWithTimeout");
        mLButton.setOnClickListener(e -> throttleWithTimeoutObserver().subscribe(i -> log("throttleWithTimeout:" + i)));
        mRButton.setText("debounce");
        mRButton.setOnClickListener(e -> debounceObserver().subscribe(i -> log("debounce:" + i)));
    }

    /**
     * debounce使用函数来限流
     * deounce操作符还可以根据一个函数来进行限流。这个函数的返回值是一个临时Observable，如果源Observable在发射一个新的数据的时候，
     * 上一个数据根据函数所生成的临时Observable还没有结束，那么上一个数据就会被过滤掉。
     *
     * @return
     */
    private Observable<Integer> debounceObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .debounce(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        log(integer);
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                //生成一个Observable并使用debounce对其进行过滤，只有发射来的数据为偶数的时候才会调用onCompleted方法来表示这个临时的Observable已经终止。
                                if (integer % 2 == 0 && !subscriber.isUnsubscribed()) {
                                    log("complete:" + integer);
                                    subscriber.onCompleted();
                                }
                            }
                        });
                    }
                }).observeOn(AndroidSchedulers.mainThread());

//        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).debounce(integer -> {
//            log(integer);
//            return Observable.create(new Observable.OnSubscribe<Integer>() {
//                @Override
//                public void call(Subscriber<? super Integer> subscriber) {
//                    if (integer % 2 == 0 && !subscriber.isUnsubscribed()) {
//                        log("complete:" + integer);
//                        subscriber.onCompleted();
//                    }
//                }
//            });
//        })
//                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发射间隔小于timeout的数据会被过滤掉
     * 设定的过滤时间是200毫秒，也就说发射间隔小于200毫秒的数据会被过滤掉。
     *
     * @return
     */
    private Observable<Integer> throttleWithTimeoutObserver() {
        return createObserver().throttleWithTimeout(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        //发射间隔小于200毫秒的数据会被过滤掉，因此只会打印0，3，6，9
    }

    /**
     * debounce使用时间来进行过滤，这时它跟throttleWithTimeOut使用起来是一样
     *
     * @return
     */
    private Observable<Integer> debounceWithTimeoutObserver() {
        return createObserver().debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(i);
                    }
                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation());
    }
}