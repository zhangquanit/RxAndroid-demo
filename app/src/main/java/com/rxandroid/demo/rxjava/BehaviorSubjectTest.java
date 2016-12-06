package com.rxandroid.demo.rxjava;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * RxJava 中有一个较少被人用到的类叫做 Subject，它是一种『既是 Observable，又是 Observer』的东西，因此可以被用作中间件来做数据传递。
 * 例如，可以用它的子类 BehaviorSubject 来制作缓存。
 * 代码大致形式：
 * api.getData().subscribe(behaviorSubject); // 网络数据会被缓存，behaviorSubject作为观察者
 * behaviorSubject.subscribe(observer); // 之前的缓存将直接送达 observer，behaviorSubject作为被观察者
 * 因此数据传递顺序：behaviorSubject.onNext()------>observer.onNext()
 */
public class BehaviorSubjectTest {

    BehaviorSubject<String> cache;
    String mCacheData;

    public void test(){
        Observer<String> observer = new Observer<String>() {
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
                System.out.println("onNext----s=" + s);
            }
        };

        final  BehaviorSubject<String> subject = BehaviorSubject.create();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subject.onNext("hello"); //因为subject.subscribe(observer); 所以observer的onNext会被调用
            }
        })
                .subscribeOn(Schedulers.io())
//                .subscribe(subject); //subject作为观察者
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        subject.onNext(s);
                    }
                });

        //subject作为被观察者,一旦subject.onNext()被调用，则转调observer.onNext()
        subject.subscribe(observer);
    }
    public void getData() {
        Observer<String> observer = new Observer<String>() {
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
                System.out.println("onNext----s=" + s);
            }
        };
        loadData(observer);
    }

    private Subscription loadData(Observer<String> observer) {
//        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    System.out.println("call------mCacheData="+mCacheData);
                    if (mCacheData == null) {
                        loadFromNetwork();
                    } else {
                        subscriber.onNext(mCacheData);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);
//        }
        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    /**
     * 从网络加载数据，BehaviorSubject作为观察者
     */
    private void loadFromNetwork() {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                //从网络中获取数据
                System.out.println("--------从网络中获取数据");
                subscriber.onNext("mCacheData from network");
            }
        })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //添加到本地缓存中
                        System.out.println("--------从网络中获取数据，添加到本地缓存中");
                        mCacheData = "mCacheData from cache";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("cache.onNext");
                        cache.onNext(s);
                    }
                });
    }

}
