package cn.com.chaoba.rxjavademo.custom;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Observable.Operator;
import rx.Observable.Transformer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class CustomActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("lift");
        mLButton.setOnClickListener(e -> liftObserver().subscribe(s -> log("lift:" + s)));
        mRButton.setText("compose");
        mRButton.setOnClickListener(e -> composeObserver().subscribe(s -> log("compose:" + s)));
    }

    /**
     * 自定义操作符
     * @return
     */
    private Observable<String> liftObserver() {
        Operator<String, String> myOperator = new Operator<String, String>() {
            @Override
            public Subscriber<? super String> call(Subscriber<? super String> subscriber) {
                return new Subscriber<String>(subscriber) {
                    @Override
                    public void onCompleted() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext("myOperator:" + s);
                        }
                    }
                };
            }

        };
        return Observable.just(1, 2, 3).map(integer -> "map1:" + integer).lift(myOperator).map(s -> "map2:" + s);
    }

    /**
     * compose
     * 是唯一一个能从流中获取原生Observable 的方法，因此，影响整个流的操作符（像subscribeOn()和observeOn()）需要使用compose()，
     * 相对的，如果你在flatMap()中使用subscribeOn()/observeOn()，它只影响你创建的flatMap()中的Observable,而不是整个流。
     * @return
     */
    private Observable<String> composeObserver() {
        Transformer<Integer, String> myTransformer = new Transformer<Integer, String>() {
            @Override
            public Observable<String> call(Observable<Integer> integerObservable) {
                //
                return integerObservable
                        .map(new Func1<Integer, String>() {
                            @Override
                            public String call(Integer integer) {
                                return  "myTransforer:" + integer;
                            }
                        }).doOnNext(new Action1<String>() {
                            @Override
                            public void call(String o) {
                                log("doOnNext:" + o);
                            }
                        });
//                return integerObservable
//                        .map(integer -> "myTransforer:" + integer)
//                        .doOnNext(s -> log("doOnNext:" + s));
            }
        };
        return Observable.just(1, 2, 3).compose(myTransformer);
    }

}
