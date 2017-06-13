package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class BufferActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("buffer");
        mRButton.setText("bufferTime");
    }

    @Override
    protected void onLeftClick(View v) {
        bufferSkipObserver().subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> i) {
                log("buffer:" + i);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        bufferTimeObserver().subscribe(new Action1<List<Long>>() {
            @Override
            public void call(List<Long> i) {
                log("bufferTime:" + i);
            }
        });
    }

    /**
     * Buffer操作符所要做的事情就是将数据按照规定的大小做一下缓存，然后将缓存的数据作为一个集合发射出去。
     */
    private Observable<List<Integer>> bufferObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).buffer(2);//收集到2个数据后就发射出去
    }

    /**
     * skip参数用来指定每次发射一个集合需要跳过几个数据.
     * 比如buffer(2,3)，就会每3个数据发射一个包含两个数据的集合
     */
    private Observable<List<Integer>> bufferSkipObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).buffer(1, 2);
    }

    /**
     * 按时间规则来缓存，3秒钟缓存发射一次
     * @return
     */
    private Observable<List<Long>> bufferTimeObserver() {
        return Observable.interval(1, TimeUnit.SECONDS).buffer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread());
    }

}
