package com.yunsheng.base.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /**
         * 泛型中pecs的理解
         * Producer Extends Consumer Super
         * 使用extends确定上界的只能是生产者，只能往外生产东西，取出的就是上界类型。不能往里塞东西。
         * 使用Super确定下界的只能做消费者，只能往里塞东西。取出的因为无法确定类型只能转成Object类型
         */
//        List<? extends Number> numbers = new ArrayList<Integer>();
        List<? extends Number> numbers = Arrays.asList(1,2,3);
//        numbers.add(1);  //报错
        Number number = numbers.get(1);

        List<? super String> strs = new ArrayList<>();
        strs.add("123");
        Object o = strs.get(1);
        // 只能自己手动强转，这当然存在一定的安全风险。
        String s = (String) o;
    }
}
