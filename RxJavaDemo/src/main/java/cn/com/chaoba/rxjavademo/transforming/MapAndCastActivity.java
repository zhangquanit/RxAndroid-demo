package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;
import android.view.View;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class MapAndCastActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("Map");
        mRButton.setText("Cast");
    }

    @Override
    protected void onLeftClick(View v) {
        mapObserver().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                log("Map:" + integer);
            }
        });
    }

    @Override
    protected void onRightClick(View v) {
        castObserver().subscribe(new Action1<Animal>() {
            @Override
            public void call(Animal dog) {
                log("Cast:" + dog.getName());
            }
        });
    }

    /**
     *  map():Map操作符的功能类似于FlatMap，不同之处在于它对数据的转化是直接进行的.
     * @return
     */
    private Observable<Integer> mapObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * 10;
            }
        });
    }

    /**
     * Cast将Observable发射的数据强制转化为另外一种类型，属于Map的一种具体的实现.
     */
    private Observable<Animal> castObserver() {
        return Observable.just(getAnimal())
                .cast(Animal.class);
    }

    Animal getAnimal() {
        return new Dog();
    }

    class Animal {
        protected String name = "Animal";

        Animal() {
            log("create " + name);
        }

        String getName() {
            return name;
        }
    }

    class Dog extends Animal {
        Dog() {
            name = getClass().getSimpleName();
            log("create " + name);
        }

    }
}
