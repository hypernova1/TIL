package org.java.study.assignment2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListNodeTest {

    @Test
    void test() {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(4);

        head.add(head, node1, 1);
        head.add(head, node2, 2);
        head.add(head, node3, 2);

        assertTrue(head.contains(head, node1));
        assertTrue(head.contains(head, node2));
        assertTrue(head.contains(head, node3));

        assertEquals(4, head.size(head));

        head.remove(head, 2);

        assertEquals(3, head.size(head));
    }

}