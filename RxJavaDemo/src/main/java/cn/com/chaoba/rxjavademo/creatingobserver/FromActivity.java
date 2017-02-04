package cn.com.chaoba.rxjavademo.creatingobserver;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Action1;

public class FromActivity extends BaseActivity {
    Integer[] arrays = {0, 1, 2, 3, 4, 5};
    List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i <= 5; i++) {
            list.add(i);
        }
        mLButton.setText("FromArray");
        mRButton.setText("FromIterable");
    }

    @Override
    protected void onLeftClick(View v) {
        FromArray().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                log("FromArray:" + i);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        FromIterable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer i) {
                log("FromArray:" + i);
            }
        });
    }

    /**
     *  From操作符用来将某个对象转化为Observable对象，并且依次将其内容发射出去.
     *  与just的区别，just也是将一组对象依次发射出去，但是just如果发射数组，会将数组作为一个对象发射出去
     *  比如 just(array).则array会作为一个对象发射出去
     *  而from(array)会将array中的对象依次发射出去
     */
    private Observable<Integer> FromArray() {
        return Observable.from(arrays);
    }

    private Observable<Integer> FromIterable() {
        return Observable.from(list);
    }
}
