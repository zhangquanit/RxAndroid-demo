package com.rxbinding.demo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private final Subject<Integer> mBus;
    private Subject<Integer> mBackgroundSubject;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    ObservableEmitter emitter;

    public RxBus() {
        mBus = PublishSubject.create();


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                emitter = e;
            }
        }).observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer code) throws Exception {
                        //发射给事件总线
                        mBus.onNext(code);
                    }
                });
//        mBackgroundSubject = PublishSubject.create();
//        mBackgroundSubject.observeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer code) throws Exception {
//                //发射给事件总线
//                mBus.onNext(code);
//            }
//        });
    }

    public void post(int code) {
        emitter.onNext(code);
//        mBackgroundSubject.onNext(code);
    }

    public synchronized void addSubscribe(final int code, final Consumer<Integer> consumer) {
        //ofType=filter+cast
        Disposable subscribe = mBus.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer curCode) throws Exception {
                System.out.println("test "+curCode);
                //过滤  只处理自己感兴趣的事件
                return curCode == code;
            }
        }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Integer.valueOf("a");
            }
        })
                .subscribe(consumer, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                System.out.println("发生异常");
                //重新订阅（发生异常后会自动取消订阅，所有需要重新订阅）
                addSubscribe(code, consumer);
                //统一抛出rpc异常，各业务调用相互不影响
            }
        });
        mCompositeDisposable.add(subscribe);

    }

    public void unscribe() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}
