package cn.com.chaoba.rxjavademo.aggregate;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;


public class ConcatAndCountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("concat");
        mLButton.setOnClickListener(e -> concatObserver().subscribe(i -> log("concat:" + i)));
        mRButton.setText("count");
        mRButton.setOnClickListener(e -> countObserver().subscribe(i -> log("count:" + i)));
    }

    /**
     * concat(observable1,observable2,observable3....)
     * 按顺序执行obervable，并依次发射结果给订阅者
     */
    private Observable<Integer> concatObserver() {
        Observable<Integer> obser1 = Observable.just(1, 2, 3);
        Observable<Integer> obser2 = Observable.just(4, 5, 6);
        Observable<Integer> obser3 = Observable.just(7, 8, 9);
        return Observable.concat(obser1, obser2, obser3);
    }

    /**
     * count
     * 获取onNext发射次数
     */
    private Observable<Integer> countObserver() {
        return Observable.just(1, 2, 3).count();
    }
}


