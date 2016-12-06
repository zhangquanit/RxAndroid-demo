package com.rxandroid.demo.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 在 RxJava 的默认规则中，事件的发出和消费都是在同一个线程的。而要实现异步，则需要用到 RxJava 的另一个概念： Scheduler 。
 * <p>
 * 在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
 * <p>
 * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
 * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多， 区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
 * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 * AndroidSchedulers.mainThread()：它指定的操作将在 Android 主线程运行。
 * <p>
 * 有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。
 * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
 * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
 *
 * @author 张全
 */
public class SchedulerTest {

    public void test1() {
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                String name = Thread.currentThread().getName();
                System.out.println("subscriber->thread:" + name + "," + integer);

            }
        };
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                String name = Thread.currentThread().getName();
                int data = 1;
                System.out.println("subscriber->thread:" + name + "," + data);
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(subscriber);
    }

    /**
     * 线程之间的多次切换
     * observeOn() 指定的是 Subscriber 的线程，而这个 Subscriber 并不是subscribe() 参数中的 Subscriber ，而是 observeOn() 执行时的当前 Observable 所对应的 Subscriber ，即它的直接下级 Subscriber 。换句话说，observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可
     * 通过 observeOn() 的多次调用，程序实现了线程的多次切换。
       不过，不同于 observeOn() ， subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。
     */
    public void test2(){

        Subscriber subscriber= new Subscriber<Integer>(){

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer s) {
                String threadName = Thread.currentThread().getName();
                System.out.println(s+"->onNext----->"+threadName+",s="+s);
            }
        };

        Observable.just(1, 2) // IO 线程，由 subscribeOn() 指定
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, String>() { // 新线程，由 observeOn() 指定

                    @Override
                    public String call(Integer integer) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(integer+"->Schedulers.newThread()->"+threadName);
                        return String.valueOf(integer);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, Integer>() { // IO 线程，由 observeOn() 指定

                    @Override
                    public Integer call(String s) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(s+"->Schedulers.io()->"+threadName);
                        return Integer.valueOf(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);  // Android 主线程，由 observeOn() 指定
    }
}
