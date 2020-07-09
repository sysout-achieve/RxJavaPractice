package com.gun.rxjavapractice;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.reactivex.rxjava3.core.Observable;

@RunWith(JUnit4.class)
public class RxJavaTest {


    @Test
    public void emit(){
        Observable.just("Hello", "RxJava 3!!").subscribe(System.out::println);
    }
}
