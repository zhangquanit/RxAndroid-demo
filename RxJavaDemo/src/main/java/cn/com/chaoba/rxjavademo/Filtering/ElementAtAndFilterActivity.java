package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Func1;

public class ElementAtAndFilterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("elementAt");
        mLButton.setOnClickListener(e -> elementAtObserver().subscribe(i -> log("elementAt:" + i)));
        mRButton.setText("Filter");
        mRButton.setOnClickListener(e -> filterObserver().subscribe(i -> log("Filter:" + i)));
    }

    /**
     * elementAt(index)
     * 返回指定index位置的数据
     * @return
     */
    private Observable<Integer> elementAtObserver() {
        return Observable.just(0, 1, 2, 3, 4, 5).elementAt(2);
    }

    /**
     * Filter只会返回满足过滤条件的数据
     * @return
     */
    private Observable<Integer> filterObserver() {
        return Observable.just(0, 1, 2, 3, 4, 5).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer<3;
            }
        });
    }




}
