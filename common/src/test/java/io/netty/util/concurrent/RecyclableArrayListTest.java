package io.netty.util.concurrent;

import io.netty.util.internal.RecyclableArrayList;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * RecyclableArrayList 测试类
 * Created by zhisheng_tian on 2017/12/12.
 */
public class RecyclableArrayListTest {

    RecyclableArrayList recyclableArrayList = RecyclableArrayList.newInstance();


    public List list1 = Arrays.asList("A", "B", "C", "D");


    @Test
    public void addAllTest() {
        boolean addAll = recyclableArrayList.addAll(list1);
        assertEquals(true, addAll);
    }

    @Test
    public void addTest() {
        boolean add = recyclableArrayList.add("a");
        assertEquals(true, add);
    }

    @Test
    public void insertSinceRecycledTest() {
        assertEquals(false, recyclableArrayList.insertSinceRecycled());
//        recyclableArrayList.addAll(list1);
        recyclableArrayList.add("a");
        assertEquals(true, recyclableArrayList.insertSinceRecycled());
    }

    @Test
    public void recycleTest() {
        assertEquals(false, recyclableArrayList.insertSinceRecycled());
        recyclableArrayList.add("a");
        assertEquals(true, recyclableArrayList.insertSinceRecycled());

        assertEquals(true, recyclableArrayList.recycle());  //清除和回收这个实例

        assertEquals(false, recyclableArrayList.insertSinceRecycled());
    }

}
