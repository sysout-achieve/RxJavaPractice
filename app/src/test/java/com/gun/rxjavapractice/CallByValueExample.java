package com.gun.rxjavapractice;

import org.junit.Before;
import org.junit.Test;

public class CallByValueExample {
    Order newOrder = new Order(2L);

    @Test
    public void orderTest() {

        Order order1 = new Order(1L);
        order2Test(order1);
//        order1 = newOrder;
//        Order order1 = new Order(1L);
//        Order order2 = new Order(1L);
//        order22.setId(222L);
//        System.out.println("order1: " + order1.hashCode());
//        System.out.println("order2: " + order2.hashCode());
//        System.out.println("order222: " + order11.getId());
//        System.out.println("order333: " + order22.getId());
//        System.out.println("444: " + order11.hashCode());
//        System.out.println("444`: " + order22.hashCode());
//        System.out.println("555`: " + (order22 == order11));
//        System.out.println(order1 == order2);
//        System.out.println(order1.equals(order2));
    }

    void order2Test(Order order){
//        idx = 2;
        order = newOrder;
        order.id = 2L;
    }
}
