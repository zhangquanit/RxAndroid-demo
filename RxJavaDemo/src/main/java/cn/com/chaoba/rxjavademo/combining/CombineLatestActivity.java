package cn.com.chaoba.rxjavademo.combining;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

public class CombineLatestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("combineList");
//        mLButton.setOnClickListener(e -> combineListObserver().subscribe(i -> log("combineList:" + i)));
        mRButton.setText("CombineLatest");
//        mRButton.setOnClickListener(e -> combineLatestObserver().subscribe(i -> log("CombineLatest:" + i)));
    }

    @Override
    protected void onLeftClick(View v) {
        combineListObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                System.out.println("combineList: " + i);
                log("combineList:" + i);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        combineLatestObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                System.out.println("CombineLatest: " + i);
                log("CombineLatest:" + i);
            }
        });
    }

    private Observable<Integer> createObserver(int index) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 1; i < 6; i++) {
                    try {
                        Thread.sleep(1000 * index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("index=" + index + " 休眠结束");
                    subscriber.onNext(i * index);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    /**
     * combineLatest(observable1,observable2,func2)
     * fun2会将observable1和observable2最后发射的数据进行处理后发射给订阅者
     * 注意：只有obervable1和observable2都有数据发射过时，才会进入func2，否则会等待另外一个observable发射数据
     */
    private Observable<Integer> combineLatestObserver() {
        return Observable.combineLatest(createObserver(1), createObserver(2), new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer num1, Integer num2) {
                log("left:" + num1 + " right:" + num2);
                System.out.println("left:" + num1 + " right:" + num2);
                return num1 + num2;
            }
        });
//        06-05 14:16:19.799 11185-11268/cn.com.chaoba.rxjavademo I/System.out: index=1 休眠结束
//        06-05 14:16:20.801 11185-11268/cn.com.chaoba.rxjavademo I/System.out: index=1 休眠结束
//        06-05 14:16:20.801 11185-11269/cn.com.chaoba.rxjavademo I/System.out: index=2 休眠结束
//        06-05 14:16:20.803 11185-11269/cn.com.chaoba.rxjavademo I/System.out: left:2 right:2
//        06-05 14:16:21.803 11185-11268/cn.com.chaoba.rxjavademo I/System.out: index=1 休眠结束
//        06-05 14:16:21.803 11185-11268/cn.com.chaoba.rxjavademo I/System.out: left:3 right:2
//        06-05 14:16:22.805 11185-11269/cn.com.chaoba.rxjavademo I/System.out: index=2 休眠结束
//        06-05 14:16:22.805 11185-11268/cn.com.chaoba.rxjavademo I/System.out: index=1 休眠结束
//        06-05 14:16:22.805 11185-11268/cn.com.chaoba.rxjavademo I/System.out: left:4 right:2
//        06-05 14:16:22.806 11185-11268/cn.com.chaoba.rxjavademo I/System.out: left:4 right:4
//        06-05 14:16:23.808 11185-11268/cn.com.chaoba.rxjavademo I/System.out: index=1 休眠结束
//        06-05 14:16:23.808 11185-11268/cn.com.chaoba.rxjavademo I/System.out: left:5 right:4
//        06-05 14:16:24.807 11185-11269/cn.com.chaoba.rxjavademo I/System.out: index=2 休眠结束
//        06-05 14:16:24.808 11185-11269/cn.com.chaoba.rxjavademo I/System.out: left:5 right:6
//        06-05 14:16:26.809 11185-11269/cn.com.chaoba.rxjavademo I/System.out: index=2 休眠结束
//        06-05 14:16:26.810 11185-11269/cn.com.chaoba.rxjavademo I/System.out: left:5 right:8
//        06-05 14:16:28.811 11185-11269/cn.com.chaoba.rxjavademo I/System.out: index=2 休眠结束
//        06-05 14:16:28.812 11185-11269/cn.com.chaoba.rxjavademo I/System.out: left:5 right:10
    }

    List<Observable<Integer>> list = new ArrayList<>();

    /**
     * 将list中的observable最后发射的数据通过FuncN处理后再发射出去
     * 注意：必须所有的observable都有数据发射过时，才会进入funcN
     */
    private Observable<Integer> combineListObserver() {
        for (int i = 1; i < 5; i++) {
            list.add(createObserver(i));
        }

        return Observable.combineLatest(list, new FuncN<Integer>() {
            @Override
            public Integer call(Object... args) {
                int temp = 0;
                for (Object i : args) {
                    log(i);
                    System.out.println();
                    temp += (Integer) i;
                }
                return temp;
            }
        });
//        return Observable.combineLatest(list, args -> {
//            int temp = 0;
//            for (Object i : args) {
//                log(i);
//                temp += (Integer) i;
//            }
//            return temp;
//        });
    }


}
