package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;
import android.view.View;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

public class GroupbyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("groupBy");
        mRButton.setText("groupByKeyValue");
    }

    @Override
    protected void onLeftClick(View v) {
        groupByObserver().subscribe(new Subscriber<GroupedObservable<Integer, Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GroupedObservable<Integer, Integer> groupedObservable) {
                groupedObservable.count().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log("key" + groupedObservable.getKey() + " contains:" + integer + " numbers");
                    }
                });
//                groupedObservable.count().subscribe(integer -> log("key" + groupedObservable.getKey() + " contains:" + integer + " numbers"));
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        groupByKeyValueObserver().subscribe(new Subscriber<GroupedObservable<Integer, String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GroupedObservable<Integer, String> groupedObservable) {
                if (groupedObservable.getKey() == 0) {
                    groupedObservable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            log(s);
                        }
                    });
//                    integerIntegerGroupedObservable.subscribe(integer -> log(integer));
                }
            }
        });
    }

    /**
     * GroupBy操作符将原始Observable发射的数据按照key来拆分成一些小的Observable，然后这些小的Observable分别发射其所包含的的数据，类似于sql里面的groupBy。
     * 在使用中，我们需要提供一个生成key的规则，所有key相同的数据会包含在同一个小的Observable中。
     * 另外我们还可以提供一个函数来对这些数据进行转化，有点类似于集成了flatMap。
     */
    private Observable<GroupedObservable<Integer, Integer>> groupByObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                //在使用中，我们需要提供一个生成key的规则，所有key相同的数据会包含在同一个小的Observable中。
                return integer % 2;
            }
        });
    }

    private Observable<GroupedObservable<Integer, String>> groupByKeyValueObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                //指定生成key的规则
                return integer % 2;
            }
        }, new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                //将Integer转换为String
                return "groupByKeyValue:" + integer;
            }
        });
//        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
//                .groupBy(integer -> integer % 2, integer -> "groupByKeyValue:" + integer);
    }
}