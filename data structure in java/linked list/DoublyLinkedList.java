public class DoublyLinkedList<T> implements LinkedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    @Override
    public void add(T t) {
        Node<T> node = new Node<>(t);
        if (isEmpty()) {
            this.head = node;
            this.tail = node;
            size++;
            return;
        }
        Node<T> currentTail = this.tail;
        this.tail = node;
        currentTail.next = node;
        node.prev = currentTail;
        size++;
    }

    @Override
    public void add(int index, T t) {
        if (isEmpty() && index != 0 || index > size)
            throw new IndexOutOfBoundsException("삽입할 수 없습니다. 배열 길이: " + size + ", 삽입 인덱스:" + index);

        Node<T> node = getNode(index);
        Node<T> newNode = new Node<>(t);
        if (index == 0) {
            this.head.prev = newNode;
            newNode.next = this.head;
            this.head = newNode;
        } else if (index == size){
            this.tail.next = newNode;
            newNode.prev = this.tail;
            this.tail = newNode;
        } else {
            newNode.prev = node.prev;
            newNode.next = node;
            node.prev.next = newNode;
            node.prev = newNode;
        }
        size++;
    }

    @Override
    public void delete(int index) {
        if (index > size - 1 || isEmpty())
            throw new IndexOutOfBoundsException("리스트의 길이보다 인덱스가 큽니다.");

        Node<T> node = getNode(index);
        if (this.head == node) {
            this.head = this.head.next;
        } else if (this.tail == node) {
            this.tail = this.tail.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;
    }

    @Override
    public T get(int index) {
        if (index > size - 1 || isEmpty())
            throw new IndexOutOfBoundsException("리스트의 길이보다 인덱스가 큽니다.");
        Node<T> node = getNode(index);
        return node.item;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        Node<T> temp = head;
        StringBuilder str = new StringBuilder("[");
        while (temp.next != null) {
            str.append(temp.item.toString());
            str.append(", ");
            temp = temp.next;
        }
        str.append(temp.item.toString());
        str.append("]");
        return str.toString();
    }

    private Node<T> getNode(int index) {
        Node<T> node;
        if (index > (size - 1) / 2) {
            node = getNodeFromTail(index);
        } else {
            node = getNodeFromHead(index);
        }
        return node;
    }

    private Node<T> getNodeFromHead(int index) {
        Node<T> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    private Node<T> getNodeFromTail(int index) {
        Node<T> node = tail;
        for (int i = size - 1; i > index; i--) {
            node = node.prev;
        }
        return node;
    }

    static class Node<T> {
        private T item;
        private Node<T> prev;
        private Node<T> next;
        public Node(T t) {
            this.item = t;
        }
    }
}
