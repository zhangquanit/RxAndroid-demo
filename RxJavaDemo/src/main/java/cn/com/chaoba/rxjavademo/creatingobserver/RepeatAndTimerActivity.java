package cn.com.chaoba.rxjavademo.creatingobserver;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class RepeatAndTimerActivity extends BaseActivity {
   Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("repeat");
        mRButton.setText("timer");
    }

    @Override
    protected void onLeftClick(View v) {
        //停止重复发射
        if(null!=subscription&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
            mLButton.setText("repeat");
            return;
        }
        mLButton.setText("点击停止");
//        repeatForver();
//        repeatCount(3);
        repeatWhen();
    }

    @Override
    protected void onRightClick(View v) {
        timerObserver().subscribe(new Action1<Long>() {
            @Override
            public void call(Long i) {
                log("timer:" + i);
            }
        });
    }


    /**
     * Repeat会将一个Observable对象重复发射，我们可以指定其发射的次数
     * repeat(): 无限重复发射
     * repeat(count): 重复发射count次
     * <p>
     * repeat 和retry的比较：
     * 当.repeat()接收到.onCompleted()事件后触发重订阅。
     * 当.retry()接收到.onError()事件后触发重订阅。
     */
    public void repeatForver() {
        //1、无限重复发射
       subscription= Observable.just(1)
                .repeat()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer i) {
                        log(i);
                    }
                });
    }

    /**
     * 重复发射指定次数
     * @param count
     */
    public void repeatCount(int count) {
        //2. 发射3次
       subscription= Observable.just(1)
                .repeat(count, Schedulers.io()) //在io线程中重复发射3次
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        String threadName = Thread.currentThread().getName();
                        log(threadName + ",onNext: " + integer);
                    }
                });
    }

    /**
     * 使用.repeatWhen() + .delay()定期轮询数据
     */
    public void repeatWhen() {
        subscription=Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("call-----111");
                subscriber.onNext(111);
                System.out.println("call-----222");
                subscriber.onNext(222);
                System.out.println("call----------------------------------------");
                subscriber.onCompleted();
            }
        })
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> completed) {
                        System.out.println("repeatWhen....completed=" + completed);
                        /**
                         至到notificationHandler发送onNext()才会重订阅到source。因为在发送onNext()之前delay了一段时间，
                         所以优雅的实现了延迟重订阅，从而避免了不间断的数据轮询。
                         */
                        return completed.delay(3, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted" );
                        log("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError" );
                        log("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext...."+integer );
                        log("onNext....i=" + integer);
                    }
                });
    }

    /**
     *  Timer会在指定时间后发射一个数字0，注意其也是运行在computation Scheduler
     */
    private Observable<Long> timerObserver() {
        //timer by default operates on the computation Scheduler
        return Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 与timer类似，也是在指定时间后发射，注意其也是运行在computation Scheduler
     */
    private Observable<Long> delayObserver() {
        //timer by default operates on the computation Scheduler
        return Observable.just(0L).delay(1,TimeUnit.SECONDS).doOnNext(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                //RxComputationScheduler-1
                System.out.println(Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

}
