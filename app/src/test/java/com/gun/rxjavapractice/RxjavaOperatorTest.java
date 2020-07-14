package com.gun.rxjavapractice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;

@RunWith(JUnit4.class)
public class RxjavaOperatorTest {

    @Test
    public void rangeTest() {
        Observable source = Observable.range(1, 10)
                .filter(data -> data % 2 == 0);
        source.subscribe(System.out::println);
    }

    @Test
    public void intervalRangeTest() throws InterruptedException {
        Observable<Long> source = Observable.intervalRange(1,
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
                Observable.fromArray(shapes).map(Shape::getSuffix),
                Observable.fromArray(coloredTriangles).map(Shape::getColor),
                (suffix, color)-> color + suffix);
        source.subscribe(System.out::println);
    }

    static class Shape{
        public static final String HEXAGON = "HEXAGON";
        public static final String OCTAGON = "OCTAGON";
        public static final String RECTANGLE = "RECTANGLE";
        public static final String TRIANGLE = "TRIANGLE";
        public static final String DIAMOND = "DIAMOND";
        public static final String PENTAGON = "PENTAGON";
        public static final String BALL = "BALL";
        public static final String STAR = "STAR";
        public static final String NO_SHAPE = "NO_SHAPE";
        public static final String FLIPPED = "(flipped)";

        //Colors for shape
        public static final String RED = "1";
        public static final String YELLOW = "2";
        public static final String GREEN = "3";
        public static final String SKY = "4";
        public static final String BLUE = "5";
        public static final String PUPPLE = "6";
        public static final String ORANGE = "7";

        public static String getColor(String shape) {
            if (shape.endsWith("<>")) //diamond
                return shape.replace("<>", "").trim();

            int hyphen = shape.indexOf("-");
            if (hyphen > 0) {
                return shape.substring(0, hyphen);
            }

            return shape; //for ball
        }

        public static String getSuffix(String shape) {
            if (HEXAGON.equals(shape)) return "-H";
            if (OCTAGON.equals(shape)) return "-O";
            if (RECTANGLE.equals(shape)) return "-R";
            if (TRIANGLE.equals(shape)) return "-T";
            if (DIAMOND.equals(shape)) return "<>";
            if (PENTAGON.equals(shape)) return "-P";
            if (STAR.equals(shape)) return "-S";
            return ""; //for BALL
        }

        public static String getShape(String obj) {
            if (obj == null || obj.equals("")) return NO_SHAPE;
            if (obj.endsWith("-H")) return HEXAGON;
            if (obj.endsWith("-O")) return OCTAGON;
            if (obj.endsWith("-R")) return RECTANGLE;
            if (obj.endsWith("-T")) return TRIANGLE;
            if (obj.endsWith("<>")) return DIAMOND;
            if (obj.endsWith("-P")) return PENTAGON;
            if (obj.endsWith("-S")) return STAR;
            return "BALL";
        }

        public static String getString(String color, String shape) {
            return color + getSuffix(shape);
        }

        public static String flip(String item) throws ShapeCannotFlipException {
            if(item.startsWith(FLIPPED)) {
                return item.replace(FLIPPED, "");
            }

            String shape = getShape(item);
            switch(shape) {
                case BALL:
                case RECTANGLE:
                case DIAMOND:
                case NO_SHAPE:
                    throw new ShapeCannotFlipException();
                    //return "throw new ShapeCannotFlipException()";
            };

            return FLIPPED + item;
        }

        public static String triangle(String color) {
            return color + "-T";
        }

        public static String rectangle(String color) {
            return color + "-R";
        }

        public static String star(String color) {
            return color + "-S";
        }

        public static String pentagon(String color) {
            return color + "-P";
        }
    }
    public static class ShapeCannotFlipException extends Exception {
        //do nothing
    }
}
