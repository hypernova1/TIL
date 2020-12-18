package org.java.study.assignment2;

public class ListNode {

    private final int value;
    private ListNode next;

    public ListNode(int value) {
        this.value = value;
    }

    public void add(ListNode head, ListNode nodeToAdd) {
        ListNode cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = nodeToAdd;
    }

    public void add(ListNode head, ListNode nodeToAdd, int position) {
        if (position == 0) {
            throw new RuntimeException("head 노드를 변경할 수 없습니다.");
        }
        if (size(head) < position) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (size(head) == position) {
            this.add(head, nodeToAdd);
            return;
        }

        ListNode cur = head;
        for (int i = 0; i < position - 1; i++) {
            cur = cur.next;
        }
        nodeToAdd.next = cur.next;
        cur.next = nodeToAdd;
    }

    public void remove(ListNode head, int position) {
        if (position == 0) {
            throw new RuntimeException("head 노드는 삭제할 수 없습니다.");
        }
        if (size(head) <= position) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (size(head) - 1 == position) {
            removeLastNode(head);
            return;
        }

        ListNode prev = head;
        for (int i = 0; i < position - 1; i++) {
            prev = prev.next;
        }

        ListNode cur = prev.next;
        prev.next = cur.next;

    }

    private void removeLastNode(ListNode head) {
        ListNode cur = head;

        while (cur.next.next != null) {
            cur = cur.next;
        }
        cur.next = null;
    }

    public boolean contains(ListNode head, ListNode nodeToCheck) {
        ListNode cur = head;
        while (cur != null) {
            if (cur.value == nodeToCheck.value) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    public int size(ListNode head) {
        int size = 0;
        ListNode cur = head;
        while (cur != null) {
            size++;
            cur = cur.next;
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        ListNode cur = this;
        while (cur.next != null) {
            sb.append(cur.value).append(",");
            cur = cur.next;
        }
        sb.append(cur.value).append("]");

        return sb.toString();
    }

}
