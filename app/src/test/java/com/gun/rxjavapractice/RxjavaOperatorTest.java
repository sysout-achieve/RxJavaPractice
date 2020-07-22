package com.gun.rxjavapractice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observables.GroupedObservable;
import static com.gun.rxjavapractice.Shape.*;

@RunWith(JUnit4.class)
public class RxjavaOperatorTest {
    private int index = 0; //FIXME don't use it

    @Test
    public void rangeTest() {
        Observable source = Observable.range(1, 10)
                .filter(data -> data % 2 == 0);
        source.subscribe(System.out::println);
    }

    @Test
    public void intervalRangeTest() throws InterruptedException {
        Observable<Long> source = Observable.intervalRange(
                1,
                5,
                100L,
                100L,
                TimeUnit.MILLISECONDS);

        source.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void heartBeatTest() throws InterruptedException {
        String serverUrl = "https://api.github.com/zen";
        Observable.timer(2, TimeUnit.SECONDS)
                .map(val -> serverUrl)
                .map(OkHttpHelper::get)
                .repeat()
                .subscribe(System.out::println);
        Thread.sleep(10000);
    }

    @Test
    public void concatMapTest() throws InterruptedException {
        //interleaving 불가능
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .concatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "!!!")
                        .take(2));
        source.subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void flatMapV2Test() throws InterruptedException {
        //interleaving 가능
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .flatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "!!!")
                        .take(2));
        source.subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void switchMapTest() throws InterruptedException {
        //interleaving 가능
        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> balls[idx])
                .take(balls.length)
                .doOnNext(System.out::println)
                .switchMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .map(notUsed -> ball + "!!!")
                        .take(2));
        source.subscribe(System.out::println);
        Thread.sleep(5000);
    }

    @Test
    public void groupByTest() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        @NonNull Observable<GroupedObservable<String, String>> source =
                Observable.fromArray(objs).groupBy(RxjavaOperatorTest::getShape);

        source.subscribe(obj -> {
            obj.subscribe(val -> System.out.println("Group : " + obj.getKey() + "\t Value : " + val));
        });
    }

    @Test
    public void groupByExampleTest() {
        String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
        @NonNull Observable<GroupedObservable<String, String>> source =
                Observable.fromArray(objs).groupBy(RxjavaOperatorTest::getShape);

        source.subscribe(obj -> {
            obj.filter(val -> obj.getKey().equals("Ball"))
                    .subscribe(val -> System.out.println("Group : " + obj.getKey() + "\t Value : " + val));
        });
    }

    @Test
    public void scanTest() {
        String[] balls = {"1", "3", "5"};
        Observable source = Observable.fromArray(balls)
                .scan((ball1, ball2) -> ball2 + "(" + ball1 + ")");
        source.subscribe(System.out::println);
    }

    private static String getShape(String obj) {
        if (obj == null || obj.equals("")) return "No-Shape";
        if (obj.endsWith("-H")) return "Hexagon";
        if (obj.endsWith("-O")) return "Octagon";
        if (obj.endsWith("-R")) return "Rectangle";
        if (obj.endsWith("-T")) return "Triangle";
        if (obj.endsWith("-D")) return "Diamond";
        return "Ball";
    }

    @Test
    public void zipTest() {
        String[] shapes = {"BALL", "PENTAGON", "STAR"};
        String[] coloredTriangles = {"2-T", "6-T", "4-T"};
        Observable source = Observable.zip(
                Observable.fromArray(shapes).map(com.gun.rxjavapractice.Shape::getSuffix),
                Observable.fromArray(coloredTriangles).map(com.gun.rxjavapractice.Shape::getColor),
                (suffix, color) -> color + suffix);
        source.subscribe(System.out::println);
    }

    @Test
    public void combineExampleTest() {
        Observable source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                Observable.just(1, 2, 3),
                (a, b, c) -> a + b + c);
        source.subscribe(System.out::println);
    }

    @Test
    public void combineExample2Test() throws InterruptedException {
        Observable source = Observable.zip(
                Observable.just("RED", "GREEN", "BLUE"),
                Observable.interval(200L, TimeUnit.MILLISECONDS),
                (value, i) -> value
        );
        source.subscribe(System.out::println);
        Thread.sleep(1000);
    }

    //부수효과 있음
    @Test
    public void combineElectricBillsTest() {
        String[] data = {
                "100",  //910 + 93.3 * 100 = 10,240원
                "300",  //1600 + 93.3 * 200 + 187.9 * 100 = 39,050원
                "800",  //7300 + 93.3 * 200 + 187.9 * 200 + 280.65 * 200 = 175,800원
        };
        Observable<Integer> basePrice = Observable.fromArray(data)
                .map(Integer::parseInt)
                .map(val -> {
                    if (val <= 200) return 910;
                    if (val <= 400) return 1600;
                    return 7300;
                });

        Observable<Integer> usagePrice = Observable.fromArray(data)
                .map(Integer::parseInt)
                .map(val -> {
                    double series1 = Math.min(200, val) * 93.3;
                    double series2 = Math.min(200, Math.max(val - 400, 0)) * 280.65;
                    double series3 = Math.max(0, Math.max(val - 400, 0)) * 280.65;
                    return (int) (series1 + series2 + series3);
                });

        Observable source = Observable.zip(
                basePrice,
                usagePrice,
                Integer::sum
        );

        source.map(val -> new DecimalFormat("#,###").format(val))
                .subscribe(val -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Usage : " + data[index] + " kWh => ");
                            sb.append("Price : " + val + " 원 ");
                            System.out.println(sb.toString());
                            index++;
                        }
                );
    }

    @Test
    public void zipExampleTest() {
        Observable source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                Integer::sum)
                .zipWith(Observable.just(1, 2, 3), Integer::sum);
        source.subscribe(System.out::println);
    }

    @Test
    public void combineLatestTest() throws InterruptedException {
        String[] data1 = {PUPPLE, ORANGE, SKY, YELLOW}; //6, 7, 4, 2
        String[] data2 = {DIAMOND, STAR, PENTAGON};

        Observable source = Observable.combineLatest(
                Observable.fromArray(data1)
                        .zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS),
                                (shape, __) -> Shape.getColor(shape)),
                Observable.fromArray(data2)
                        .zipWith(Observable.interval(150L, 200L, TimeUnit.MILLISECONDS),
                                (shape, __) -> Shape.getSuffix(shape)),
                (v1, v2) -> v1 + v2);

        source.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void mergeTest() throws InterruptedException {
        String[] data1 = {"1", "3"};
        String[] data2 = {"2", "4", "6"};

        Observable<String> source1 = Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx -> data1[idx])
                .take(data1.length);
        Observable<String> source2 = Observable.interval(50L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx-> data2[idx])
                .take(data2.length);

        Observable<String> source = Observable.merge(source1, source2);
        source.subscribe(System.out::println);
        Thread.sleep(1000);
    }

    @Test
    public void concatTest() throws InterruptedException {
        Action onCompleteAction = ()-> System.out.println("onComplete()");

        String[] data1 = {"1", "3"};
        String[] data2 = {"2", "4", "6"};

        Observable<String> source1 = Observable.fromArray(data1)
                .doOnComplete(onCompleteAction);
        Observable<String> source2 = Observable.interval(50L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(idx-> data2[idx])
                .take(data2.length)
                .doOnComplete(onCompleteAction);

        Observable<String> source = Observable.concat(source1, source2).doOnComplete(onCompleteAction);
        source.subscribe(System.out::println);
        Thread.sleep(1000);
    }

    @Test
    public void ambTest() throws InterruptedException {
        String[] data1 = {"1","3","5"};
        String[] data2 = {"2-R","4-R"};
        List<Observable<String>> source = Arrays.asList(
                Observable.fromArray(data1)
                .doOnComplete(()-> System.out.println("Observable #1 :")),
                Observable.fromArray(data2)
                .doOnComplete(()->System.out.println("Observable #2 :"))
        );
        Observable.amb(source).doOnComplete(()->System.out.println("OnComplete :"))
                .subscribe(System.out::println);
        Thread.sleep(1000);
    }
}