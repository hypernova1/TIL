package org.java.study.assignment5;


import org.java.study.exception.NoSuchElementException;

public class Queue {

    private final int[] data;
    private int capacity;
    private int front;
    private int rear;
    private int count;

    public Queue(int capacity) {
        this.data = new int[capacity];
        this.capacity = capacity;
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    public void enqueue(int value) {
        if (isFull()) {
            throw new IndexOutOfBoundsException();
        }
        this.data[++rear % capacity] = value;
        count++;
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        count--;
        return this.data[front++ % this.capacity];
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.data[front];
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean isFull() {
        return this.size() == this.capacity;
    }

    public int size() {
        return this.count;
    }

}