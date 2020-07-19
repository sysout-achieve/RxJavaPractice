package com.gun.rxjavapractice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RxUnitTest {
    ReactiveSum reactiveSum;

    @Before
    public void setUp() throws Exception {
        reactiveSum  = new ReactiveSum();
    }

    @Test
    public void checkReactiveSumTest() throws InterruptedException {
        //안드로이드 콘솔창 입력 불가 -> editText로 input 받아야 함
        reactiveSum.run();
    }
}
