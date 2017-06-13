package com.rxandroid.demo.rxjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rxandroid.demo.R;
import com.rxandroid.demo.entity.Course;
import com.rxandroid.demo.entity.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * RxJava操作符
 * RxJava 提供了对事件序列进行变换的支持，这是它的核心功能之一，也是大多数人说『RxJava 真是太好用了』的最大原因。
 * 所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列。
 * 变换的目的：订阅者无需关心事件类型是如何产生的，被观察者会将源事件经过一系列转换，最终转换为订阅者需要的事件类型。
 */

public class OperaterTest {

    /**
     * map(): 事件对象的直接变换，是一对一的转化
     * FuncX ：和ActionX类似，只不过FuncX 包装的是有返回值的方法，用于将参数类型转换为返回值类型
     */
    public void map(final Context ctx) {
        Observable.just(R.mipmap.ic_launcher) // 输入类型 Integer
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Integer, Bitmap>() { //将Integer类型转换为Bitmap类型,发生在当前线程中
                    @Override
                    public Bitmap call(Integer resId) { // 参数类型 Integer
                        String threadName = Thread.currentThread().getName();
                        System.out.println("Func1->threadName=" + threadName);
                        return BitmapFactory.decodeResource(ctx.getResources(), resId); // 返回类型 Bitmap
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println("Action1->threadName=" + threadName + ",bitmap=" + bitmap);
                    }
                });
    }

    /**
     * flatMap(): 和 map() 不同的是， flatMap() 中返回的是个 Observable 对象
     * (与Retrofit结合使用，可避免Callback的嵌套使用，比如先获取token，然后获取用户信息)
     * 打印出每个学生所需要修的所有课程的名称
     */
    public void flatMap() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("英语"));
        courses.add(new Course("语文"));
        int count = 3;
        Student[] students = new Student[count];
        for (int i = 0; i < count; i++) {
            Student student = new Student();
            student.setName("Jack" + i);
            student.setCourse(courses);
            students[i] = student;
        }

        /*
           不使用flatMap，在onNext中循环打印
         */
//        Subscriber<Student> subscriber = new Subscriber<Student>() {
//
//            @Override
//            public void onNext(Student student) {
//                //自己循环打印
//                List<Course> courses = student.getCourse();
//                for (int i = 0; i < courses.size(); i++) {
//                    Course course = courses.get(i);
//                    System.out.println(course.getName());
//                }
//            }
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//        };
//        Observable.from(students)
//                .subscribe(subscriber);

        Subscriber<Course> subscriber = new Subscriber<Course>() {

            @Override
            public void onNext(Course course) {
                System.out.println("onNext-----" + course.getName());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        System.out.println("flatMap----student.name=" + student.getName());
                        return Observable.from(student.getCourse());
                    }
                })
                .subscribe(subscriber);
        /**
         flatMap----student.name=Jack0
         onNext-----英语
         onNext-----语文
         flatMap----student.name=Jack1
         onNext-----英语
         onNext-----语文
         flatMap----student.name=Jack2
         onNext-----英语
         onNext-----语文
         onCompleted
         */
    }

    /**
     * zip(observale1,observable2,fun2(T1,T2,R))
     * 同时访问，然后将结果糅合后转换为统一的格式输出
     */
    public void zip() {
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    System.out.println("observable1---开始休眠");
                    Thread.sleep(2 * 1000);
                    System.out.println("observable1---开始发送,subscriber=" + subscriber);
                    subscriber.onNext("hello world");
                    System.out.println("observable1---发送完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> observable2 = Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    System.out.println("observable2---开始休眠");
                    Thread.sleep(5 * 1000);
                    System.out.println("observable2---开始发送,subscriber=" + subscriber);
                    subscriber.onNext(1);
                    System.out.println("observable2---发送完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io());

        //同时访问observable1，observable2，并将observable1,observable2的返回结果合并后输出为Map
        Observable.zip(observable1, observable2, new Func2<String, Integer, Map<String, Integer>>() {

            @Override
            public Map<String, Integer> call(String s, Integer integer) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put(s, integer);
                return map;
            }
        }).subscribe(new Subscriber<Map<String, Integer>>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onNext(Map<String, Integer> map) {
                System.out.println("onNext " + map);
            }
        });
        /**
         observable1---开始休眠
         observable2---开始休眠
         observable1---开始发送,subscriber=rx.internal.operators.OperatorSubscribeOn$1$1@7a46f15
         observable1---发送完毕
         observable2---开始发送,subscriber=rx.internal.operators.OperatorSubscribeOn$1$1@a19e22a
         onNext {hello world=1}
         observable2---发送完毕
         */

    }

    /**
     * 发生错误时，重试源Observable
     * <p>
     * retry(): 无限重试
     * retry(count): 重试count次
     */
    public void retry() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("call--------" + subscriber);
                subscriber.onNext("a");
                Integer.valueOf("a"); //发生错误
                subscriber.onCompleted();

            }
        })
                .retry(2) //如果发生错误  则重试2次
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError--" + e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext,s=" + s);
                    }
                });
/**
 12-06 11:10:37.536 14339-14339/com.rxandroid.demo I/System.out: call--------rx.internal.operators.OnSubscribeRedo$2$1@131358
 12-06 11:10:37.537 14339-14339/com.rxandroid.demo I/System.out: onNext,s=a
 12-06 11:10:37.538 14339-14339/com.rxandroid.demo I/System.out: call--------rx.internal.operators.OnSubscribeRedo$2$1@6143cb1
 12-06 11:10:37.538 14339-14339/com.rxandroid.demo I/System.out: onNext,s=a
 12-06 11:10:37.539 14339-14339/com.rxandroid.demo I/System.out: call--------rx.internal.operators.OnSubscribeRedo$2$1@301f596
 12-06 11:10:37.539 14339-14339/com.rxandroid.demo I/System.out: onNext,s=a
 12-06 11:10:37.539 14339-14339/com.rxandroid.demo I/System.out: onError--Invalid int: "a"
 */
    }

    /**
     * retryWhen(): 发生错误时,可以根据条件判断是否发生重试
     */
    String token = null;

    public void retryWhen() {
        Observable.just(null)
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        System.out.println("--------call,o=" + o + ",token=" + token);
                        if (token == null) {  //token为空时重试请求
                            return Observable.<String>error(new NullPointerException("token=null"));
                        }
                        return Observable.just(token);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    /**
                     * 关于方法参数：
                     1、Func1像个工厂类，用来实现你自己的重试逻辑。
                     2、输入的是一个Observable<Throwable>。
                     3、输出的是一个Observable<?>。
                     被返回的Observable<?>所要发送的事件决定了重订阅是否会发生。如果发送的是onCompleted或者onError事件，将不会触发重订阅。
                     相对的，如果它发送onNext事件，则触发重订阅（不管onNext实际上是什么事件）。这就是为什么使用了通配符作为泛型类型：这仅仅是个通知（next, error或者completed），一个很重要的通知而已。

                     source每次一调用onError(Throwable)，Observable<Throwable>都会被作为输入传入方法中。换句话说就是，它的每一次调用你都需要决定是否需要重订阅。
                     当订阅发生的时候，工厂Func1被调用，从而准备重试逻辑
                     */
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> errors) {
                        //当订阅发生的时候，工厂Func1被调用，从而准备重试逻辑
                        System.out.println("retryWhen----observable=" + errors);

                        /**
                         输入的Observable必须作为输出Observable的源。你必须对Observable<Throwable>做出反应，然后基于它发送事件；你不能只返回一个通用泛型流。
                         比如 return Observable.just(null);}
                         因为它不仅不能奏效，而且还会打断你的链式结构。你应该做的是，而且至少应该做的是，把输入作为结果返回，就像这样
                         return errors;
                         */

                        return errors.flatMap(new Func1<Throwable, Observable<?>>() {
                            //当Observable.error发生时才会进入
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                System.out.println("retryWhen--------call，throwable=" + throwable);

                                //对于NullPointerException，发生重试
                                if (throwable instanceof NullPointerException) {
                                    token = "xdfdfsdfdsf";
                                    return Observable.just(null);//
                                }
                                //对于其他错误，不用重试,则调用Subscriber的onError
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext----s=" + s);
                    }
                });

        //-----------第一次 token=null
        /**
         12-06 11:24:52.983 32298-32298/com.rxandroid.demo I/System.out: retryWhen----observable=rx.Observable@143ae2f
         12-06 11:24:52.990 32298-32298/com.rxandroid.demo I/System.out: --------call,o=null,token=null
         12-06 11:24:52.994 32298-32298/com.rxandroid.demo I/System.out: retryWhen--------call，throwable=java.lang.RuntimeException: mCacheData=null
         12-06 11:24:52.995 32298-32298/com.rxandroid.demo I/System.out: --------call,o=null,token=xdfdfsdfdsf
         12-06 11:24:52.995 32298-32298/com.rxandroid.demo I/System.out: onNext----s=xdfdfsdfdsf
         12-06 11:24:52.995 32298-32298/com.rxandroid.demo I/System.out: onCompleted
         */

        //---------第2次请求  token!=null
        /**
         12-06 11:26:23.568 32298-32298/com.rxandroid.demo I/System.out: retryWhen----observable=rx.Observable@3dac3c
         12-06 11:26:23.570 32298-32298/com.rxandroid.demo I/System.out: --------call,o=null,token=xdfdfsdfdsf
         12-06 11:26:23.570 32298-32298/com.rxandroid.demo I/System.out: onNext----s=xdfdfsdfdsf
         12-06 11:26:23.571 32298-32298/com.rxandroid.demo I/System.out: onCompleted
         */
    }

    /**
     * 使用.flatMap() + .timer()实现延迟重订阅
     */
    public void retryWhen2() {

        Observable.just(null)
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        System.out.println("--------call,o=" + o + ",token=" + token);
                        if (token == null) {  //token为空时重试请求
                            return Observable.<String>error(new NullPointerException("token=null"));
                        }
                        return Observable.just(token);
                    }
                })
               .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                   @Override
                   public Observable<?> call(Observable<? extends Throwable> errors) {
                       System.out.println("retryWhen--------call，observable=" + errors);
                       return errors.flatMap(new Func1<Throwable, Observable<?>>() {
                           @Override
                           public Observable<?> call(Throwable throwable) {
                               System.out.println("retryWhen--------call，throwable=" + throwable);
                               if(throwable instanceof  NullPointerException){
                                   token = "xdfdfsdfdsf";
                                   return Observable.timer(5,TimeUnit.SECONDS);//延迟5秒重订阅
                               }
                               return Observable.error(throwable);

                           }
                       });
                   }
               })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext,s="+s);
                    }
                });
    }
    /**
     * 使用.zip() + .range()实现有限次数的重订阅
     */
    public void retryWhen3() {

        Observable.just(null)
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        System.out.println("--------call,o=" + o + ",token=" + token);
                        if (token == null) {  //token为空时重试请求
                            return Observable.<String>error(new NullPointerException("token=null"));
                        }
                        return Observable.just(token);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> errors) {
//                        System.out.println("retryWhen--------call，observable=" + errors);
                        return errors.zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Integer>() {

                            @Override
                            public Integer call(Throwable throwable, Integer integer) {
                                System.out.println("retryWhen--------call，i="+integer+",throwable=" + throwable);
//                                if(integer==2){
//                                    token = "xdfdfsdfdsf";
//                                }
                                return  integer;
                            }
                        });
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext,s="+s);
                    }
                });
        /**
 12-06 14:02:27.610 31024-31024/com.rxandroid.demo I/System.out: --------call,o=null,token=null
 12-06 14:02:27.612 31024-31024/com.rxandroid.demo I/System.out: retryWhen--------call，i=1,throwable=java.lang.NullPointerException: token=null
 12-06 14:02:27.613 31024-31024/com.rxandroid.demo I/System.out: --------call,o=null,token=null
 12-06 14:02:27.613 31024-31024/com.rxandroid.demo I/System.out: retryWhen--------call，i=2,throwable=java.lang.NullPointerException: token=null
 12-06 14:02:27.613 31024-31024/com.rxandroid.demo I/System.out: --------call,o=null,token=xdfdfsdfdsf
 12-06 14:02:27.613 31024-31024/com.rxandroid.demo I/System.out: onNext,s=xdfdfsdfdsf
 12-06 14:02:27.613 31024-31024/com.rxandroid.demo I/System.out: onCompleted
         */
    }

    /**
     *  发送失败时，重试3次，每次延迟5秒执行
     */
    public void retryWhen4() {

        Observable.just(null)
                .flatMap(new Func1<Object, Observable<String>>() {
                    @Override
                    public Observable<String> call(Object o) {
                        System.out.println("--------call,o=" + o + ",token=" + token);
                        if (token == null) {  //token为空时重试请求
                            return Observable.error(new NullPointerException("token=null"));
                        }
                        return Observable.just(token);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> errors) {
//                        System.out.println("retryWhen--------call，observable=" + errors);
                        return errors.zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Integer>() {

                            @Override
                            public Integer call(Throwable throwable, Integer integer) {
                                System.out.println("retryWhen--------call，i=" + integer + ",throwable=" + throwable);
                                return integer;
                            }
                        }).flatMap(new Func1<Integer, Observable<?>>() {
                            @Override
                            public Observable<?> call(Integer integer) {
                                return Observable.timer(5,TimeUnit.SECONDS);
                            }
                        });
                    }
                })

                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext,s=" + s);
                    }
                });
    }

    /**
     * take(num): 从数据集合中最多取num个
     */
    public void take() {
        Integer[] data = new Integer[]{1, 2, 3, 4, 5};
        Observable.from(data)
                .take(6)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    /**
     * fliter()：过滤数据
     */
    public void fliter() {
        String[] data = new String[]{"1", null, "3"};
        Observable.from(data)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null; //过滤null
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    /**
     * doOnNext(): 允许我们在每次输出一个元素之前做一些额外的事情
     */
    public void doOnNext() {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("data from network");
            }
        })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //保存到本地缓存中
//                        saveToLocal(s);
                        System.out.println("doOnNext---,s=" + s);
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("s=" + s);
            }
        });
    }

    /**
     * range(n,m):从n开始，输入m个值
     */
    public void range() {
        Observable.range(10, 5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer); //10，11，12，13，14
                    }
                });


        //从一个list中截取指定范围的数据
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setName("姓名:" + i);
            students.add(student);
        }
        final List<Student> studentList = students;
        //从下标2开始，获取5个元素
        Observable.range(2, 5)
                .map(new Func1<Integer, Student>() {
                    @Override
                    public Student call(Integer integer) {
                        return studentList.get(integer);
                    }
                })
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        System.out.println(student.getName());
                    }
                });
/**
 12-05 18:07:00.643 4853-4853/com.rxandroid.demo I/System.out: 姓名:2
 12-05 18:07:00.643 4853-4853/com.rxandroid.demo I/System.out: 姓名:3
 12-05 18:07:00.643 4853-4853/com.rxandroid.demo I/System.out: 姓名:4
 12-05 18:07:00.643 4853-4853/com.rxandroid.demo I/System.out: 姓名:5
 12-05 18:07:00.643 4853-4853/com.rxandroid.demo I/System.out: 姓名:6
 */

    }

    Observable<Long> justObservable;

    public void just() {
        if (null == justObservable)
            justObservable = Observable.just(System.currentTimeMillis());
        justObservable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });
/**
 12-05 18:24:12.085 24850-24850/com.rxandroid.demo I/System.out: 1480933444151
 12-05 18:24:12.265 24850-24850/com.rxandroid.demo I/System.out: 1480933444151
 12-05 18:24:12.450 24850-24850/com.rxandroid.demo I/System.out: 1480933444151
 12-05 18:24:12.629 24850-24850/com.rxandroid.demo I/System.out: 1480933444151
 */
        //-----------just和from的区别
        Integer[] data = new Integer[]{1, 2, 3, 4};
        Observable.just(data) //将整个数组一起发射出去，而from(array)或from(iterator)会依次发射数组里的对象
                .subscribe(new Action1<Integer[]>() {
                    @Override
                    public void call(Integer[] ints) {
                        System.out.println("just:" + Arrays.toString(ints)); // [1, 2, 3, 4]
                    }
                });
        Observable.from(data)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("from:" + integer);
                    }
                });

    }

    private Observable<Long> deferObservable;

    /**
     * Defer操作符只有当有Subscriber来订阅的时候才会创建一个新的Observable对象,也就是说每次订阅都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的
     */
    public void defer() {
        if (null == deferObservable)
            deferObservable = Observable.defer(new Func0<Observable<Long>>() {
                @Override
                public Observable<Long> call() {
                    return Observable.just(System.currentTimeMillis());
                }
            });
        deferObservable.subscribe(new Action1<Long>() { //当有Subscriber来订阅的时候才会创建一个新的Observable对象，可以确保Observable对象里的数据是最新的
            @Override
            public void call(Long data) {
                System.out.println(data);
            }
        });
/**
 12-05 18:25:08.837 26001-26001/com.rxandroid.demo I/System.out: 1480933508828
 12-05 18:25:09.600 26001-26001/com.rxandroid.demo I/System.out: 1480933509600
 12-05 18:25:10.310 26001-26001/com.rxandroid.demo I/System.out: 1480933510310
 12-05 18:25:10.479 26001-26001/com.rxandroid.demo I/System.out: 1480933510479
 */
    }

    /**
     * interval()
     * Interval所创建的Observable对象会从0开始，每隔固定的时间发射一个数字。需要注意的是这个对象是运行在computation Scheduler,所以如果需要在view中显示结果，要在主线程中订阅。
     */
    public void interval() {
        //延迟1秒开始，每隔1秒发射一个数字，数字从0开始
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + aLong);
                    }
                });
/**
 12-05 18:42:47.442 15835-15902/com.rxandroid.demo I/System.out: RxComputationScheduler-1: 0
 12-05 18:42:49.439 15835-15902/com.rxandroid.demo I/System.out: RxComputationScheduler-1: 1
 12-05 18:42:51.438 15835-15902/com.rxandroid.demo I/System.out: RxComputationScheduler-1: 2
 12-05 18:42:53.439 15835-15902/com.rxandroid.demo I/System.out: RxComputationScheduler-1: 3
 */
    }

    /**
     * Repeat会将一个Observable对象重复发射，我们可以指定其发射的次数
     * repeat(): 无限重复发射
     * repeat(count): 重复发射count次
     * <p>
     * repeat 和retry的比较：
     * 当.repeat()接收到.onCompleted()事件后触发重订阅。
     * 当.retry()接收到.onError()事件后触发重订阅。
     */
    public void repeat() {
        //1、无限重复发射
//    Observable.just(1)
//            .repeat()
//            .subscribe(new Action1<Integer>() {
//                @Override
//                public void call(Integer integer) {
//                    System.out.println(integer);
//                }
//            });

        //2. 发射3次
        Observable.just(1)
                .repeat(3, Schedulers.io()) //在io线程中重复发射3次
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ",onNext: " + integer);
                    }
                });
        /**
         12-06 10:56:25.810 30251-30515/com.rxandroid.demo I/System.out: RxIoScheduler-2,onNext: 1
         12-06 10:56:25.814 30251-30515/com.rxandroid.demo I/System.out: RxIoScheduler-2,onNext: 1
         12-06 10:56:25.814 30251-30515/com.rxandroid.demo I/System.out: RxIoScheduler-2,onNext: 1
         12-06 10:56:25.814 30251-30515/com.rxandroid.demo I/System.out: onCompleted
         */

        //3、repeatWhen
    }

    /**
     * 使用.repeatWhen() + .delay()定期轮询数据
     */
    public void repeatWhen() {
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("call-----");
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        })
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> completed) {
                        System.out.println("repeatWhen....completed=" + completed);
                        /**
                         直到notificationHandler发送onNext()才会重订阅到source。因为在发送onNext()之前delay了一段时间，
                         所以优雅的实现了延迟重订阅，从而避免了不间断的数据轮询。
                         */
                        return completed.delay(3, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext....i=" + integer);
                    }
                });
    }

}
