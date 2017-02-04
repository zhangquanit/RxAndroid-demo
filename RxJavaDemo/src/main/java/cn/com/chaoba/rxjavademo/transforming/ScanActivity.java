package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;

public class ScanActivity extends BaseActivity {
    ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 10; i++) {
            list.add(2);
        }
        mLButton.setText("scan");
    }

    @Override
    protected void onLeftClick(View v) {
        scanObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("scan:" +integer);//输出了2的n次方
            }
        });
    }

    /**
     * Scan操作符对一个序列的数据应用一个函数，并将这个函数的结果发射出去作为下个数据应用这个函数时候的第一个参数使用，有点类似于递归操作
     */
    private Observable<Integer> scanObserver() {
        return Observable.from(list).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                log(integer+"*"+integer2);
                log("----------------------------");
                return integer*integer2;
            }
        }).observeOn(AndroidSchedulers.mainThread());
//        return Observable.from(list).scan((x, y) -> x * y).observeOn(AndroidSchedulers.mainThread());
    }

}

