package cn.com.chaoba.rxjavademo.connectable;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

public class RefCountActivity extends BaseActivity {
    Subscription subscription;
    ConnectableObservable<Long> obs = publishObserver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mLButton.setText("refCount");
//        mLButton.setOnClickListener(e -> subscription = obs.refCount().subscribe(aLong -> {
//            log("refCount:" + aLong);
//        }));
        mRButton.setText("stop");
//        mRButton.setOnClickListener(e -> subscription.unsubscribe());
    }

    @Override
    protected void onLeftClick(View v) {

        /**
         *  Connectable Observable
         */
  /*    final  Subscription sub = obs.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("refCount:" + aLong);
                if(aLong==5){

                }
            }
        });

        subscription= obs.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                log("refCount2:" + aLong);
                if(aLong==5){
                    sub.unsubscribe(); //注销observer1
                }
            }
        });

        obs.connect();*/


        /**
         *
         */
//        Observable<Long> refObservable = obs.refCount();
//        final  Subscription sub = refObservable.subscribe(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
//                log("refCount :" + aLong);
//            }
//        });
//
//        subscription= refObservable.subscribe(new Action1<Long>() {
//            @Override
//            public void call(Long aLong) {
//                log("refCount2 :" + aLong);
//                if(aLong==5){
//                    sub.unsubscribe();
//                }
//            }
//        });
//
//        obs.connect();

//        refCountObservable();
        connectObservableTest();
//        normalObservable();
    }

    @Override
    protected void onRightClick(View v) {
        subscription.unsubscribe();
    }

    private ConnectableObservable<Long> publishObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.publish();
    }


    /**
     * refCount
     * 将ConnectObservable转换成普通的Observalbe，当订阅被取消后重新订阅不会重新执行
     */
    Subscription subscrption11;
    Subscription subscrption12;

    private void refCountObservable() {
        ConnectableObservable<Long> connectableObservable = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread()).publish();

        final Observable<Long> observable = connectableObservable.refCount();

        final Subscriber<Long> subscriber3 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext3................." + integer);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted3.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError3.....................");
            }
        };

        final Subscriber<Long> subscriber1 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext1................." + integer);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted1.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError1.....................");
            }
        };

        Subscriber<Long> subscriber2 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext2................." + integer);
                if (integer == 1) {
                    subscrption11.unsubscribe(); //取消订阅，之后即使重新订阅也不会再重新执行
                } else if (integer == 3) {
                    observable.subscribe(subscriber1); //将ConnectObservable转换成普通的Observalbe，当订阅被取消后重新订阅不会重新执行
                    observable.subscribe(subscriber3); //会接收到observable发射的数据
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted2.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError2.....................");
            }
        };

        subscrption12 = observable.subscribe(subscriber2);
        subscrption11 = observable.subscribe(subscriber1);
/**
 08-11 15:30:42.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................0
 08-11 15:30:42.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext1.................0
 08-11 15:30:43.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................1
 08-11 15:30:44.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................2
 08-11 15:30:45.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................3
 08-11 15:30:46.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................4
 08-11 15:30:46.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext3.................4  //
 08-11 15:30:47.830 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................5
 08-11 15:30:47.830 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext3.................5  //
 08-11 15:30:48.828 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext2.................6
 08-11 15:30:48.829 23129-23863/cn.com.chaoba.rxjavademo I/System.out: onNext3.................6  //
 */

    }

    /**
     * ConnectObservable
     */
    Subscription subscription2;
    Subscription subscribe21;
    Subscription subscribe22;

    private void connectObservableTest() {
        final ConnectableObservable<Long> observable =
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .publish();

        final Subscriber<Long> subscriber3 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext3................." + integer);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted3.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError3.....................");
            }
        };

        final Subscriber<Long> subscriber1 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext1................." + integer);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted1.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError1.....................");
            }
        };

        Subscriber<Long> subscriber2 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext2................." + integer);
                if (integer == 1) {
                    subscribe21.unsubscribe(); //取消订阅，之后即使重新订阅也不会再重新执行
                } else if (integer == 3) {
                    observable.subscribe(subscriber1);
                    observable.subscribe(subscriber3); //会接收observable发射的数据
                } else if (integer == 10) {
                    subscription2.unsubscribe(); //注销所有的订阅
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted2.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError2.....................");
            }
        };

        subscribe22 = observable.subscribe(subscriber2);
        subscribe21 = observable.subscribe(subscriber1);

        subscription2 = observable.connect(); //发射数据
/**
 08-11 15:35:42.481 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................0
 08-11 15:35:42.481 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext1.................0
 08-11 15:35:43.480 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................1
 08-11 15:35:44.480 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................2
 08-11 15:35:45.481 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................3
 08-11 15:35:46.520 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................4
 08-11 15:35:46.520 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................4  //
 08-11 15:35:47.514 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................5
 08-11 15:35:47.514 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................5  //
 08-11 15:35:48.514 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................6
 08-11 15:35:48.514 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................6  //
 08-11 15:35:49.514 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................7
 08-11 15:35:49.515 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................7  //
 08-11 15:35:50.513 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................8
 08-11 15:35:50.513 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................8  //
 08-11 15:35:51.516 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................9
 08-11 15:35:51.516 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................9  //
 08-11 15:35:52.518 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext2.................10
 08-11 15:35:52.518 25348-25504/cn.com.chaoba.rxjavademo I/System.out: onNext3.................10 //
 *
 */
    }

    /**
     * 普通的Observable
     */
    private void normalObservable() {
        final Observable<Long> observable =
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread());

        final Subscriber<Long> subscriber3 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext3................." + integer);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted3.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError3.....................");
            }
        };

        final Subscriber<Long> subscriber1 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext1................." + integer);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted1.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError1.....................");
            }
        };

        Subscriber<Long> subscriber2 = new Subscriber<Long>() {
            @Override
            public void onNext(Long integer) {
                System.out.println("onNext2................." + integer);
                if (integer == 1) {
                    subscriber1.unsubscribe(); //取消订阅，之后即使重新订阅也不会再重新执行
                } else if (integer == 3) {
                    observable.subscribe(subscriber1); //不会重新执行
                    observable.subscribe(subscriber3); //subscriber3 会重新订阅到所发射的数据 0，1，2，3，4，5，......
                }
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted2.................");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError2.....................");
            }
        };

        observable.subscribe(subscriber2);
        observable.subscribe(subscriber1);

/**
 08-11 15:17:09.292 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................0
 08-11 15:17:09.292 22525-22813/cn.com.chaoba.rxjavademo I/System.out: onNext1.................0
 08-11 15:17:10.291 22525-22813/cn.com.chaoba.rxjavademo I/System.out: onNext1.................1
 08-11 15:17:10.292 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................1
 08-11 15:17:11.292 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................2
 08-11 15:17:12.292 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................3
 08-11 15:17:13.292 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................4
 08-11 15:17:13.294 22525-22872/cn.com.chaoba.rxjavademo I/System.out: onNext3.................0
 08-11 15:17:14.293 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................5
 08-11 15:17:14.294 22525-22872/cn.com.chaoba.rxjavademo I/System.out: onNext3.................1
 08-11 15:17:15.293 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................6
 08-11 15:17:15.294 22525-22872/cn.com.chaoba.rxjavademo I/System.out: onNext3.................2
 08-11 15:17:16.294 22525-22814/cn.com.chaoba.rxjavademo I/System.out: onNext2.................7
 *
 */
    }
}
