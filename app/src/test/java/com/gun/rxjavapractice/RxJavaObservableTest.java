package com.gun.rxjavapractice;

import android.os.health.SystemHealthManager;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.processors.BehaviorProcessor;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

@RunWith(JUnit4.class)
public class RxJavaObservableTest {

    Observable observable;

    Disposable d;

    @Before
    public void setUp() throws Exception {
        observable = Observable.just(1, "RxJava 3!!", 2, 3, 4, 5, 6);
    }

    @Test
    public void emitTest1() {
        Observable.just("Hello", "RxJava 3!!").subscribe(System.out::println);
    }

    @Test
    public void emitTest2() {
        observable.subscribe(System.out::println);
    }

    @Test
    public void emitTest3() {
        Observable<String> source = Observable.just("RED", "GREEN", "YELLOW");
        d = source.subscribe(v -> System.out.println("onNext(): value" + v)
                , err -> System.err.println("onError() : err " + err.getMessage())
                , () -> System.out.println("oncomplete()"));
        System.out.println("isDisposed() : " + d.isDisposed());
    }

    @Test
    public void emitTest4() {
        Observable<Integer> source = Observable.create((ObservableEmitter<Integer> emitter) -> {
            emitter.onNext(100);
            emitter.onNext(200);
            emitter.onNext(300);
            emitter.onComplete();
        });

        ArrayList arrayList = new ArrayList();
        source.subscribe(arrayList::add);
        arrayList.forEach(System.out::println);

        source.subscribe(System.out::println);
        source.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                System.out.println("Result : " + integer);
            }
        });
    }

    @Test
    public void arrTest() {
        Integer[] arr = {100, 200, 300};
        Observable source = Observable.fromArray(arr);
        source.subscribe(System.out::println);

        int[] arr1 = {100, 200, 300};
        Observable source1 = Observable.fromArray(arr1);
        source1.subscribe(System.out::println);

        Observable source2 = Observable.fromArray(toIntegerArray(arr1));
        source2.subscribe(System.out::println);
    }

    private Integer[] toIntegerArray(int[] intArray) {
        return IntStream.of(intArray).boxed().toArray(Integer[]::new);
    }

    @Test
    public void fromIterableTest() {
        List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("William");
        names.add("Bob");
        Observable.fromIterable(names).subscribe(System.out::println);
    }


    @Test
    public void callableTest() {
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "Hello Callable";
        };

        Observable source = Observable.fromCallable(callable);
        source.subscribe(System.out::println);
    }

    @Test
    public void fromFutureTest() {
        Future future = Executors.newSingleThreadExecutor().submit(() -> {
            Thread.sleep(1000);
            return "Hello Future";
        });
    }

    @Test
    public void fromPublisherTest() {
        Publisher publisher = new Publisher() {
            @Override
            public void subscribe(Subscriber s) {
                s.onNext("Hello Publisher");
                s.onComplete();
            }
        };
        Observable source = Observable.fromPublisher(publisher);
        source.subscribe(System.out::println);
    }

    @Test
    public void singleTest() {
        Single source = Single.just("Hello Single");
        source.subscribe(System.out::println);
    }

    @Test
    public void ObservableWithSingleTest() {
        Observable<String> source = Observable.just("Hello single");
        Single.fromObservable(source)
                .subscribe(System.out::println);

        Observable
                .just("Hello Single22")
                .single("default item")
                .subscribe(System.out::println);

        String[] colors = {"Red", "Blue", "Gole"};
        Observable.fromArray(colors).first("default name")
                .subscribe(System.out::println);

        Observable.empty()
                .single("default value")
                .subscribe(System.out::println);

        Observable.just(new String("11"), new String("22"))
                .take(1)
                .single(new String("33"))
                .subscribe(System.out::println);
    }


    @Test
    public void behaviorSubjectTest() {
        //Subject class
        //AsyncSubject, BehaviorSubject, PublishSubject, ReplaySubject...
        BehaviorSubject subject = BehaviorSubject.createDefault(6);
        subject.subscribe(data -> System.out.println("Sub #1 -> " + data));
        subject.onNext("1");
        subject.onNext("3");

        subject.subscribe(data -> System.out.println("Sub #2 -> " + data), System.out::println, () -> System.out.println("onComplete()"));
        subject.onNext("5");
        subject.onComplete();
    }

    @Test
    public void connectableObservableTest() throws InterruptedException {
        String[] dt = {"1", "2", "3"};
        Observable balls = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(i -> dt[i])
                .take(dt.length);
        ConnectableObservable source = balls.publish();

        source.subscribe(data -> System.out.println("Subscriber #1 : " + data));
        source.subscribe(data -> System.out.println("Subscriber #2 : " + data));

        Thread.sleep(250);
        source.connect();
        source.subscribe(data -> System.out.println("Subscriber #3 :" + data));
        Thread.sleep(600);
    }

    @Test
    public void mapTest() {
        String[] balls = {"1", "2", "3", "4", "5"};

        /*      NullPointerException: items is null   */
//        String[] balls = null;
        Observable source = Observable.fromArray(balls)
                .map(ball -> ball + " Ball");
        source.subscribe(System.out::println);
    }

    @Test
    public void mapTest2() {
        Function<String, String> getBalls = ball -> ball + " Ball";
        String[] balls = {"1", "2", "3", "4", "5"};
        Observable source = Observable.<String>fromArray(balls)
                .map(getBalls);
        source.subscribe(System.out::println);
    }

    @Test
    public void flatMapTest() {
        Function<String, Observable<String>> getDoubleBalls = ball -> Observable.just(ball + " Ball", ball + " Ball2");
        String[] balls = {"1", "3", "5"};

        //Function interface
        Observable source = Observable.fromArray(balls)
                .flatMap(getDoubleBalls);

        //lambda
        Observable source2 = Observable.fromArray(balls)
                .flatMap(ball -> Observable.just(ball + " Ball", ball + " Ball2", "  !Hi!"));
        source.subscribe(System.out::println);
        source2.subscribe(data -> System.out.println("source2# : " + data));

    }

    @Test
    public void gugudanExampleTest() {
        Integer[] row = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer inputData = 7;
        Observable source = Observable.fromArray(row)
                .map(multipliers -> textCalculation(multipliers, inputData) + resultCalculation(multipliers, inputData));
        source.subscribe(data -> {
            System.out.println("구구단 " + data);
        });
    }

    @Test
    public void gugudanHardVerTest() {
        // 1~9단까지 구구단 전체 곱셈 만들기
        Integer[] row = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Observable<Integer> source = Observable.fromArray(row)
                .map(data -> data);
        Observable.fromArray(row)
                .map(data -> source.subscribe(multipliers -> printGugudan(multipliers, data)
                        , System.out::println
                        , () -> System.out.println("--- " + data + "단 ---")))
                .subscribe();
    }

    private void printGugudan(Integer multipliers, Integer dan) {
        System.out.print(textCalculation(multipliers, dan));
        System.out.println(resultCalculation(multipliers, dan));
    }

    private String textCalculation(Integer multipliers, Integer dan) {
        return dan + " x " + multipliers + " = ";
    }

    private int resultCalculation(Integer multipliers, Integer dan) {
        return dan * multipliers;
    }
}