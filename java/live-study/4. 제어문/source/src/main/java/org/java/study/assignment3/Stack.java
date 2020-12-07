package org.java.study.assignment3;

import org.java.study.exception.StackEmptyException;
import org.java.study.exception.StackFullException;

public class Stack {

    private final int[] values;
    private int top;

    public Stack(int size) {
        this.values = new int[size];
        this.top = -1;
    }

    public void push(int value) {
        if (isFull()) {
            throw new StackFullException();
        }
        this.values[++this.top] = value;
    }

    public int pop() {
        if (isEmpty()) {
            throw new StackEmptyException();
        }
        return values[this.top--];
    }

    public boolean isEmpty() {
        return this.top == -1;
    }

    public boolean isFull() {
        return size() == this.values.length;
    }

    public int size() {
        return top + 1;
    }

}
