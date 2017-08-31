package cn.com.chaoba.rxjavademo.combining;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class ZipActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("zipWith");
        mLButton.setOnClickListener(e -> zipWithObserver().subscribe(i -> log("zipWith:" + i + "\n")));
        mRButton.setText("zip");
        mRButton.setOnClickListener(e -> zipWithIterableObserver().subscribe(i -> log("zip:" + i + "\n")));
    }

    /**
     * observable1.zipWith(observable2,Func2)
     * 结果等同Observable.zip(observable1，observable2，func2)
     * @return
     */
    private Observable<String> zipWithObserver() {
        return createObserver(2).zipWith(createObserver(3), new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + "-" + s2;
            }
        });
//        return createObserver(2).zipWith(createObserver(3), (s, s2) -> s + "-" + s2);
    }

    /**
     * zip(obervable1,observable2,func2)
     * 同时执行observable1，observable2，然后将两者的结果合并后发射给订阅者
     * 与merge相比，zip需要合并结果
     * 参考：https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247484322&idx=1&sn=70e6c88cfcd518f2134e80d3b4fdf309&chksm=96cda2efa1ba2bf90007379eef18d2ea3976ced1ce0aa47e9fd929652760927d2d9b23502ad9&scene=21#wechat_redirect
     * @return
     */
    private Observable<String> zipWithIterableObserver() {
        return Observable.zip(createObserver(2), createObserver(3), createObserver(4), (s, s2, s3) -> s + "-" + s2 + "-" + s3);
    }

    private Observable<String> createObserver(int index) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 1; i <= index; i++) {
                    log("emitted:" + index + "-" + i);
                    subscriber.onNext(index + "-" + i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }


    /**
     * zip:
     * 组合的过程是分别从两根水管里各取出一个事件来进行组合, 并且一个事件只能被使用一次, 组合的顺序是严格按照事件发送的顺利来进行的,
     * 最终下游收到的事件数量是和上游中发送事件最少的那一根水管的事件数量相同. 这个也很好理解, 因为是从每一根水管里取一个事件来进行合并,
     * 最少的那个肯定就最先取完, 这个时候其他的水管尽管还有事件, 但是已经没有足够的事件来组合了, 因此下游就不会收到剩余的事件了.
     * <p>
     * 接收到的个数为最少发射的个数，并且按照事件发射的顺序来取，比如observable1发射了1，2，3，此时observable2才发射1，则会将(1，1)进行合并后发射，
     * observable1又继续发射了4，5，6，observable2才发射2，则会从observable1按事件顺序取出2，和observable2中发射的2，合并(2,2)发射出去.
     */
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


}
