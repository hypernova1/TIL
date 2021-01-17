# equals는 일반 규약을 지켜 재정의하라

equal 메서드를 재정의하는 것은 쉬워보이지만 함정이 있어서 끔찍한 결과를 초래할 수 있다. 따라서 다음과 같은 상황 중 하나에 해당한다면 재정의하지 않는 것이 좋다. **즉, 꼭 필요한 경우가 아니라면 equals를 재정의 하지 말라는 말이다.**

- 각 인스턴스가 본질적으로 고유하다.
  - 값을 표현하는 것이 아니라 동작하는 개체를 표현하는 클래스가 여기에 해당한다. `Thread`가 좋은 예로, `Object`의 `equals` 메서드는 이러한 클래스에 맞게 구현되었다.

- 인스턴스의 논리적 동치성을 검사할 일이 없다.
  - `java.util.regex.Pattern`은 `equals`를 재정의 해서 두 `Pattern`의 인스턴스가 같은 정규 표현식을 나타내는지를 검사하는, 즉 논리적 동치성을 검사하는 방법도 있다. 하지만 설계자는 클라이언트가 이 방식을 원하지 않거나 애초에 필요하지 않다고 판단할 수도 있다. 설계자가 후자로 판단했다면 `Object`의 기본 `equals`만으로 해결된다.

- 상위 클래스에서 재정의한 `equals` 메서드가 하위 클래스에도 맞는다.
  - 대부분의 `Set` 구현체는 `AbstractSet`이 구현한 `equals`를 사용한다.

- 클래스가 `private`이거나 `package-private`이고 `equals` 메서드를 호출할 일이 없다.
  - `equals`가 실수로라도 호출되는 것을 막고 싶다면
    - ~~~java
      @Override
      public boolean equals(Object o) {
          throw new AssortionError();
      }
      ~~~

## equals를 재정의해야 할 때는 언제인가?

객체 식별성이 아니라 논리적 동치성을 확인해야 하는데, 상위 클래스의 `equals`가 논리적 동치성을 비교하도록 정의되어있지 않았을 때 사용한다. 주로 값 클래스(String, Integer과 같은 값을 주로 표현하는 클래스)들이 여기에 해당한다. 두 객체가 같은지가 아니라 값이 같은지를 알고싶을 것이기 때문이다. 또한 논리적 동치성을 확인하도록 재정의하면 `Map`의 키나 `Set`의 값으로 사용가능하다.

값 클래스라 하더라도, 값이 가은 인스턴스가 둘 이상 만들어지지 않게 보장하는 인스턴스 통제 클래스라면 `equals`를 재정의하지 않아도 된다. `Enum`도 여기에 해당한다. 이런 클래스는 어차피 논리적으로 같은 인스턴스가 둘 이상 만들어지지 않으니 논리적 동치성과 객체 식별성이 사실상 같은 의미가 된다. 따라서 `Object`의 `equals`가 논리적 동치성까지 확인해준다고 볼 수 있다.

## equals 메서드를 재정의하는 일반 규약 (Object 명세)

`Object` 명세에서 말하는 동치관계란 집합을 서로 같은 원소들로 이뤄진 부분집합으로 나누는 연산이다. 이 부분집합을 동치류(equivalence class)라 한다. `equals` 메서드가 쓸모 있으려면 모든 원소가 같은 동치류에 속한 어떤 원소와도 서로 교환할 수 있어야 한다.

> equals 메서드는 동치관계를 구현하며, 다음을 만족한다.

### 반사성(reflexivity)
null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true이다.

즉, 객체는 자기 자신과 같아야 한다.

### 대칭성(symmetry)
null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true이다.

두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다.

~~~java
public final class CaseInsensitiveString {
    private final String s;
    
    public CaseInsentiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        if (o instanceof String)
            return s.equalsIgnoreCase((String) o);
        return false;
    }
}
~~~

~~~java
CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
String s = "polish";
  
System.out.println(cis.equals(s)); //true
System.out.println(s.equals(cis)); //false
~~~

위의 경우 `CaseInsensitiveString`의 `equals` 메서드는 일반 스트링에 대한 처리가 따로 되어 있지만 `String`의 `equals` 메서드는 `CaseInsensitiveString`에 대한 처리가 없기 때문에 서로 상반된 결과가 나온다.

~~~java
List<CasInsensitiveString> list = new ArrayList<>();
list.add(cis);

System.out.println(list.contain(s)); //?
~~~ 

현재의 OpenJDK에서는 false를 반환하지만 다른 JDK에서는 true를 반환하거나 런타임 예외를 던질 수도 있다. **equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응하는지 알 수 없다.**

이 문제를 해결하려면 그냥 `CaseInsensitiveString`의 `equals`를 `String`과 연동하지 않으면 된다.

~~~java
@Override
public boolean equals(Object o) {
      return o instanceof CaseInsentiveString && ((CaseInsensitiveString) o).s.eqaulsIgnoreCase(s);
}
~~~

### 추이성(transitivity)
null이 아닌 모든 참조 값 x, y, z에 대해, x.equals(y)가 true이고 y.equals(z)도 true이면 x.equals(z)도 true이다.

첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세번째 객체는 같아야 한다는 뜻이다.
  
~~~java
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}

public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }
}
~~~
위의 경우 `ColorPoint`의 `equals` 메서드는 어떻게 해야할까? 그대로 둔다면 `Point`의 구현이 상속되어 색상은 무시한 채로 비교하게 될 것이다. `equals` 규약을 어긴 것은 아니지만 중요한 정보를 놓치게 된다.

#### 잘못된 코드 - 대칭성 위배
~~~java
@Override
public boolean equals(Object o) {
    if (!(o instanceof ColorPoint))
        return false;
    return super.equals(o) && ((ColorPoint) o).color = color;
}
~~~

위의 메서드는 `Point`를 `ColorPoint`에 비교한 결과와 그 둘을 바꿔 비교한 결과가 다를 수 있다. `Point`의 `equals`는 색상을 무시하고 `ColorPoint`의 `equals`는 입력 매개변수의 클래스 종류가 다르다며 매번 `false`를 반환할 것이다.

~~~java
Point p = new Point(1, 2);
ColorPoint cp = new ColorPoint(1, 2, Color.RED);

System.out.println(p.equals(cp)); //true
System.out.println(cp.equals(p)); //false
~~~

#### 잘못된 코드 - 추이성 위배
~~~java
@Override
public boolean equals(Object o) {
    if (!(o instanceof Point))
        return false;
    // o가 일반 Point면 색상을 무시하고 비교
    if (!(o instanceof ColorPoint))
        return o.equals(this);
    // o가 ColorPoint면 색상까지 비교
    return super.equals(o) && ((ColorPoint) o).color = color;
}
~~~

위의 방식은 대칭성을 지켜주지만 추이성을 깨버린다.

~~~java
ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
Point p2 = new Point(1, 2);
ColorPoint p3 = new ColorPoint(1, 2 Color.BLUE);

System.out.println(p1.equals(p2)); //true
System.out.println(p2.equals(p3)); //true
System.out.println(p1.equals(p3)); //false
~~~

또한, 이 방식은 무한 재귀에 빠질 위험이 있다. Point의 또 다른 하위 클래스 `SmellPoint`를 만들고 `equals`를 같은 방식으로 구현한 다음 `myColorPoint.equals(mySmellPoint)`를 호출하면 StackOverflowError`를 일으킨다.

~~~java
public class SmellPoint extends Point {
    private final Smell smell;

    public SmellPoint(int x, int y, Smell smell) {
        super(x, y);
        this.smell = smell;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        
        if (!(o instanceof SmellPoint))
            return o.equals(this);
        
        return super.equals(o) && this.smell = ((SmellPoint) o).smell;
    }
}

~~~

~~~java
Point p1 = new SmellPoint(1, 2, Smell.SWEET);
Point p2 = new ColorPoint(1, 2, Color.RED);

System.out.println(p1.equals(p2));
~~~

`p1.equals(p2)`는 `SmellPoint`의 `equals` 메서드를 호출하게 되는데, 2번째 if 블록에서 `o.equals(this)`는 다시 `ColorPoint`의 `equals` 메서드를 호출하게 된다. 마찬가지로 `ColorPoint`의 `equals`메서드의 두 번째 블록에서는 다시 `SmellPoint`의 `equals`메서드를 호출하게 되므로 무한 재귀에 빠지게 된다.

이 현상은 모든 객체 지향 언어의 동치관계에서 나타나는 근본적인 문제이다. **구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.** 객체 지향적 추상화의 이점을 포기하지 않는 한은 말이다.

이 말은 `instanceof` 검사를 `getClass()` 검사로 바꾸면 된다는 뜻으로 들린다.

~~~java
@Override
public boolean equals(Object o) {
    if (o == null || o.getClass() != getClass())
        return false;
    Point p = (Point) o;
    return p.x  == x && p.y == y;
}
~~~
위의 `equals`의 경우 같은 구현 클래스의 객체를 비교할 때만 `true`를 반환한다. 하지만 `Point`의 하위 클래스는 정의상 여전히 `Point` 클래스 이므로 어디서든지 `Point`로 사용될 수 있어야 한다. 위의 코드는 그렇지 못하기 때문에 사용할 수 없다.

예를 들어 주어진 점이 반지름이 반지름이 1인 단위 원 안에 있는지를 판별하는 메서드를 구현해보자.

~~~java
private static final Set<Point> unitCircle = Set.of(
    new Point(1, 0), new Point(0, 1),
    new Point(-1, 0), new Point(0, -1)
);

public static boolean onUnitCircle(Point p) {
    return unitCircle.contains(p);
}
~~~

이제 값을 추가하지 않는 방식으로 `Point`를 확장해보자. 만들어진 인스턴스의 개수를 생성자에서 세보도록 한다.

~~~java
public class CounterPoint extends Point {
    private static final AtomicInteger counter = AtomicInteger();

    public CounterPoint(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }

    public static int numberCreated() {
        return counter.get();
    }
}
~~~

리스코프 치환 원칙에 따르면, 어떤 타입에 있어 중요한 속성이라면 그 하위 타입에서도 마찬가지로 중요하다. 따라서 그 타입의 메서드가 하위 타입에서도 똑같이 잘 작동해야 한다.

그런데 `CounterPoint`의 인스턴스를 `onUnitCircle`메서드에 넘기면 false를 반환할 것이다. `Set`을 포함하여 대부분의 컬렉션 구현체에서 주어진 원소를 담고 있는지를 확인할 때 `equals` 메서드를 사용하기 때문이다. `CointerPoint`의 인스턴스는 어떤 `Point`와도 같을 수 없다. 반면 `instanceof` 기반으로 올바르게 구현했다면 `onUnitCircle` 메서드는 제대로 동작할 것이다.

구체 클래스의 하위 클래스에서 값을 추가할 방법은 없지만 괜찮은 우회 방법이 있다. `Point`를 상속하는 대신 `Point`를 `CounterPoint`의 `private` 필드로 두고, `ColorPoint`와 같은 위치의 일반 `Point`를 반환하는 view 메서드를 `public`으로 추가하는 방법이다.

~~~java
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        point = new Point(x, y);
        this.colir = Objects.requireNonNull(color);
    }

    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
~~~

자바 라이브러리에도 구체 클래스를 확장해 값을 추가한 클래스가 종종 있다. `java.sql.Timestamp`는 `java.util.Date`를 확장한 후 `nanoseconds` 필드를 추가했다. 그 결과로 `Timestamp`의 `equals`는 대칭성을 위배하며 `Date` 객체와 섞어 쓸 때 엉뚱하게 동작할 수 있다. 그래서 `Timestamp`의 API 설명에는 `Date`와 섞어 쓸 때 주의사항을 언급하고 있다. `Timestamp` 설계는 실수니 따라하지 말자.

### 일관성(consistency)
null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다.

두 객체가 같다면 앞으로도 영원히 같아야 한다는 뜻이다. 가변 객체는 비교 시점에 따라 서로 다를 수도, 같을 수도 있지만 불변 객체는 한 번 같으면 끝까지 같아야하고 다르면 끝까지 달라야 한다.

클래스가 불변이든 가변이든 `equals` 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안 된다. 이 제약을 어기면 일관성 조건을 만족하기가 아주 어렵다. `java.util.URL`의 `equals`는 주어진 URL과 매칭된 호스트의 IP 주소를 이용해 비교한다. 호스트 이름을 IP 주소로 바꾸려면 네트워크를 통해야 하는데, 그 결과가 항상 같다고 보장할 수 없다. 이런 문제를 피하려면 `equals`는 항시 메모리에 존재하는 객체만을 사용한 결정적 계산을 수행해야 한다.

### null-아님
null이 아닌 모든 참조값 x에 대해, x.equals(null)은 false다.

모든 객체가 null과 같지 않아야 한다는 뜻이다.

~~~java
@Override
public boolean equals(Object o) {
    if (o == null)
        return false;
    //...
}
~~~

사실 위와 같은 검사는 필요하지 않다. 동치성을 검사하려면 `equals`는 건네받은 객체를 적절히 형변환한 후 필수 필드들의 값을 알아내야 한다. 그러려면 형변환에 앞서 `instanceof` 연산자로 입력 매개변수가 올바른 타입인지 검사해야 한다.

~~~java
@Override
public boolean equals(Object o) {
    if (!(o instanceof MyType))
        return false;
    
    MyType mt = (MyType) o;
    //...
}
~~~

`instanceof`는 피연산자가 `null`이라면 `false`를 반환하기 때문에 `null` 검사를 명시적으로 해주지 않아도 된다.

> ### equals 메서드 구현 단계
> 1. == 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.
>     * 자기 자신이면 `true`를 반환한다. 이는 단순한 성능 최적화 용으로, 비교 작업이 복잡한 상황일 때 값어치를 한다.
> 2. instanceof 연산자로 입력이 올바른 타입인지 확인한다.
>     * 그렇지 않다면 false를 반환한다.
>     * 이때의 올바른 타입은 equals가 정의된 클래스인 것이 보통이지만 가끔은 그 클래스가 구현한 특정 인터페이스가 될 수도 있다.
>     * 어떤 인터페이스는 자신을 구현한 서로다른 클래스 끼리도 비교할 수 있도록 equals를 수정하기도 한다. 이런 인터페이스를 구현한 클래스라면 equals에서 클래스가 아닌 해당 인터페이스를 사용해야 한다.
>     * Set, List, Map, Map.Entry 등의 컬렉션 인터페이스 들이 여기 해당한다.
> 3. 입력을 올바른 타입으로 형변환한다.
>    * 앞에서 instanceof 연산자로 검증을 했기 때문에 이 단계는 100% 성공한다.
> 4. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나하나 검사한다.
>    * 모든 필드가 일치하면 true, 하나라도 다르다면 false를 반환한다.

### 각 티입별 비교 방법

* `float`과 `double`을 제외한 기본 타입
  * `==` 연산자로 비교
* 참조 타입
  * `equals` 메서드
* `null`을 정상 값으로 취급하는 참조타입
  * `Objects.equals(Object, Object)`
    * `NullPointException`을 예방할 수 있다.
* `float`, `double`
  * 각각 정적 메서드인 `Float.compare(float, float)`, `Double.compare(double, double)`로 비교
  * 'float`과 `double`은 특수한 부동 소수값 등을 다뤄야 하기 때문.
  * `Float.equals`와 `Double.equals`를 사용할 수도 있지만 오토박싱을 수반할 수 있으니 성능상 좋지 않다.
* 배열
  * 원소 각각을 앞의 방뱁대로 비교한다.
  * 모든 원소가 핵심 필드라면 `Arrays.equals` 메서드들 중 하나를 사용
* 앞서 예제로 든 CaseInsensitiveString 클래스와 같은 복잡한 필드를 가진 클래스
  * 그 필드의 표준형(canocical form)을 저장해둔 후 표준형끼리 비교
  * 가변 객체라면 값이 바뀔 때마다 표준형을 최신상태로 갱신

어떤 필드를 먼저 비교하느냐에 따라 `equals`의 성능을 좌우하기도 한다. 최상의 성능을 바란다면 다를 가능성이 크거나 비교하는 비용이 싼 필드를 먼저 비교하면 된다. 또한 **동기화용 락(lock) 필드 같이 객체의 논리적 상태와 관련 없는 필드는 비교하면 안된다.**

### 주의사항
* equals를 재정의할 땐 hashcode도 반드시 재정의하자.
* 너무 복잡하게 해결하려 들지 말자.
  * 필드의 동치성만 검사해도 equals의 규약을 어렵지 않게 지킬 수 있다.오히려 너무 공격적으로 파고들다가 문제를 일으키기도 한다. 일반적으로 별칭(alias)는 비교하지 않는 것이 좋다.
* Object 외의 타입을 매개변수로 받는 euqals 메서드는 선언하지 말자.
    ~~~java
    public boolean equals(MyClass o) {
        ...
    }
    ~~~
  * 위의 메서드는 Object.equals를 재정의한게 아니라 다중정의를 한 것이다.
  * `@Override` 애노테이션을 일관되게 사용하면 이러한 실수를 예방할 수 있다.