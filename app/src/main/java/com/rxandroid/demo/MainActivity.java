package com.rxandroid.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rxandroid.demo.rxjava.BehaviorSubjectTest;
import com.rxandroid.demo.rxjava.OperaterTest;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.button_run_scheduler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });


    }

    BehaviorSubjectTest subjectTest = new BehaviorSubjectTest();
    OperaterTest operaterTest = new OperaterTest();

    private void test() {
//        fun();

//        new BaseDemo().test2();

        // 线程调度
//        new SchedulerTest().test1();
//        new SchedulerTest().test2();

        //BackPressure
//        BackPressureTest.test1();
//        BackPressureTest.test2();

        // 操作符
//        operaterTest.map(this);
//        operaterTest.flatMap();
//        operaterTest.zip();
//        operaterTest.retry();
//        operaterTest.retryWhen();
//        operaterTest.retryWhen2();
//        operaterTest.retryWhen3();
//        operaterTest.take();
//        operaterTest.fliter();
//        operaterTest.doOnNext();
//        operaterTest.range();
//        operaterTest.just();
//        operaterTest.defer();
//        operaterTest.interval();
//        operaterTest.repeat();
        operaterTest.repeatWhen();


//        subjectTest.getData();
//        subjectTest.test();
    }

    private  void fun(){

        Observable.from(new Integer[]{1,2})
                .map(new Func1<Integer, Integer>() {

                    @Override
                    public Integer call(Integer integer) {
                        return integer+1;
                    }
                })
               .filter(new Func1<Integer, Boolean>() {
                   @Override
                   public Boolean call(Integer integer) {
                       if(integer==2)return true;
                       return false;
                   }
               }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });


    }



}