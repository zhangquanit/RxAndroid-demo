package com.rxandroid.demo.rxjava;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**



 */
public class BackPressureTest {

    /**
     * Caused by: rx.exceptions.MissingBackpressureException
     * 被观察者发送事件的速度太快，而观察者处理太慢，而且没有做相应措施，所以报异常。
     */
    public static void test1() {
        //被观察者在主线程中，每1ms发送一个事件
        Observable.interval(1, TimeUnit.MILLISECONDS)
                //.subscribeOn(Schedulers.newThread())
                //将观察者的工作放在新线程环境中
                .observeOn(Schedulers.newThread())
                //观察者处理每1000ms才处理一个事件
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.w("TAG", "---->" + aLong);
                    }
                });
    }

    /**
     * 模拟背压策略
     * 注意：range操作符是支持背压的，它发送事件的速度可以被控制
     */
    public static void test2() {
        Observable observable = Observable.range(1, 5);//被观察者将产生100000个事件
        observable.observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onStart() {
                        //通知被观察者先发送一个事件
                        request(1);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted....");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError....");
                    }

                    @Override
                    public void onNext(Integer n) {
                     System.out.println("onNext....n="+n);
                        //处理完毕之后，在通知被观察者发送下一个事件
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        request(1); //通知被观察者先发送下一个事件
                    }
                });
    }
}
