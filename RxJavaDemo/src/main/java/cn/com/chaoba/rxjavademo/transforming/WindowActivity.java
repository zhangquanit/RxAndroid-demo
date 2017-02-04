package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WindowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("window");
//        mLButton.setOnClickListener(e -> windowCountObserver().subscribe(i -> {
//            log(i);
//            i.subscribe((j -> log("window:" + j)));
//        }));
        mRButton.setText("Time");
//        mRButton.setOnClickListener(e -> wondowTimeObserver().subscribe(i -> {
//            log(i);
//            i.observeOn(AndroidSchedulers.mainThread()).subscribe((j -> log("wondowTime:" + j)));
//        }));
    }

    @Override
    protected void onLeftClick(View v) {
        windowCountObserver().subscribe(new Action1<Observable<Integer>>() {
            @Override
            public void call(Observable<Integer> observable) {
                log(observable);
                observable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("window:" + integer);
                    }
                });
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        wondowTimeObserver().subscribe(new Action1<Observable<Long>>() {
            @Override
            public void call(Observable<Long> observable) {
                log(observable);
                observable.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        log("wondowTime:" + aLong);
                    }
                });
            }
        });
    }
    /**
     * Window操作符类似于我们前面讲过的buffer，不同之处在于window发射的是一些小的Observable对象，由这些小的Observable对象来发射内部包含的数据。
     * 同buffer一样，window不仅可以通过数目来分组还可以通过时间等规则来分组
     */

    /**
     * 使用window的数目规则来进行分组
     */
    private Observable<Observable<Integer>> windowCountObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).window(3);
    }

    /**
     * 使用window的时间规则来进行分组
     */
    private Observable<Observable<Long>> wondowTimeObserver() {
        //每隔3秒钟发射出一个包含2~4个数据的Observable对象
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
                .window(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }


}