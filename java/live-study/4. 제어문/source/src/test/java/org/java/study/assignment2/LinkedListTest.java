package org.java.study.assignment2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    @Test
    void add_test() {
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertTrue(list.contains(3));
        assertEquals(3, list.get(2));
        assertEquals(3, list.size());
        list.remove(2);
        list.remove(1);
        list.remove(0);
        assertEquals(0, list.size());

    }

}