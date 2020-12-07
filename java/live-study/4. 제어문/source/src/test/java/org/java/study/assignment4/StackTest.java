package org.java.study.assignment4;

import org.java.study.exception.StackEmptyException;
import org.java.study.exception.StackFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StackTest {

    @Test
    void test() {
        Stack stack = new Stack(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertThrows(StackFullException.class, () -> stack.push(4));

        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertThrows(StackEmptyException.class, stack::pop);
    }

}
