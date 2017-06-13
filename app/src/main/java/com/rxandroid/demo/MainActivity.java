package com.rxandroid.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.rxandroid.demo.rxjava.BehaviorSubjectTest;
import com.rxandroid.demo.rxjava.OperaterTest;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
  Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.button_run_scheduler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

    }

    BehaviorSubjectTest subjectTest = new BehaviorSubjectTest();
    OperaterTest operaterTest = new OperaterTest();

    private void test() {

        fun();

//        new BaseDemo().test2();

        // 线程调度
//        new SchedulerTest().test1();
//        new SchedulerTest().test2();

        //BackPressure
//        BackPressureTest.test1();
//        BackPressureTest.test2();

        // 操作符
//        operaterTest.map(this);
//        operaterTest.flatMap();
//        operaterTest.zip();
//        operaterTest.retry();
//        operaterTest.retryWhen();
//        operaterTest.retryWhen2();
//        operaterTest.retryWhen3();
//        operaterTest.take();
//        operaterTest.fliter();
//        operaterTest.doOnNext();
//        operaterTest.range();
//        operaterTest.just();
//        operaterTest.defer();
//        operaterTest.interval();
//        operaterTest.repeat();
//        operaterTest.repeatWhen();


//        asObservable.getData();
//        asObservable.test();
//        subjectTest.asObservable();
//        subjectTest.asObserver();


    }

    Subscription subscription;

    private void fun() {
        Observable<Integer> observable1 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("111 开始执行");
                subscriber.onNext(1);
                    //
                subscriber.onCompleted();
                System.out.println("111 执行完毕");
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> observable2 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("222 开始执行");
                subscriber.onNext(2);
                subscriber.onCompleted();
                System.out.println("222 执行完毕");
            }
        }).subscribeOn(Schedulers.io());
        Observable<Integer> observable3 = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("333 开始执行");
                subscriber.onNext(3);
                subscriber.onCompleted();
                System.out.println("333 执行完毕");
            }
        }).subscribeOn(Schedulers.io());
//        Observable.concat(observable1,observable2,observable3)
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        System.out.println(integer);
//                    }
//                });

//        Observable.merge(observable1, observable2, observable3)
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("onError");
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        System.out.println("onNext  "+integer);
//                    }
//                });

//        Observable.zip(observable1, observable3, new Func2<Integer, Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer, Integer integer2) {
//                System.out.println("call  "+integer+" "+integer2);
//                return integer+integer2;
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                System.out.println(integer);
//            }
//        });



        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                System.out.println("开始休眠");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("继续执行");
                subscriber.onNext(2);
                subscriber.onCompleted();

            }
        })

        .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
//                .doOnNext(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        System.out.println("doOnNext  "+integer +" "+Thread.currentThread().getName());
//                    }
//                })
//                .observeOn(Schedulers.newThread())
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
                        System.out.println("onNext  "+integer+" "+Thread.currentThread().getName());
                    }
                });

        subscription.unsubscribe();

        subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onCompleted();

            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
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
                        System.out.println("onNext  "+integer+" "+Thread.currentThread().getName());
                    }
                });


        subscription.unsubscribe();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("bbbbbbb");
//                subscription.unsubscribe();
//            }
//        },500);





//
//        subscription=Observable.just(1,2,3)
//                .observeOn(Schedulers.io())
//                .doOnNext(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        System.out.println("222  doOnNext start"+integer+" "+Thread.currentThread().getName());
//                        try {
//                            Thread.sleep(5*1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("222  doOnNext end"+integer+" "+Thread.currentThread().getName());
//                    }
//                })
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        System.out.println("222  ->"+integer);
//                    }
//                });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                subscription.unsubscribe();
//            }
//        },1000);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=subscription&&!subscription.isUnsubscribed()){
            System.out.println("onDestroy unsubscribe");
            subscription.unsubscribe();
        }
        System.out.println(this.hashCode());
    }
}