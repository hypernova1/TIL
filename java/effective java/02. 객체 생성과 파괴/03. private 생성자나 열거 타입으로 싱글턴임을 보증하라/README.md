# private 생성자나 열거 타입으로 싱글턴임을 보증하라

싱글턴(Singleton)이란 인스턴스를 오직 하나만 생성하는 클래스이다. 인스턴스마다 상태를 가질필요가 없을 때 사용하면 된다. 하지만 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트에서 테스트하기가 어려워 질 수 있다. 타입을 인스턴스로 정의한 다음 인터페이스를 구현해서 만든 싱글턴이 아니라면 가짜(mock) 구현으로 대체할 수 없기 때문이다.

## 싱글턴 생성 방법과 장점
싱글턴을 만드는 방법은 보통 세가지가 있다. 첫 번째는 **생성자를 private으로 감추고 public static으로 만든 인스턴스 필드를 하나 두는 것**이다.
~~~java
public class Singleton {
    public static final Singleton INSTANCE = new Singleton();

    private Singleton() {}
}
~~~

이렇게 생성을 하면 생성자는 처음 `Singleton.INSTANCE` 필드를 호출할 때 한번만 호출이 된다. 외부에서 생성자를 호출할 수 있는 방법이 없기 때문에 인스턴스는 오직 하나만 생성된다는 것이 보장된다. 따라서 클라이언트는 손을 쓸 수 없다. 리플렉션을 이용하여 private 생성자를 가져올 수 있긴 하지만 이 경우는 생성자 내부에 두번 째 객체가 생성되려 할 때 예외를 던지게 처리하면 된다. 이 방식의 장점은 코드가 간결하고, 해당 클래스가 싱글턴임이 API에 명백히 드러난다 public static 필드가 final이라서 절대로 다른 객체를 참조할 수 없다.

~~~java
public class Singleton {
    public static final Singleton INSTANCE = new Singleton();

    private Singleton() {
        if (INSTANCE != null) {
            throw new RuntimeException();
        }
    }

    public void method() { ... }
}

//...
Singleton singleton = Singleton.INSTANCE;
Constructor<? extends Singleton> declaredConstructor = singleton.getClass().getDeclaredConstructor();
declaredConstructor.setAccessible(true);
Singleton singleton2 = declaredConstructor.newInstance();
~~~
에러 내용
~~~
Exception in thread "main" java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at Main.main(Main.java:11)
Caused by: java.lang.RuntimeException
	at Singleton.<init>(Singleton.java:7)
	... 5 more
~~~

싱글턴을 만드는 두 번째 방법은 **정적 팩터리 메서드를 제공하는 것**이다.
~~~java
public class Singleton {
    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() { return INSTANCE; }

    public void method() { ... }
}
~~~
`Singleton.getInstance` 메서드는 항상 같은 인스턴스를 반환하므로 인스턴스를 두 개 이상 만들 수 없다. 이 방식의 장점은 첫 째, API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다는 점이다. 유일한 인스턴스를 반환하는 팩터리 메서드가 호출하는 스레드 별로 다른 인스턴스를 넘겨주게 만들 수 있다. 두 번째는 원한다면 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다. 세 번째는 정적 팩터리의 메서드 참조자를 공급자(supplier)로 사용할 수 있다는 점이다. `Singleton.getInstance`를 `Supplier<Singleton>`으로 사용하는 식이다.

세 번째 방법은 **원소가 하나인 열거(enum) 타입을 선언하는 것**이다.

~~~java
public enum Singleton {
    INSTANCE;

    public void method() { ... }
}
~~~

첫 번째 방식과 비슷하지만, 더 간결하고 직렬화시 추가적인 작업이 필요 없고, 리플렉션 공격도 완벽히 막아준다. 조금 부자연스러워 보일 수 있지만 **대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.** 다만 싱글턴이 Enum 외의 클래스를 상속해야한다면 이 방법은 사용할 수 없다.

## 직렬화 방법
첫 번째, 두 번째 방식의 싱글턴을 직렬화하려면 단순시 `Serializable`을 구현한다고 선언하는 것만으로는 부족하다. 모든 인스턴스 필드를 일시적(transient)이라 선언하고 `readResolve` 메서드를 제공해야 한다. 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화 할 때마다 새로운 인스턴스가 만들어진다.

~~~java
private Object readResolve() {
    return INSTANCE;
}
~~~