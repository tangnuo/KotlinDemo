package com.example.caowj.kotlintest.java;

import com.example.caowj.kotlintest.activity.FunctionListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * package: com.example.caowj.kotlintest
 * author: Administrator
 * date: 2017/9/21 14:34
 */
public class Test1 {

    private List<Class> ss = new ArrayList<>();

    public static void main(String[] args) {

        System.out.print("hello Test1");
    }


    private void set() {
        ss.add(FunctionListActivity.class);
    }
}
