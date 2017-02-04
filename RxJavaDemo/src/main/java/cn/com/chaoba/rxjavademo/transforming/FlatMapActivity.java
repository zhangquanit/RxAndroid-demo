package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FlatMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("flatMap");
        mRButton.setText("flatMapIterable");
    }

    @Override
    protected void onLeftClick(View v) {
        flatMapObserver().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                log(s);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        flatMapIterableObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("flatMapIterable:"+integer);
            }
        });
    }

    /**
     * map()用于对数据进行直接转换
     */
    private Observable<String> mapObserver(){
       return  Observable.just(1,2,3).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                //将Integer类型数据转换为String类型数据
                return "map:" + integer;
            }
        });
    }

    /**
     * flatMap()：FlatMap通过一些中间的Observables来转换数据
     */
    private Observable<String> flatMapObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.just("flat map:" + integer);
            }
        });
    }

    /**
     * flatMapIterable():将源数据转换为一个迭代器再发射出去
     */
    private Observable<? extends Integer> flatMapIterableObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
                                     @Override
                                     public Iterable<Integer> call(Integer integer) {
                                         List<Integer> s = new ArrayList<>();
                                         for (int i = 0; i < integer; i++) {
                                             s.add(integer);
                                         }
                                         return s;
                                     }
                                 }
                );
    }


}