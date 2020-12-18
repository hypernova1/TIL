# finalizer와 cleaner 사용을 피하라

## 사용하지 않는 자원 회수 방법

### C++

C++은 파괴자(desctuctor)를 제공한다. 파괴자는 특정 객체와 관련된 자원을 회수하는 보편적인 방법이다. 또한 비메모리 자원을 회수하는 용도로도 쓰인다.

### 자바

자바는 접근할 수 없게 된 자원을 가비지 컬렉터가 회수하고, 비메모리 자원을 회수하는 역할은 `try-with-resources`와 `try-finally`를 사용해 해결한다.

## 자바의 객체 소멸자

자바는 두 가지 객체 소멸자를 제공하고 있다. `finalizer`와 `cleaner`이다. 하지만 위험성이 있고 예측할 수 없기 때문에 사용을 자제해야한다.

### 문제점

`finalizer`나 `cleaner`는 실행이 되기 까지 얼마나 걸릴지 알 수 없기 때문에, 제 때 실행되어야 하는 작업은 할 수 없다. 예를 들어 파일 닫기를 `finalizer`나 `cleaner`에게 맡긴다면 시스템에서 동시에 열수 있는 파일의 개수가 한정되어 있기 때문에 제 때 닫지 않아 중대한 오류를 발생시킬 수 있다. `finalizer`나 `cleaner`이 실행되는 것은 가비지 컬렉터의 알고리즘에 달려있기 때문에 가비지 컬렉터 구현마다 천차만별이다. 따라서 프로그래머가 사용한 JVM에선 잘 작동하여도 다른 JVM에서는 문제를 일으킬 수 있다.

클래스에 `finalizer`를 달아두게 되면 해당 인스턴스의 자원 회수가 지연될 수 있다. `finalizer`의 스레드가 다른 애플리케이션 스레드보다 우선 순위가 낮아서 실행될 기회를 제대로 얻지 못하기 때문이다. 해결법은 `finalizer`를 사용하지 않는 것 말고는 없다. `cleaner`는 자신을 수행할 스레드를 제어할 수 있다는 점에서 조금 낫지만. 여전히 백그라운드에서 실행되며 가비지 컬렉션이 제어하기 때문에 즉각 수행되지 않을 수 있다.

자바 언어 명세는 `finalizer`나 `cleaner`의 수행 시점뿐 아니라 수행 여부도 보장하지 않는다. 접근할 수 없는 일부 객체에의 종료 작업을 전혀 수행하지 않은 채 프로그램이 중단될 수도 있다는 뜻이다. 따라서 데이터 베이스같은 공유 자원의 영구 락(lock) 해제와 같은 **프로그램 생애주기와 상관 없는 상태를 영구적으로 수정하는 작업에서는 `finalizer`나 `cleaner`에 의존해서는 안된다.**

## System.gc와 System.runFinalization

`System.gc`나 `System.runFinalization` 메서드는 `finalizer`나 `cleaner`의 실행 가능성을 높여주긴 하나 보장하진 않는다. `System.runFinalizerOnExit`와 `Runtime.runFinalizaerOnExit`처럼 실행을 보장해주는 함수가 있긴 하지만 심각한 결함 때문에 수십 년간 지탄 받아왔다.

## 소멸자의 부작용

### 1. 예외를 출력하지 못하고 즉시 종료

`finalizer` 동작 중 발생한 예외는 무시된다. 처리할 작업이 남아있더라도 즉시 종료되기 때문에 잡지 못한 예외로 인해 해당 객체는 마무리가 덜 된 상태로 남을 수 있다. 그리고 다른 스레드가 이러한 객체를 사용하게 되면 어떤 동작을 할지 예측할 수 없다. 보통의 경우에는 잡지 못한 예외가 스레드를 중단 시키고 스텍 추적 내역을 출력하겠지만 `finalizer`에서 일어나게 되면 경고조차 출력하지 않는다. 그나마 `cleaner`를 사용한 라이브러리는 스자신의 스레드에서 통제하기 때문에 이러한 문제는 발생하지 않는다.

### 2. 성능 문제

`finalizer`와 `cleaner`는 심각한 성능 문제를 발생시킨다.  `AutoCloseable`객체를 생성하고 가비지 컬렉터가 수거해가는 데까지 걸리는 시간은 12ns가 걸린 반면 `finalizer`를 사용하면 550n가 걸린다. 이는 `finalizer`가 가비지 컬렉터의 효율을 떨어뜨리기 때문이다. `cleaner`도 클래스의 모든 인스턴스를 수거하는 형태로 사용하면 `finalozer`와 비슷한 속도를 낸다.

> #### AutoCloseable
>
> 파일이나 스레드 등 종료해야 할 자원을 담고 있는 객체를 해제하기 위해서는 `AutoCloseable`을 구현하고 인스턴스를 사용한 후 `close` 메서드를 호출하면 된다. 또한 자신이 닫혔는지 추적하기 위해 `close` 메서드는 객체가 더 이상 유효하지 않음을 필드에 기록하고, 다른 메서드는 이 필드를 검사해 객체가 닫힌 후에 불리면 `IllegalStateException`을 던진다.


### 3. 보안 문제

`finalizer`를 사용한 클래스는 `finalizer` 공격에 노출되어 심각한 보안 문제를 일으킬 수 있다. 생성자나 직렬화 과정에서 예외가 발생하면, 이 생성되다 만 객체에서 악의적인 하위 클래스의 `finalizer`가 수행될 수 있게 된다. 이 `finalizer`는 정적 필드에 자신의 참조를 할당하여 가비지 컬렉터가 수집하지 못하도록 막을 수 있다. 이렇게 객체가 만들어지게 되면 객체의 메서드를 호출해 애초에 허용되지 않았을 작업을 수행하게 할 수 있다. 객체 생성을 막으려면 생성자에서 예외를 던지는 것으로 충분하지만, finalizer`가 있다면 그렇지 않다. `final` 클래스는 하위 클래스를 만들 수 없으니 이 공격에서 안전하다. `final`이 아닌 클래스를 `finalizer` 공격으로부터 방어하려면 아무 일도 하지 않는 `finalizer` 메서드를 만들고 `final`로 선언하면 된다.

## 소멸자가 쓰이는 곳은?

이쯤되면 자바에서 소멸자를 호출하는 것은 거의 금기시 되는 일이라 생각된다. 하지만 적절한 쓰임새가 두 가지 정도 있다.

첫 번째는 자원의 소유자가 `close` 메서드를 호출하지 않는 것을 대비한 안전망 역할이다. `cleaner`나 `finalizer`가 즉시 호출되리라는 보장은 없지만 클라이언트가 하지 않은 자원 회수를 늦게라도 해주는 것이 아예 안하는 것보단 낫다. 이러한 안전망 역할의 `finalizer`를 작성할 때는 그럴만한 가치가 있는지 심사숙고 해야한다. 자바의 일부 클래스에서는 안전망 역할의 `finalizer`를 제공한다. `FileInputSteam`, `FileOutputStream`, `TheadPoolExecutor`가 대표적이다. 이러한 문제를 사전에 방지하기 위해서는 `try-with-resources`를 사용하면 된다.

두 번째는 네이티브 피어(navive peer)와 연결된 객체에서이다. 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말한다. 네이티브 피어는 자바 객체가 아니라서 가비지 컬렉터가 존재를 알지 못하기 때문에 자바 피어를 회수할 때 네이티브 객체까지 회수하지 못한다. 따라서 `finalizer`와 `cleaner`를 사용하여 처리하기 적합하다. 단, 성능 저하를 감당할 수 있고 네이티브 피어가 심각한 자원을 가지고 있지 않을 때만 해당된다. 그 이외에는 `close` 메서드를 사용해야 한다.

## `cleaner` 사용 예

~~~java
public class Room implements AutoCloseable {

    private static final Cleaner cleaner = Cleaner.create();

    private final State state;

    private Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        this.state = new State(numJunkPiles);
        //cleaner에 Room과 State를 등록
        this.cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        cleanable.clean();
    }

    private static class State implements Runnable {
        //cleaner가 방 청소시 수거할 자원
        int numJunkPiles;

        public State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        //cleanable에 의해 딱 한 번 호출 된다.
        @Override
        public void run() {
            System.out.println("room cleaning");
            this.numJunkPiles = 0;
        }
    }

}
~~~

위의 코드에서 `run` 메서드가 호출되는 상황은 둘 중 하나이다. 보통은 `Room`의 `close` 메서드를 호출할 때이며 `close` 메서드에서 `Cleaneable`의 `clean` 메서드를 호출하면 이 메서드 안에서 `run`을 호출한다. 혹은 가비지 컬렉터가 `Room`을 회수할 때까지 클라이언트가 `close`를 호출하지 않는다면 `cleaner`가 `State`의 `run` 메서드를 호출해 줄 것이다.

이 때 **`State` 인스턴스는 절대로 `Room` 인스턴스를 참조하면 안 된다.** 서로 참조를 하게 되면 순환 참조가 생겨서 가비지 컬렉터가 `Room` 인스턴스를 회수해가지 않는다. `State`가 정적 중첩 클래스로 선언된 것이 그 이유이다. 정적이 아닌 중첩 클래스는 자동으로 바깥 객체의 참조를 갖게 되기 때문이다. 람다 또한 바깥 객체의 참조를 갖기 쉬우니 사용하지 않는 것이 좋다.

`Room`의 `cleaner`는 안전망으로만 쓰였다. `try-with-resources` 블록으로 감쌌다면 자동 청소는 필요하지 않다.

#### 잘 짜인 코드

~~~java
public class Adult {
    public static void main(String[] args) {
        try (Room room = new Room(7)) {
            System.out.println("hello");
        }
    }
}
~~~

#### 출력 결과
~~~
hello
room cleaning
~~~

기대했던 대로 출력이 된다.

#### 잘못 짜인 코드

~~~java
public class Teenager {
    public static void main(String[] args) {
        new Room(99);
        System.out.println("hello");
    }
}
~~~

#### 출력 결과
~~~
hello
~~~

room cleaning은 출력되지 않았다. `cleaner`의 명세를 보자

> The behavior of cleaners during System.exit is implementation specific. No guarantees are made relating to whether cleaning actions are invoked or not.
>
> System.exit을 호출할 때의 cleaner 동작은 구현하기 나름이다. 청소가 이뤄질지는 보장하지 않는다.

명세에 명시되어 있진 않지만 일반적인 프로그램 종료에서도 마찬가지이다. 나의 컴퓨터에서는 `System.gc`를 호출하면 종료 전에 `room cleaning`이 출력되지만 다른 JVM에서는 호출되지 않을 수도 있다.