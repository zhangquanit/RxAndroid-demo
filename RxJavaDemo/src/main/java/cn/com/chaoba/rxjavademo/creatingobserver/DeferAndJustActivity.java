package cn.com.chaoba.rxjavademo.creatingobserver;

import android.os.Bundle;
import android.view.View;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

public class DeferAndJustActivity extends BaseActivity {
    Observable<Long> deferObservable;
    Observable<Long> justObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deferObservable = DeferObserver();
        justObservable = JustObserver();
        mLButton.setText("Defer");
        mRButton.setText("Just");
    }

    @Override
    protected void onLeftClick(View v) {
        //每次调用subscibe都会创建一个新的Observable对象
        deferObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long time) {
                log("defer:" + time);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        justObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long time) {
                log("defer:" + time);
            }
        });
    }


    /**
     * Defer操作符只有当有Subscriber来订阅的时候才会创建一个新的Observable对象,也就是说每次订阅都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的
     */
    private Observable<Long> DeferObserver() {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(System.currentTimeMillis());
            }
        });
    }

    /**
     * Just操作符将某个对象转化为Observable对象，并且将其发射出去，可以是一个数字、一个字符串、数组、Iterate对象等，是一种非常快捷的创建Observable对象的方法
     */
    private Observable<Long> JustObserver() {
        return Observable.just(System.currentTimeMillis());
    }

    private void test() {
        //对于可变参数，just内部会将参数封装成数组，然后调用from(T[])，因此等价于from，会依次发射1，2，3
        Observable.just(1, 2, 3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
        //依次发射1，2，3
        Observable.from(new Integer[]{1, 2, 3})
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });

    }

    private void test2() {
        //将数组整个发射出去
        Observable.just(new Integer[]{1, 2, 3})
                .subscribe(new Action1<Integer[]>() {
                    @Override
                    public void call(Integer[] integers) {
                        System.out.println(integers);
                    }
                });
        //依次发射数组中的1，2，3
        Observable.from(new Integer[]{1, 2, 3})
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }


}
