package com.rxandroid.demo.rxjava;


import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * RxJava的基本使用步骤
 * 1、创建观察者
 * 多种创建方式：Observer、Subscriber、ActionX(部分回调监听)
 * 2、创建被观察者
 * Observerable的创建方式有很多种，最基本的创建方式
 * Observable.create(Observable.OnSubscribe);
 * 3、订阅
 * observable.subscribe(subscriber);
 * 一旦产生订阅，就会执行Observable.OnSubscribe.call()方法。
 */
public class BaseDemo {
    /**
     * ------------------------观察者的几种创建方式-----------------------
     */

    private void observer1() {
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
    }

    private void observer2() {
        /**
         * Subscriber实现了Observer，并实现了Subscription（unsubscribe()、isUnsubscribed()）
         * 1、相比Observer，增加了onStart方法，提供取消订阅方法unsubscribe().
         */
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                System.out.println("onStart");
            }

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
                System.out.println("onNext,s=" + s);
            }
        };
    }

    /**
     * ActionX: 实现部分回调监听
     */
    private void observer3() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });
        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * ------------------被观察者的基本创建方式-----------------------------------
     */

    private void observable1() {
        /**
         * Observable.OnSubscribe
         */
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");

//                 Integer.valueOf("a"); //如果产生异常，则自动回调subscriber.onError();
//                subscriber.onError(new NullPointerException("value=null")); //手动调用onError

                subscriber.onCompleted();
            }
        });
    }

    private void observable2() {
        /**
         * just(T...): 将传入的参数依次发送出来。
         */
        Observable<String> just = Observable.just("Hello", "Hi", "Aloha");
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();
    }

    private void observable3() {
        /**
         *  from(T[]) : 将传入的数组拆分成具体对象后，依次发送出来。
         *  from(Iterable<? extends T>) ：将传入的 Iterable 拆分成具体对象后，依次发送出来。
         */
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable from = Observable.from(words);
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();
    }
    /**
     * ------------------观察者的基本使用-----------------------------------
     */

    public void test1() {
        //一、创建观察者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onStart() {
                System.out.println("onStart");
            }

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
                System.out.println("onNext,s=" + s);
            }
        };

        /**
         * 二、创建被观察者
         * Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
         * RxJava 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则
         * OnSubscribe 会被存储在返回的 Observable 对象中，它的作用相当于一个计划表，当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，事件序列就会依照设定依次触发
         *
         */
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });
        //create() 方法是 RxJava 最基本的创造事件序列的方法。基于这个方法， RxJava 还提供了一些方法用来快捷创建事件队列，例如：
//         Observable.just("Hello", "Hi", "Aloha");

        /**
         * 三、 Subscribe (订阅)
         */
        Subscription subscription = observable.subscribe(subscriber);

        //取消订阅
//        if(!subscription.isUnsubscribed()){
//            subscription.unsubscribe();
//        }

        /**
         * Observable.subscribe(Subscriber) 的内部实现是这样的（仅核心代码）：
         public Subscription subscribe(Subscriber subscriber) {
         subscriber.onStart();
         onSubscribe.call(subscriber);
         return subscriber;
         }
         */

        /**
         * 可以看到，subscriber() 做了3件事：
         调用 Subscriber.onStart() 。这个方法在前面已经介绍过，是一个可选的准备方法。
         调用 Observable 中的 OnSubscribe.call(Subscriber) 。在这里，事件发送的逻辑开始运行。从这也可以看出，在 RxJava 中， Observable 并不是在创建的时候就立即开始发送事件，而是在它被订阅的时候，即当 subscribe() 方法执行的时候。
         将传入的 Subscriber 作为 Subscription 返回。这是为了方便 unsubscribe().
         */
    }

    /**
     * 除了 subscribe(Observer) 和 subscribe(Subscriber) ，subscribe() 还支持不完整定义的回调，
     * RxJava 会自动根据定义创建出 Subscriber
     * ActionX：X代表参数个数，比如Action1表示有一个参数
     */
    public void  test2(){
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });


        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                System.out.println("onNext,s="+s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                System.out.println("onError");
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                System.out.println("onCompleted");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }
}
