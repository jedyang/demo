package com.example.demo.stream;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: yunsheng
 * @createDate: 2020/6/17
 * @version: 1.0
 */
public class StreamDemo {
    public static void main(String[] args) {
        Stream<String> s1 = Stream.of("a", "b");
        Stream<String> s2 = Stream.of("c", "d");
        Stream<String> s3 = Stream.concat(s1, s2);

        Stream<Integer> s4 = Stream.iterate(1, n -> n + 2);
        s4.limit(4).forEach(System.out::println);

        Stream<Double> s5 = Stream.generate(Math::random);
        s5.limit(4).forEach(System.out::println);

        Stream<Integer> integerStream = Stream.of(2, 5, 100, 5);
        Integer max = integerStream.max(Integer::compareTo).get();
        System.out.println(max);

        // 流在结束后，要重新生成
        integerStream = Stream.of(2, 5, 100, 5);
        Integer i = integerStream.findFirst().get();
        System.out.println(i);

        // 流在结束后，要重新生成
        integerStream = Stream.of(2, 5, 100, 5);
        Integer i2 = integerStream.findAny().get();
        System.out.println(i2);

        integerStream = Stream.of(2, 5, 100, 5);
        long count = integerStream.count();
        System.out.println(count);

        System.out.println("=======peek======");
        integerStream = Stream.of(2, 5, 100, 5);
        List<Integer> collect = integerStream.limit(2).peek(n -> {
            System.out.println(n);
        }).collect(Collectors.toList());

        System.out.println("=======map======");
        integerStream = Stream.of(2, 5, 100, 5);
        List<String> stringList = integerStream.map(n -> "我是" + n).collect(Collectors.toList());

        System.out.println("=======mapToDouble======");
        integerStream = Stream.of(2, 5, 100, 5);
        double sum = integerStream.mapToDouble(n -> n).sum();
        System.out.println(sum);

        integerStream = Stream.of(2, 5, 100, 5);
        ArrayList<Double> doubles = integerStream.mapToDouble(n -> n + 0.1).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(JSONUtil.toJsonStr(doubles));
    }


}
