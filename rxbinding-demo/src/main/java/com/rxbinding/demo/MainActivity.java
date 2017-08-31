package com.rxbinding.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.create;

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


//        rxBusTest();
//
//
//        testObservable();
//        onClickView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                rxBus.post(1);
////                rxBus.post(2);
//                emitter.onNext(1);
//            }
//        });

//        sendNullTest();
//        onEndTest();
//        takeUtilWhileTest();
//        doTermTest();
//        doCompleeTest();

//        switchMapTest();
        doOnTest();

//        mergeTest();
//        publishTest();
//        onTerminateDetachTest();

//        rxBindingTest();
//        justSubscribe();
//        normalTest();
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


//        final AlertDialog alertDialog = new AlertDialog.Builder(this)
//                .setTitle("title")
//                .setMessage("message")
//                .setPositiveButton("sure", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create();
//
//        alertDialog.show();
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                System.out.println("onSubscribe "+Thread.currentThread().getName());
//                Thread.sleep(5*1000);
//                alertDialog.dismiss();
////                finish();
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        System.out.println("doOnTerminate");
//                    }
//                })
//                .doAfterTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        System.out.println("doAfterTerminate");
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println("onNext " + s);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        System.out.println("onError " + throwable.getMessage());
//                    }
//                });

        System.out.println("MainActivity修改2");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    private void justSubscribe() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                System.out.println("subscribe");
                Integer.valueOf("a");
//                e.onNext("a");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnTerminate " + Thread.currentThread().getName());
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("onNext " + Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("onError");
            }
        });
    }

    private void sendNullTest() {
        create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                e.onNext(null);
                Integer.valueOf("a");
            }
        }).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("doOnNext  " + o);
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

    }


    private void normalTest() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                System.out.println("subscribe " + Thread.currentThread().getName());
                e.onNext("a");
                e.onNext("b");
                Integer.valueOf("c");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println("normalTest doOnNext " + s + " " + Thread.currentThread().getName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("normalTest doAfterTerminate");
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        System.out.println("normalTest onNext " + s + " " + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println("normalTest onError " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("normalTest onComplete");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        System.out.println("normalTest onSubscribe");
                    }
                });
        compositeDisposable.add(disposable);
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

    private void takeUtilWhileTest() {
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println(aLong);
                    }
                });

    }

    private void doTermTest() {
        Observable.just(1)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Integer.valueOf("a");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doAfterTerminate");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        System.out.println("onNext " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private void doCompleteTest() {
        create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onComplete();
            }
        }).subscribe(Functions.<String>emptyConsumer(), Functions.ON_ERROR_MISSING, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("onComplete");
            }
        });
    }

    private void switchMapTest() {
        Observable.just(10, 20, 30).switchMap(
                new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Integer integer) throws Exception {
                        return Observable.just(integer).delay(0, TimeUnit.MILLISECONDS);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("switchMap onNext:" + o);
                    }
                });
    }

    private void doOnTest() {

        Observable<Integer> observable = create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onError(new NullPointerException("this is NullPointerException"));
                e.onComplete();
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        System.out.println("doOnSubscribe ");
                    }
                })
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        System.out.println("doOnEach  " + integerNotification.getValue());
                    }
                })
                .doOnEach(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("[doOnEach Observer] onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        System.out.println("[doOnEach Observer] onNext " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("[doOnEach Observer] onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("[doOnEach Observer] onComplete");
                    }
                })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("doOnNext ");
                    }
                })
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("doAfterNext");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doAfterTerminate");
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnComplete");
                    }
                }).doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doFinally");
                    }
                }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnTerminate");
                    }
                }).doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("doOnDispose");
                    }
                });

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                System.out.println("onNext " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("onError ");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete ");
            }
        });

        /**
         07-26 16:04:38.551 16387-16387/com.rxbinding.demo I/System.out: doOnSubscribe
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doOnEach  1
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doOnNext
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: onNext 1
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doAfterNext
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doOnEach  2
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doOnNext
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: onNext 2
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doAfterNext
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doOnEach  null
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doOnComplete
         07-26 16:04:38.552 16387-16387/com.rxbinding.demo I/System.out: doFinally
         07-26 16:04:38.553 16387-16387/com.rxbinding.demo I/System.out: doAfterTerminate
         */

    }

    private void publishTest() {
        final ConnectableObservable<String> observable = create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                for (int i = 0; i < 10; i++) {
                    e.onNext("a" + i);
                    Thread.sleep(100);
                }
            }
        })
                .observeOn(Schedulers.newThread())
                .publish();

        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("before onNext=" + s);
            }
        });
        observable.connect();
    }

    private void mergeTest() {
        Observable<Integer> observable1 = create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1 * 1500);
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> observable2 = create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 5; i < 10; i++) {
                    Thread.sleep(1 * 1000);
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.merge(observable1, observable2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext  " + integer);
                    }
                });
    }

    private void onTerminateDetachTest() {
        create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("start");
                Thread.sleep(5 * 1000);
                e.onNext("end");
                e.onComplete();
            }
        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .delay(5, TimeUnit.SECONDS)
                .onTerminateDetach()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext " + s);
                    }
                });


        ObservableTransformer<Integer, Integer> observableTransformer = new ObservableTransformer<Integer, Integer>() {
            @Override
            public ObservableSource<Integer> apply(@NonNull Observable<Integer> upstream) {
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                return upstream;
            }
        };

        Observable.just(1)
                .compose(observableTransformer);

        Observable.just(2)
                .compose(observableTransformer);

    }

    private void rxBindingTest() {
        View clickView = findViewById(R.id.onClick);
        Disposable disposable = RxView.clicks(clickView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("doOnNext " + o + " " + Thread.currentThread().getName());
                        num++;
                        if (num == 3) {
                            Integer.valueOf("a");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("onNext onClick on" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError ");
                        throwable.printStackTrace();
                    }
                });

        final TextView textView = (TextView) findViewById(R.id.result);
        EditText editText = (EditText) findViewById(R.id.et);
        RxView.textChanges(editText)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("doOnNext " + o + " " + Thread.currentThread().getName());
                        num++;
                        if (num == 3) {
                            Integer.valueOf("a");
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence text) throws Exception {
                        System.out.println("onNext onClick on" + Thread.currentThread().getName());
                        textView.setText(text);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError ");
                        throwable.printStackTrace();
                    }
                });

    }

    private int num;
}
