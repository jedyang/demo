package com.yunsheng.mix.stream;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * @作者 UncleY
 * @时间 2020/6/19
 * @描述
 */
public class StreamDemoTest {

    @Getter
    @Setter
    class User {
        private String name;
        private int age;
    }

    @Test
    public void distinct() {
        Stream<Integer> integerStream = Stream.of(2, 5, 100, 5);
        List<Integer> collect = integerStream.distinct().collect(Collectors.toList());
        System.out.println(JSONUtil.toJsonStr(collect));

        User a = new User();
        a.setName("yun");
        a.setAge(20);

        User b = new User();
        b.setName("yun");
        b.setAge(20);

        Stream<User> userStream = Stream.of(a, b);
        List<User> userList = userStream.distinct().collect(Collectors.toList());
        System.out.println(JSONUtil.parse(userList));


    }

    @Test
    public void sorted() {
        Stream<String> strStream = Stream.of("ba", "bb", "aa", "ab");
//        strStream.sorted().forEach(item -> System.out.println(item));

        Comparator<String> comparator = (x, y) -> x.substring(1).compareTo(y.substring(1));
        strStream.sorted(comparator).forEach(item -> System.out.println(item));


    }

    @Test
    public void filter() {
        Stream<Integer> integerStream = Stream.of(2, 5, 100, 5);
        integerStream.filter(item -> item > 10).forEach(item -> System.out.println(item));
    }

    @Test
    public void toArray() {
        Stream<Integer> integerStream = Stream.of(2, 5, 100, 5);
        Object[] objects = integerStream.toArray();
        Integer[] toArray = integerStream.toArray(Integer[]::new);
    }

    @Test
    public void reduce() {
        Stream<Integer> integerStream = Stream.of(1,2,3);
        Integer sum = integerStream.reduce(100, (x, y) -> x + y);
        System.out.println(sum);
    }

    @Test
    public void parallel() {
        Stream<Integer> integerStream = Stream.of(1,2,3).parallel();
//        List<Integer> src = Arrays.asList(1, 2, 3);
//        Stream<Integer> integerStream = src.parallelStream();

        Integer sum = integerStream.reduce(100, (x, y) -> x + y);
        System.out.println(sum);
    }
}