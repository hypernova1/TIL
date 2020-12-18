package org.java.study.assignment2;

import org.java.study.exception.NoSuchElementException;

public class LinkedList {

    private Node head;
    private int size;

    public void add(int value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            this.head = newNode;
            size++;
            return;
        }
        Node cur = this.head;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = newNode;
        size++;
    }

    public int get(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node cur = this.head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.value;
    }

    public boolean contains(int value) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node cur = this.head;
        while (cur != null) {
            if (cur.value == value) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public void remove(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (index == 0) {
            head = head.next;
            size--;
            return;
        }

        Node prev = this.head;
        Node cur = prev.next;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
            cur = cur.next;
        }
        prev.next = cur.next;
        size--;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    private static class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
        }
    }

}
