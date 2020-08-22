package com.gun.rxjavapractice;

import java.util.Scanner;

//@Getter
//@Setter
//@Log
//@Log4j
//@ToString
//@Data
// ㄴ getter, setter, tostring
public class Order {
    public long id;

    public Order() {}

    int num;
    String title;

    public Order(Long id) {
        Scanner scanner = new Scanner(System.in);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id == order.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                '}';
    }
}
