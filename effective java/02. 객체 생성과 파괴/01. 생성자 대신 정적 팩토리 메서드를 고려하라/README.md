# 생성자 대신 정적 팩토리 메서드를 고려하라

## 생성자와 비교하여 가지는 장점

### 1. 이름을 가질 수 있다.

생성자에 넘기는 매개변수와 생성자 자체로는 반환될 객체의 특성을 제대로 설명하지 못한다. 하지만 정적 팩토리 메서드는 이름으로 반환될 객체의 특성을 묘사할 수 있다. 입력 매개변수의 순서를 다르게 하여 생성자를 추가할 수 있지만 사용하는 개발자 입장에서는 정확히 어떤 특성을 가진 객체가 생성되는지 기억하기 어렵다. **한 클래스에서 시그니처가 같은 생성자가 여러개 필요하다면, 생성자 대신 여러개의 정적 팩토리 메소드를 사용하고 구분할 수 있는 이름을 지어주자.**

~~~java
String name1 = "바둑이";
Animal dog = Animal.createDog(name1);

String name2 = "샘찬";
Animal human = Animal.createHuman(name2);
~~~

### 2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.

불변 클래스는 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 방식으로 불필요한 객체 생성을 피한다. 따라서 **생성 비용이 큰 객체가 자주 요청되는 상황이라면 성능에 상당히 이점이 있다.**

~~~java
boolean b1 = Boolean.valueOf(value);
boolean b2 = Boolean.valueOf(value);

System.out.println(b1 == b2); //true
~~~

이러한 방식을 사용하는 클래스는 언제 어느 인스턴스를 살아 있게 할지를 통제할 수 있기 때문에 **인스턴스 통제 클래스**(*instance-controlled*)라고 한다. 인스턴스를 통제하게 되면 클래스를 싱글턴(*singleton*)이나 아예 인스턴스화 불가(*noninstantiable*)하게 만들 수 있다.

### 3. 반환 타입의 하위 타입 객체를 반환할 수 있다.

**정적 팩토리 메소드는 반환할 객체의 클래스를 자유롭게 선택할 수 있는 유언성을 제공**하며, API를 만들 때 구현 클래스를 공개하지 않고도 그 객체를 반환할 수 있기 때문에 API를 작게 유지할 수 있다. 자바 9부터는 인터페이스에 private 정적 메서드를 허용하지만, 자바 8은 정적 필드와, 정적 멤버 클래스가 public이어야 한다.

~~~java
public interface Animal {
    static Dog createDog(String name) {
        return new Dog(name);
    }
    static Human createHuman(String name) {
        return new Humen(name);
    }

    class Dog implements Animal {}
    class Human implements Animal {}
}
~~~

### 4. 입력 매개변수에 따라 다른 클래스의 객체를 반환할 수 있다.

**반환 타입이 하위 타입이기만 하면 어떤 클래스를 반환하든 상관없고, 심지어 다른 클래스의 객체를 반환해도 된다.** `EnumSet`클래스는 public 생성자 없이 오직 정적 팩토리 메서드만 제공하는데, OpenJDK에서는 원수의 수에 따라 다른 인스턴스를 반환한다. 사용자는 반환되는 인스턴스의 클래스를 알 필요가 없고, 그저 `EnumSet`의 하위 클래스이기만 하면 된다.

~~~java
public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) {
    Enum<?>[] universe = getUniverse(elementType);
    if (universe == null)
        throw new ClassCastException(elementType + " not an enum");

    if (universe.length <= 64)
        return new RegularEnumSet<>(elementType, universe);
    else
        return new JumboEnumSet<>(elementType, universe);
}
~~~

### 5. 정적 팩토리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

이런 유연함은 서비스 제공자 프레임워크(*service privider framework*)를 만드는 근간이 된다. 대표적으로 JDBC(*Java Database Connectivity*)가 있다. 서비스 제공자 프로임워크에서 제공자는 서비스의 구현체이다. 이 구현체들을 클라이언트에서 제공하는 역할을 프레임워크가 통제하여, 클라이언트를 구현체로부터 분리해준다.

#### 서비스 제공자 프레임워크의 4가지 핵심 컴포넌트
* 서비스 인터페이스(service interface): 구현체의 동작을 정의
* 제공자 등록 API(provider registration API): 제공자가 구현체를 등록할 때 사용
* 서비스 접근 API(service access API): 클라이언트가 서비스의 인스턴스를 얻을 때 사용
* 서비스 제공자 인터페이스(service provider interface): 서비스 인터페이스의 인스턴스를 생성하는 팩터리 객체를 설명 

### jdbc에서의 서비스 프레임워크 핵심 컴포넌트 분류
~~~java
//서비스 인터페이스
public interface Connection {...

//서비스 제공자 인터페이스
Driver driver = new OracleDriver();

//제공자 등록 API
DriverManager.registerDriver(driver);
//제공자 등록 API 2
Class.forName("oracle.jdbc.driver.OracleDriver");

//서비스 접근 API
DriverManager.getConneton(url, user, password);
~~~

## 단점

### 1. 상속을 하려면 public 이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.

컬렉션 프레임워크의 유틸리티 구현 클래스 들은 상속이 불가능하다. 하지만 이 제약은 상속보다 컴포지션을 사용하도록 유도하고 불변 타입으로 만들려면 이 제약을 지켜야한다는 점에서 장점으로 받아들여질 수도 있다.

### 2. 정적 팩토리 메서드는 프로그래머가 찾기 어렵다.

생성자처럼 API 설명에 명확히 드러나지 않아 인스턴스화 방법을 직접 알아야 한다. 그러므로 알려진 규약을 따라 짓는 식으로 문제를 완화해주어야 한다.

#### 정적 팩토리 메서드 명명 방식들

* `from` 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드
~~~java
Instant instant = Instant.now();
Date d = Date.from(instant);
~~~

* `of` 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
~~~java
Set<Rank> faceCard = EnumSet.of(JACK, QUEEN, KING);
~~~

* `valueOf`: `from`과 `of`의 자세한 버전
~~~java
BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
~~~

* `instance`, `getInstance`: 매개변수로 명시한 인스턴스를 반환하지만 같은 인스턴스임을 보장하지 않는다.

~~~java
StackWalker luke = StackWalker.getInstance(options);
~~~

* `create`, `newInstance`: 매번 새로운 인스턴스를 반환한다.

~~~java
Object newAray = Array.newInstance(classObject, arrayLen);
~~~

* `getType`: `getInstance`와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. `Type`은 반환할 객체의 타입이다.

~~~java
FileStore fs = Files.getFileStore(path);
~~~

* `newType`: `newInstance`과 같으나, 생성한 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다.

~~~java
BufferedReader br = Files.newBufferedReader(path);
~~~

* `type`: `getType`, `newType`의 간결한 버전

~~~java
List<Complaint> litany = Collections.list(legacyLitany);
~~~
