package com.rxandroid.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rxandroid.demo.rxjava.BehaviorSubjectTest;
import com.rxandroid.demo.rxjava.OperaterTest;

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
//        new BaseDemo().test2();

        // 线程调度
//        new SchedulerTest().test1();
//        new SchedulerTest().test2();

        // 操作符
//        operaterTest.map(this);
//        operaterTest.flatMap();
//        operaterTest.zip();
//        operaterTest.retry();
//        operaterTest.retryWhen();
//        operaterTest.retryWhen2();
        operaterTest.retryWhen3();
//        operaterTest.take();
//        operaterTest.fliter();
//        operaterTest.doOnNext();
//        operaterTest.range();
//        operaterTest.just();
//        operaterTest.defer();
//        operaterTest.interval();
//        operaterTest.repeat();
//        operaterTest.repeatWhen();


//        subjectTest.getData();
//        subjectTest.test();
    }


}