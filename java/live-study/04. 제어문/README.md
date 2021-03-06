# 제어문

제어문은 조건에 따라 다르게 실행할 수 있는 분기문(조건문)과 코드를 반복적으로 실행하게 하는 반복문이 있습니다.

## 분기문(조건문)

분기문은 크게 `if` 문, `switch` 문 두 가지로 나뉩니다. 전자는 boolean 값에 따라서만 분기가 되고 후자는 boolean 값이 아니어도 됩니다.

### if문

if문은 `if` 문, `if - else` 문, `if - else if - else` 문 이렇게 세가지로 다시 나뉩니다.

#### if 문

`if`문은 `boolean`값에 따라 흐름을 다르게 하거나 특정 조건에만 추가적인 흐름을 만들 수 있습니다. 형태는 아래와 같습니다.

~~~
if (조건식) {

    조건식이 참일 때 실행

}
~~~

`if` 문 괄호 안에 있는 조건식이 `true`를 반환하면 볼록 안의 내용을 수행하고 `false`면 건너뛰게 됩니다. 예를 들어 회원 등급이 VIP, 일반으로 나뉘어 있다고 하면 VIP만 누릴 수 있는 혜택을 따로 추가해 주는 추가 로직을 작성할 수 있습니다.

~~~java
if (member.getGrade.equal("VIP")) {
    member.setAuthorization("VIP");
}

//...
if (member.getAuthorization().equals("VIP")) {
    //VIP 혜택에 대한 로직
}
~~~

`member.addAuthorization` 이라는 세터 메서드는 등급이 VIP일 때만 실행되기 때문에 VIP가 아닌 회원은 VIP혜택을 사용할 수 없게 됩니다.

#### if else 문

~~~
if (조건식) {

    조건식이 참일 때 실행

} else {

    조건식이 거짓일 때 실행

}
~~~

`if else`는 조건이 참이면 if 블록 안의 내용을 실행하고 거짓이면 else 블록 안의 내용을 실행합니다. 예제를 살펴봅시다.

~~~java
if (member.getGrade.equal("VIP")) {
    member.addAuthorization("일반");
    member.addAuthorization("VIP");
} else {
    member.addAuthorization("일반");
}
~~~

회원의 등급이 VIP 회원이면 조건식이 `true`이기 때문에 일반 회원과 VIP 회원의 권한 모두를 사용할 수 있고 VIP가 아니라면 일반 회원의 권한만 사용할 수 있습니다.

#### if - else if - else 문

~~~
if (조건식1) {

    조건식1이 참일 때 실행

} else if (조건식2) {
    
    조건식2가 참일 때 실행

} else {

    조건식1, 조건식2가 모두 거짓일 때 실행

}
~~~

`if - else if - else` 문은 조건에서 공통의 범위를 체크할 때 많이 사용합니다. 이 때, `else` 문은 필수가 아닙니다. 예를 들어 상품의 금액 범위마다 포인트를 지급하는 것이 달라질 때 사용할 수 있습니다.

~~~java
double point;
if (item.getPrice() > 10000) {
    point = item.getPrice() * 0.5;
} else if (item.getPrice() > 5000) {
    point = item.getPrice() * 0.3;
} else {
    point = item.getPrice() * 0.1;
}
~~~

그렇다면 `if - else if - else` 문을 하나 쓰는 것과 `if` 문 두 개를 사용하는 것의 차이는 무엇일까요?

~~~
if (item.getPrice > 10000) {
    point = item.getPrice * 0.5;
}
if (item.getPrice > 5000) {
    point = item.getPrice * 0.3;
}
~~~

이렇게 된다면 금액이 10000 이상이면 두 가지 조건이 모두 만족하기 때문에 첫 번째 `if` 문, 두 번째 `if` 문을 모두 통과해 두 번째의 조건의 계산 방식으로 포인트를 적립받게 되는 현상이 발생하게 됩니다. 따라서 같은 값의 범위에 따라 분기 할 때는 `if - else - else` 문을 써야 합니다.

### switch 문

`switch` 문은 `if - else if - else` 문과 거의 비슷합니다. 즉 여러개의 조건을 나누는 분기문입니다. 차이점이라면 `if - else if - else`문은 조건의 결과가 반드시 `boolean` 타입이어야 하지만 `switch`문은 반대로 `char`, `byte`, `short`, `int`, `String`, 또는 `enum` 타입을 사용할 수 있습니다. 형태는 아래와 같습니다.

~~~
switch(표현식) {
    case "값1": 
        값1 일 때 실행
        break;
    case "값2": 
        값2 일 때 실행
        break;
    case "값3": 
        값3 일 때 실행
        break;
    default:
        값 1,2,3이 아닐 때 실행
}
~~~

실행줄 밑에 `break`라는 키워드가 보입니다. `break`는 해당 줄까지만 실행하고 `switch`문을 빠져나올 수 있게 합니다. 따라서 다음과 같이 사용하면 잘못된 결과가 나올 수 있습니다.

~~~java
switch(value) {
    case "a":
        System.out.println("a");
    case "b":
        System.out.println("b");
    case "b":
        System.out.println("c");
    default
        System.out.println("default");
}
~~~

실행결과

~~~
a
b
c
default
~~~

`switch` 문의 마지막에는 `default`가 있는데요. 이 것은 `if` 문에서 `else`와 같은 역할을 합니다. 즉, 정의해 놓은 case 이외의 조건에 대한 로직을 작성해야 합니다.

자바 13부터 `switch` 문 이외에 `switch` 연산자가 추가 되었습니다. 이에 대한 설명은 [두 번째 글](https://github.com/hypernova1/TIL/tree/master/java/live-study/3.%20%EC%97%B0%EC%82%B0%EC%9E%90)에 정리해 놓았습니다.

## 반복문

반복문은 같은 로직을 반복적으로 수행할 수 있게 하는 제어문입니다. 조건에 따라 반복하는 `while` 문과 정해진 횟수만큼 반복하는 `for` 문이 있습니다.

### while 문

`while` 문은 조건식이 참이 아닐 때 까지 반복합니다. `break` 키워드를 사용하여 즉시 블록에서 빠져 나오거나, `continue`키워드를 사용하여 해당 라인까지만 실행 후 다음 반복을 수행할 수 있습니다. 주의해야 할 점은 일정 횟수만큼 반복하는 것이 아니기 때문에 마지막에 조건식의 결과를 거짓으로 만들어 빠져나오게끔 해야 합니다. 그게 아니라면 블록을 빠져나오지 못하고 무한 루프에 빠지게 됩니다. 형태는 다음과 같습니다.

~~~
while (조건식1) {
    
    실행 로직

    if (조건식2) {
        continue; //continue를 만나면 반복문의 처음으로 돌아간다.
    }
    
    if (조건식3) {
        break; //break를 만나면 블록을 빠져나온다.
    }

}
~~~

다음의 예제와 같이 조건식이 항상 참이 되게 되면 무한루프에 빠지게 됩니다.

~~~java
boolean flag = true;

while (flag) {
    System.out.println("hello");
}
~~~

실행결과

~~~
hello
hello
hello
hello
hello
hello
hello
//이하 반복
~~~

따라서 특정 조건이 충족되면 반복문을 빠져나오게 만들어야 합니다.

~~~java
boolean flag = true;

while (flag) {
    if (조건식1) {
        flag = false;
    }
    
    if (조건식2) {
        break;
    }
}
~~~

첫 번째 조건식이 만족하면 flag를 `false`로 만들기 때문에 한 번 반복후 다음 반복에 블록을 빠져나오게 됩니다. 이렇게 조건을 거짓으로 만들어서 빠져나오게 할 수 있고, 조건식2의 경우처럼 `break`키워드를 사용하여 즉시 블록을 빠져나오게 할 수도 있습니다. 

### label
break는 위의 예처럼 while 블록 다음 라인으로 넘어가게 할 수 있고 조건문이 중첩된다면 `break` 뒤에 `label`를 사용하여 특정 라인으로 돌아가게 만들 수 있습니다. `label`은 모든 반복문에서 사용이 가능합니다. (`continue`에서도 사용 가능)

~~~java
a: //label 선언
while (flag) {
    //..
    while (flag) {
        break a; //break를 만나면 label의 라인으로 되돌아간다.
    }
}
~~~

### do while 문

`do while`문은 일반 `while`문과 같지만 무조건 한 번은 실행한다는 점이 다릅니다. 즉, 조건이 참이 아니어도 먼저 한 번 블록 내부의 로직을 수행하게 됩니다. 그 이외에는 `while`문과 모두 같습니다.

~~~
do {

    실행 로직

} while (조건식)
~~~
예
~~~java
boolean flag = false;

do {
    System.out.println("hello");
} while (flag);
~~~

실행결과
~~~
hello
~~~

## for 문

`for` 문은 일정 횟수만큼 반복을 합니다. 일반적으로 인덱스를 받아 사용하는 `for` 문과 0번째부터 끝까지 실행하는 `foreach`문이 있습니다.

### for 문

`for` 문은 정의된 인덱스 값 부터 특정 수만큼 반복합니다. 처음부터 반복할 필요는 없습니다.

~~~
for (초기화식; 조건식; 증감식) {
    실행 로직
}
~~~

형태를 살펴보면 `초기화식`, `조건식`, `증감식` 이렇게 총 세가지가 있습니다. 초기화식에서는 블록 내부에서 사용할 인덱스를 정의하고, 조건식에서는 언제까지 반복할지에 대한 조건을 정의합니다. 마지막으로 증감식에는 인덱스 값을 얼마나 증가 혹은 감소시킬지 정의 합니다.

예
~~~java
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}
~~~

인덱스를 0으로 초기화 하였고 인덱스가 10보다 작을 때까지 실행되게 하였습니다. 또한 인덱스는 1씩 증가하게 됩니다. 순서를 정리해보면 아래와 같습니다.

1. 인덱스 초기화
    - i를 0으로 초기화
2. 조건식 평가
    - i가 10보다 작으므로 통과
3. 로직 수행
4. 증감식 수행
    - i를 1 증가 시킴

또한 `while`문과 마찬가지로 `break`, `continue`, `label` 모두 사용 가능합니다.

~~~java
a:
for (int i = 0; i < 10; i++) {
    if (i == 7) {
        continue;
    }

    for (int j = 0; j < 10; j++) {
        if (j == 5) {
            break a;
        }
    }
}
~~~

소괄호 안에 있는 3가지는 모두 생략 가능하며 극단적으로 사용하면 조건을 참으로 고정한 `while`문 처럼 사용도 가능합니다. 하지만 `while`문이 존재하기 때문에 굳이 사용하지 말고 필요에 따라 한 두개씩 생략해서 사용하도록 합시다. 아래의 예제들은 위의 예제와 결과가 같습니다.

~~~java
//초기화식 생략
int i = 0;
for (; i < 10; i++) {
    System.out.println(i);
}

//조건식 생략
for (int i = 0; ; i++) {
    System.out.println(i);
    if (i == 10) {
        break;
    }
}

//증감식 생략
for (int i = 0; i < 10;) {
    System.out.println(i);
    i++;
}

//모두 생략
for (;;) {
    System.out.println(i);
    i++;
    if (i == 10) {
        break;
    }
}

//모두 생략한 for 문과 같다.
while (true) {
    System.out.println(i);
    i++;
    if (i == 10) {
        break;
    }
}
~~~

### foreach 문

`foreach`는 `for`문과 유사하지만 무조건 0번째부터 끝까지 반복합니다. 만약 배열이나 리스트의 요소를 사용한다면 직접 그 요소에 접근하는 것이 아니라 선언한 변수에 임시로 할당되는 것이니 주의합시다. 요소의 값을 바꿔야 한다면 `for`문을 사용해야 합니다.

~~~java
int[] arr = new int[10];

for (int a : arr) {
    a = 1;
}

System.out.println(arr[0]); //0 값이 바뀌지 않음

//내부 요소를 변경하고 싶다면 for 문을 사용
for (int i = 0; i < arr.length; i++) {
    arr[i] = 1;
}

System.out.println(arr[0]); //1
~~~

`foreach` 문도 마찬가지로 `break`, `continue`, `label` 모두 사용 가능합니다.

## 과제 1. 깃헙 대시 보드 만들기

~~~java
package org.java.study.assignment1;

import org.kohsuke.github.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GithubApi {

    public static void main(String[] args) throws IOException {

        GitHub gitHub = new GitHubBuilder().withOAuthToken("토큰").build();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        List<GHIssue> issues = repository.getIssues(GHIssueState.ALL);
        
        Map<String, Integer> users = new HashMap<>();
        for (GHIssue issue : issues) {
            PagedIterable<GHIssueComment> comments = issue.listComments();
            for (GHIssueComment comment : comments) {
                String userName = comment.getUser().getName();
                if (Objects.isNull(userName)) continue;
                users.put(userName, users.getOrDefault(userName, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> user : users.entrySet()) {
            String value = String.format("%.2f", (user.getValue() / (float) 18) * 100);
            System.out.println(user.getKey() + ": " + value + "%");
        }

    }

}
~~~

#### 실행결과

![1](images/실행결과.png)

## 과제 2. LinkedList 구현

### 기존의 배열

기존의 배열은 논리적 순서과 물리적 순서가 같아야 한다. 즉 인덱스 0과 1을 비교해보면 1이 숫자가 더 크기 때문에 주소값도 0번째보다 뒤에 있어야 하고 만약 중간에 값을 삽입해야 한다면 해당 인덱스부터 한칸씩 뒤로 밀어내고 빈 자리에 값을 삽입해야한다. 

### LinkedList

LinkedList는 배열과는 다르게 각 노드의 물리적 위치와 논리적 위치가 일치하지 않다. 배열을 이용하여 만들지 않고 노드의 다음 노드를 지정해서 하나하나 다음 노드를 탐색해서 값을 찾는 방식이기 때문에 물리적인 위치가 유동적일 수 있는 것이다. 만약 중간에 값을 삽입해야 한다면 연결된 노드를 끊고 그 사이에 노드를 삽입후 앞 뒤로 포인터를 연결해주면 된다.

#### ListNode 구현

~~~java
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
~~~

#### 테스트 코드

~~~java
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
~~~

위의 구현 방식은 head를 기준으로 값을 삽입하거나 삭제하기 때문에 0번 째에 노드를 삽입하거나 삭제할 수 없다. 자기 자신을 삭제할 방법이 없기 때문이다. 따라서 다음의 구현 예제와 `Node` 클래스를 따로 내부에 선언하고 `Node` 인스턴스를 사용하여 head를 만들어 사용하면 된다. 이 경우 0번째 노드는 무조건 head이기 때문에 0번 째를 삭제하려면 head의 다음 노드를 head로 지정해 주면 된다.

#### Node를 사용하여 구현한 LinkedList

~~~java
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
~~~

#### 테스트 코드

~~~java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    @Test
    void test() {
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
~~~

## 과제 3. Stack 구현

~~~java
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
~~~

#### 테스트 코드

~~~java
import org.java.study.exception.StackEmptyException;
import org.java.study.exception.StackFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

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
~~~

## 과제 4. LinkedList를 사용한 Stack 구현

~~~java
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
~~~

#### 테스트 코드

~~~java
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

~~~

## 과제 5. Queue 구현

~~~java
public class Queue {

    private final int[] data;
    private final int capacity;
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
~~~

#### 테스트 코드

~~~java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @Test
    void test() {
        Queue queue = new Queue(3);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(3, queue.size());
        assertTrue(queue.isFull());
        assertThrows(IndexOutOfBoundsException.class, () -> queue.enqueue(4));

        assertEquals(1, queue.peek());
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertTrue(queue.isEmpty());

        queue.enqueue(1);
        assertEquals(1, queue.size());
    }

}
~~~
