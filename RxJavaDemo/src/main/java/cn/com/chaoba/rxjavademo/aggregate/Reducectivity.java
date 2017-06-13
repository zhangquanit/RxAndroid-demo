package cn.com.chaoba.rxjavademo.aggregate;

import android.os.Bundle;

import java.util.ArrayList;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;

public class Reducectivity extends BaseActivity {
    ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 10; i++) {
            list.add(2);
        }
        mLButton.setText("reduce");
        mLButton.setOnClickListener(e -> reduceObserver().subscribe(i -> log("reduce:" + i)));
        mRButton.setText("collect");
        mRButton.setOnClickListener(e -> collectObserver().subscribe(i -> log("collect:" + i)));
    }

    /**
     * reduce(x,y)
     * x是上一次reduce返回的结果，y是本次循环的数据
     */
    private Observable<Integer> reduceObserver() {
        return Observable.from(list).reduce(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer x, Integer y) {
                System.out.println(x+" "+y);
                return x*y;
            }
        });
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 2 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 4 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 8 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 16 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 32 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 64 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 128 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 256 2
//        06-05 13:52:47.322 22488-22488/com.rxandroid.demo I/System.out: 512 2
    }

    /**
     * collect(Func0,Action2)
     * Func2: 用于创建数据集合，用于收集源Observable发射的数据
     * Action2：将源Observable发射的数据添加到集合中
     */
    private Observable<ArrayList<Integer>> collectObserver() {
        return Observable.from(list).collect(new Func0<ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> call() {
                System.out.println("call 创建数据集合");
                return new ArrayList<Integer>();
            }
        }, new Action2<ArrayList<Integer>, Integer>() {
            @Override
            public void call(ArrayList<Integer> integers, Integer i) {
                System.out.println(i+" "+integers);
                integers.add(i);
            }
        });
    }

}

