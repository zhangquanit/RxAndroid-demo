package com.rxbinding.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends Activity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //监听点击事件
        View onClickView = findViewById(R.id.onClick);

//        Disposable subscription =
//                RxView.clicks(onClickView)
////                test(onClickView)
//                        .throttleFirst(1000, MILLISECONDS)
//                        .doOnNext(new Consumer<Object>() {
//                            @Override
//                            public void accept(Object o) throws Exception {
//                                System.out.println("故意出错");
//                                Integer.valueOf("a");
//                            }
//                        })
//                        .subscribe(new Consumer<Object>() {
//                            @Override
//                            public void accept(Object o) throws Exception {
//                                System.out.println("onClick");
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                System.out.println("onError " + throwable.getMessage());
//                            }
//                        });
//        compositeDisposable.add(subscription);


        rxBusTest();


        testObservable();
        onClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rxBus.post(1);
//                rxBus.post(2);
                emitter.onNext(1);
            }
        });

        sendNullTest();

        //监听文字变化
//        final TextView textView = (TextView) findViewById(R.id.result);
//        EditText editText = (EditText) findViewById(R.id.et);
//        Disposable subscribe = RxView.textChanges(editText)
//                .debounce(150, MILLISECONDS)
//                .subscribe(new Consumer<CharSequence>() {
//                    @Override
//                    public void accept(CharSequence charSequence) throws Exception {
//                        textView.setText(charSequence);
//                    }
//                });
//        compositeDisposable.add(subscribe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    private void  sendNullTest(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                e.onNext(null);
                Integer.valueOf("a");
            }
        }).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("doOnNext  "+o);
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("onNext  " + o);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("onError");
            }
        });


//        Observable.just(null)
//                .doOnNext(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        System.out.println("2222 doOnNext  "+o);
//                    }
//                }).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                System.out.println("2222 onNext  "+o);
//            }
//        });
    }

    ObservableEmitter emitter;
    Observable<Object> observable;

    private void testObservable() {

        observable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                emitter = e;
                System.out.println("subscribe");
//                emitter.onNext(1);
//                Integer.valueOf("a");
            }
        });

        observable.doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("doOnNext");
                Integer.valueOf("a");
            }
        });
    }

    RxBus rxBus = new RxBus();

    private void rxBusTest() {

        rxBus.addSubscribe(1, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("11111");
                Integer.valueOf("a");
            }
        });
        rxBus.addSubscribe(2, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("222222");
            }
        });

    }
}
