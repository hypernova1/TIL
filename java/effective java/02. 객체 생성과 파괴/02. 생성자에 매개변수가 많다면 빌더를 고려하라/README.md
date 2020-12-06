# 생성자에 매개변수가 많다면 빌더를 고려하라

생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는 것이 더 낫다. 매개변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그렇다.

## 정적 팩터리와 생성자에 있는 제약과 해결 방법

선택적 매개변수가 많을 때 절적히 대응하기 어렵다. 그 해결 방법으로 *점층적 생성자 패턴*, *자바빈즈 패턴*, *빌터 패턴*을 사용할 수 있다.

### 1. 점층적 생성자 패턴(Telescoping Constructor Pattern)

필수 매개변수만 받는 생성자, 필수매개 변수와 선택 매개변수를 1개를 받는 생성자, 선택 매개변수를 2개 받는 생성자, ..., 모든 매개변수를 전부다 받는 생성자까지 늘리는 방식이다.

~~~java
class Member {
    private final String name;      //필수
    private final int age;          //필수
    private final String job;       //선택
    private final String address;   //선택
    private final String zipcode;   //선택

    public Member(String name, int age) {
        this(name, age, "none");
    }

    public Member(String name, int age, String job) {
        this(name, age, job, "none");
    }

    public Member(String name, int age, String job, String address) {
        this(name, age, job, address, "none");
    }

    public Member(String name, int age, String job, String address, String zipcode) {
        this.name = name;
        this.age = age;
        this.job = job;
        this.address = address;
        this.zipcode = zipcode;
    }
}
~~~

위의 코드는 매개변수가 다섯개 뿐이라 그리 복잡해 보이진 않지만 **매개변수의 수가 늘어날 수록 걷잡을 수 없이 복잡해진다. 또한 파라미터의 순서를 제대로 알지 못하면 엉뚱한 데이터를 사용하게 될 수 있다.** 타입이 다르다면 컴파일시 에러를 잡을 수 있겠지만 같은 타입이라면 런타임시 엉뚱한 동작을 하게 된다.

~~~java
Member member = new Member("sam", 31, "seoul", "00000", "student");

System.out.print(member.getJob()); //seoul
~~~

### 2. 자바빈즈(JavaBeans Pattern) 패턴

매개변수가 없는 생성자로 객체를 만든 후 세터(Setter)메서드를 호출하여 원하는 매개변수의 값을 설정하는 방식이다.

~~~java
class Member {
    private final String name;      //필수
    private final int age;          //필수
    private final String job;       //선택
    private final String address;   //선택
    private final String zipcode;   //선택

    public Member() {}

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setJob(String job) { this.job = job; }
    public void setAddress(String address) { this.address = address; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
}
~~~

~~~java
Member member = new Member();
member.setName("sam");
member.setAge(31);
member.setJob("student");
member.setAddress("seoul");
member.setZipcode("00000")
~~~

자바빈즈 패턴을 사용하면 인스턴스를 만들기 쉽고 점층적 생성자 패턴보다 덜 복잡해져서 코드가 읽기 쉬워졌다. 하지만 **세터 메서드를 여러 개 호출해야 하며, 객체가 완전히 생성되기 전까지는 일관성(Consistency)이 무너진 상태에 놓이게 된다.** 점층적 생성자 패턴은 매개변수들이 유효한지를 생성자에서만 확인하면 일관성을 유지할 수 있지만 자바빈즈 패턴은 그러한 기준이 없다. 따라서 런타임시 디버깅하기가 힘들어진다. **일관성이 무너지는 문제 때문에 자바빈즈 패턴에서는 클래스를 불변으로 만들 수 없고, 스레드 안정성을 얻기 위해 프로그래머가 추가작업을 해주어야 한다.** 이러한 단점을 완화하기 위해 생성이 끝난 객체를 수동으로 얼린 후 얼리기 전에는 사용할 수 없도록 하기도 하는데 다루기 어려워서 실전에선 거의 쓰이지 않는다. 사용한다 하더라도 프로그래머가 얼리는(freeze) 메서드를 호출했는지 컴파일러가 보증할 수 없어 런타임 오류에 취약하다.

### 3. **빌더패턴**

빌더패턴은 파이썬, 스칼라에 있는 **명명된 선택적 매개변수(Named Optional Parameters)를 흉내낸 것**이다. 필요한 객체를 직접 만드는 대신 필수 매개변수만으로 생성자를 호출하여 빌더 객체를 얻고, 빌더 객체가 제공하는 함수를 통해 선택 매개변수를 설정하는 방식이다. 빌더를 생성할 클래스 안에 정적 멤버 클래스로 만들어 사용한다.

~~~java
class Member {
    private final String name;
    private final int age;
    private final String job;
    private final String address;
    private final String zipcode;

    public static class Builder {
        //필수 매개변수
        private String name;
        private int age;

        //선택 매개변수 - 기본값으로 초기화
        private String job = null;
        private String address = null;
        private String zipcode = null;

        public Builder(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Builder job(String job) {
            this.job = job;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder zipcode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }

    private Member(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.job = builder.job;
        this.address = builder.address;
        this.zipcode = builder.zipcode;
    }
}
~~~

Member 클래스는 불변이며, 모든 매개변수의 기본값 들을 한 곳에 모아뒀다. 빌더 클래스의 세터 메서드는 자신을 반환하기 때문에, 연쇄적으로 호출이 가능하다.

~~~java
Member member = new Member.Builder("sam", 31).job("student").address("seoul").zipcode("00000").build();
~~~

잘못된 매개변수를 최대한 일찍 발견하려면 빌더의 생성자와 메서드에서 입력 매개변수를 검사하고, build 메서드가 호출하는 생성자에서 여러 매개변수에 걸친 불변식을 검사하면 된다. 잘못된 점을 발견하면 어떤 매개변수가 잘못되었는지를 자세히 알려주는 메세지를 담아 IllegalArgumentException을 던지면 된다.

>>>
불변(immutable, immutability)이란 어떠한 변경도 허용하지 않는다는 뜻으로, 변경을 허용하는 가변(mutable) 객체와 구분하는 용도로 쓰인다. 대표적으로 String 객체는 한 번 만들어지면 값을 바꿀 수 없는 불변 객체이다. 불변식(invariant)이란 프로그램이 실행되는 동안, 혹은 정해진 기간 동안 반드시 만족해야 하는 조건을 말한다. 변경을 할 수 는 있으나 주어진 조건 내에서만 허용한다는 뜻이다. 예를 들어 나이는 음수가 될 수 없고, 시작일은 종료일보다 느릴 수 없다. 따라서 가변 객체에도 불변식은 존재할 수 있고 불변은 불변식의 극단적 예라 할 수 있다.
>>>

**빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기 좋다.** 각 계층의 클래스에 관련 빌더를 멤버로 정의한다. 추상 클래스는 추상 빌더를, 구체 클래스는 구체 빌더를 갖게 한다.

~~~java
public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
    final Set<Topping> topping;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.nonOf(Topping.class);
        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();

            abstract Pizza build();

            //하위 클래스는 이 메서드를 재정의 하여 this를 반환하도록 한다.
            protected abstract T self();
        }
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppins.clone();
    }
}
~~~

Pizza.Builder 클래스는 재귀적 타입 한정을 이용하는 제네릭 타입이다. 여기에 추상 메서드인 self를 더해 하위 클래스에서는 형변환하지 않고도 메서드 연쇄를 지원할 수 있다. self 타입이 없는 자바를 위한 이 우회 방법을 **시뮬레이트한 셀프 타입**(simulated self-type) 관용구라 한다.

Pizza 클래스에는 하위 클래스 2개가 있다. 뉴욕피자와, 칼초네 피자이다. 뉴욕 피자는 크기 매개변수를 필수로 받고 칼초네 피자는 소스를 안에 넣을지 선택하는 매개변수를 필수로 받는다.

#### 뉴욕피자
~~~java
public class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public MyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private MyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}
~~~

#### 칼초네 피자
~~~java
public class Clazone extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside;

        public Builder sauceInside() {
            this.sauceInside = true;
            return this;
        }

        @Override
        public Calzode build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private Calzone(Builder builder) {
        super(builder);
        this.sauceInside = builder.sauceInside;
    }
}
~~~

각 하위 클래스의 빌더가 정의한 build 메서드는 해당하는 구체 하위 클래스를 반환하도록 선언한다. NyPizza.Builder는 NyPizza를, Calzone.Builder는 Calzone를 반환한다는 뜻이다. 하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌 그 하위 타입을 반환하는 기능을 **공변 반환 타이핑**(covariant return typing)이라 한다. 이 기능을 사용하면 클라이언트가 형변환에 신경쓰지 않고 빌더를 사용할 수 있다.

~~~java
NyPizza pizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();

Calzone calzone = new Calzone.Builder().addTopping(HAM).sauceInsize().build();
~~~

빌더 패턴은 상당히 유연하다. 빌더 하나로 여러 객체를 순회하면서 만들 수 있고, 빌더에 넘기는 매개변수에 따라 다른 객체를 만들 수도 있다. 하지만 빌더 패턴은 객체를 만들려면 빌더부터 만들어야 하기 때문에 민감한 상황에서는 문제가 될 수 있다. 또한 점층적 생성자 패턴보다는 코드가 장황하기 때문에 매개변수가 4개 이상은 되어야 이득이 있다. 하지만 API는 시간이 지날 수록 매개변수가 증가하는 경향이 있기 때문에 빌더 패턴으로 시작해도 좋은 상황이 많다.
