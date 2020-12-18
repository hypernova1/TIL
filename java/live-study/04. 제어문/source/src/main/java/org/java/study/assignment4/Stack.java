package org.java.study.assignment4;

import org.java.study.assignment2.LinkedList;
import org.java.study.exception.StackEmptyException;
import org.java.study.exception.StackFullException;

public class Stack {

    private int top;
    private final LinkedList list;
    private final int size;

    public Stack(int size) {
        this.list = new LinkedList();
        this.size = size;
        this.top = -1;
    }

    public void push(int value) {
        if (isFull()) {
            throw new StackFullException();
        }

        list.add(value);
        top++;
    }

    public int pop() {
        if (isEmpty()) {
            throw new StackEmptyException();
        }
        int result = list.get(top);
        list.remove(top--);
        return result;
    }

    private boolean isEmpty() {
        return top == -1;
    }

    private boolean isFull() {
        return this.size == top + 1;
    }

}
