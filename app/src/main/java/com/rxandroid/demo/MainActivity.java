package com.rxandroid.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.rxandroid.demo.rxjava.BehaviorSubjectTest;
import com.rxandroid.demo.rxjava.OperaterTest;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.button_run_scheduler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test();
//                exceptionTest();
//                zipTest();
                backpressureTest();
            }
        });

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

    }

    private void exceptionTest() {
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("a");
            }
        })
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("call " + Thread.currentThread().getName());
                        Integer.valueOf(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError " + Thread.currentThread().getName());
//                        throw  (RuntimeException)e;
                        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext " + s);
                    }
                });
    }

    public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
            System.out.println("uncaughtException");
        }
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


        Observable.concat(observable1, observable2, observable3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });


//
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
                        System.out.println("onNext  " + integer + " " + Thread.currentThread().getName());
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
                        System.out.println("onNext  " + integer + " " + Thread.currentThread().getName());
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

    private void zipTest() {
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    for (int i = 0; i < 10; i++) {
                        System.out.println("observable1 发射 " + i);
                        subscriber.onNext("no1-" + i);
                        Thread.sleep(1 * 1000);

                    }
                    subscriber.onCompleted();
                    System.out.println("observable1...onComplete");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    for (int i = 0; i < 4; i++) {
                        System.out.println("observable2 发射 " + i);
                        subscriber.onNext("no2-" + i);
                        Thread.sleep(2 * 1000);

                    }
                    subscriber.onCompleted();
                    System.out.println("observable2...onComplete");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + "&" + s2;
            }
        })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext s=" + s);
                    }
                });

    }

    private void backpressureTest() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 16; i++) {
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()) //事件产生和消费在不同io线程中
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {

                    @Override
                    public void call(Integer integer) {
                        try {
                            //休眠2000毫秒，模拟事件消费速度
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != subscription && !subscription.isUnsubscribed()) {
            System.out.println("onDestroy unsubscribe");
            subscription.unsubscribe();
        }
        System.out.println(this.hashCode());
    }
}