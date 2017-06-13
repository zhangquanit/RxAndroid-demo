package cn.com.chaoba.rxjavademo.errorhandling;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class OnErrorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("onErrorReturn");
        mLButton.setOnClickListener(e -> onErrorReturnObserver().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onErrorReturn-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("onErrorReturn-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onErrorReturn-onNext:" + s);
            }
        }));
        mRButton.setText("onErrorResume");
        mRButton.setOnClickListener(e -> onErrorResumeNextObserver().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onErrorResume-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("onErrorResume-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onErrorResume-onNext:" + s);
            }
        }));
    }

    /**
     * onErrorReturn
     * 当Observable出现异常时(subscriber.onError())，将异常转换为字符串描述,然后回调onNext(string)
     * 注意：不会回调onError()
     */
    private Observable<String> onErrorReturnObserver() {
        return createObserver().onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                if(throwable instanceof NullPointerException){
                    return "出现NullPointerException";
                }
                return "onErrorReturn";
            }
        });

    }

    /**
     * onErrorResumeNext(newObservable)
     * 当Observable出现异常时(subscriber.onError())，重新发射新的newObservable,回调subcriber.onNext()
     */
    private Observable<String> onErrorResumeNextObserver() {
        return createObserver().onErrorResumeNext(Observable.just("7", "8", "9"));
    }

    private Observable<String> createObserver() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 1; i <= 6; i++) {
                    if (i < 3) {
                        subscriber.onNext("onNext:" + i);
                    } else {
                        subscriber.onError(new Throwable("Throw error"));
                    }
                }
            }
        });
    }
}
