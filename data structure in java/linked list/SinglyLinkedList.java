public class SinglyLinkedList<T> implements LinkedList<T> {

    private Node<T> head;
    private int size;

    //삽입 함수
    @Override
    public void add(T t) {
        //새로운 노드 생성
        Node<T> node = new Node<>(t);
        //연결 리스트가 비어있으면 노드를 헤드에 할당
        if (isEmpty()) {
            head = node;
            size++;
            return;
        }
        Node<T> temp = head;
        //마지막 노드까지 이동
        while (temp.next != null) {
            temp = temp.next;
        }
        //마지막 노드의 다음 요소에 새로운 노드 할당
        temp.next = node;
        size++;
    }

    //중간 삽입 함수
    @Override
    public void add(int index, T t) {
        //새로운 노드 생성
        Node<T> node = new Node<>(t);
        //연결 리스트가 비어있으면 헤드에 새로운 노드 할당
        if (isEmpty()) {
            head = node;
            size++;
            return;
        }
        //0번째 인덱스라면 head를 새로운 요소의 다음 요소로 지정하고 헤드에 새로운 노드를 할당
        if (index == 0) {
            node.next = head;
            head = node;
            return;
        }
        Node<T> temp = head;
        //삽입할 인덱스 - 1 번째 까지 이동
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        //새로운 노드의 다음 요소에 원래 index에 있던 요소의 다음 요소를 할당
        node.next = temp.next;
        //기존 요소의 다음 요소에 새로운 요소 할당
        temp.next = node;
    }

    //요소 가져오기
    @Override
    public T get(int index) {
        Node<T> temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    //요소 삭제
    @Override
    public void delete(int index) {
        //0번째 요소를 삭제한다면 head의 다음 요소를 head로 할당
        if (index == 0) {
            head = head.next;
            size--;
            return;
        }
        Node<T> temp = head;
        //삭제할 요소의 이전 요소까지 이동
        for (int i = 0; i < index - 1; i++) {
            temp = temp.next;
        }
        //삭제할 요소의 이전요소에 삭제할 요소의 다음 요소를 할당
        //삭제할 요소의 이전 요소 -> 삭제할 요소 -> 삭제할 요소의 다음 요소
        //=>   삭제할 요소의 이전요소 -> 삭제할 요소의 다음요소
        temp.next = temp.next.next;
        size--;
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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    //노드 클래스 선언
    static class Node<T> {
        private T item;
        private Node<T> next;
        public Node(T item) {
            this.item = item;
        }
    }
}
