# 다 쓴 객체 참조를 해제하라

C, C++과 달리 자바는 메모리를 프로그래머가 아닌 가비지 컬렉터가 관리해준다. 더 이상 사용되지 않는 객체는 알아서 회수해간다. 이 때문에 자칫 자바는 메모리 관리에 신경쓰지 않아도 되는 언어라 생각하기 쉽지만, 그렇지 않다. 다음 코드를 확인해보자.

~~~java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    public void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
~~~

위의 코드는 특별한 문제가 없어 보이고, 어떤 테스트를 하더라도 통과할 것이다. 하지만 숨어있는 문제가 있다. 바로 **메모리 누수**이다. 위의 `Stack` 클래스를 사용하다보면 점차 가비지 컬렉션 활동과 메모리 사용량이 늘어나 성능이 저하될 것이다. 심한 경우 디스크 페이징이나 `OutOfMemoryError`가 발생해 프로그램이 예기치 않게 종료될 수도 있다.

어떤 이유에서 위와 같은 현상이 발생하게 되는지 알아보자. `Stack`의 `push` 메서드를 실행하여 객체를 추가하게 되면 배열의 인덱스가 1씩 증가하면서 배열이 채워지게 되고, 배열이 가득 차면 `ensureCapacity` 메서드가 실행되어 배열의 길이가 두배가 되고 기존의 객체들을 복사한다. 따라서 아무리 `pop`을 실행하더라도 `size` 필드의 값이 줄어들 뿐 실제 배열의 객체가 지워지지 않기 때문에 가비지 컬렉터가 회수해가지 않는다. 이를 다 쓴 참조(obsolete reference)라 한다.

가비지 컬렉션 언어에서는 이러한 경우 메모리 누수를 찾기가 아주 까다로워진다. 또한 객체 참조를 하나 살려두면 그 객체 뿐만 아니라 그 객체가 참조하고 있는 다른 객체들도 가비지 컬렉터가 회수해가지 못하기 때문에 단 몇 개의 객체가 매우 많은 객체를 회수하지 못하게 할 수 있고 잠재적으로 성능에 악영향을 미칠 수 있다.

## 해결 방법
해결 방법은 간단하다. 참조를 다 썼을 때 null 처리를 해주어 참조를 해제하면 된다.

~~~java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null;
    return result;
}
~~~

## null 처리의 이점과 오해

다 쓴 참조를 null 처리하면 다른 이점도 있다. null 처리된 참조를 사용하려 하면 `NullPointException`이 발생하여 오류를 초기에 잡아낼 수 있기 때문이다. 하지만 모든 상황에서 null 처리를 하는 것은 불필요하며 코드를 지저분하게 만든다. **객체 참조를 null 처리 하는 일은 예외적인 경우여야 한다.** 다 쓴 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효 범위(scope) 밖으로 밀어내는 것이다.

## null 처리를 언제해야 할까

null 처리는 비활성 영역의 객체가 쓸모 없을 때 하면 된다. 위의 `Stack`의 삭제된 인덱스의 데이터들이 그 예이다. 이는 프로그래머만이 알 수 있기 때문에 null 처리를 하여 가비지 컬렉터에게 더 이상 사용하지 않는다고 알려야 한다. **일반적으로 자기 메모리를 직접 관리하는 클래스라면 프로그래머는 항시 메모리 누수에 주의해야한다.** 원소를 사용한 즉시 그 원소가 참조한 객체들을 다 null 처리 해줘야 한다.

## 메모리 누수의 주범들

### 캐시

캐시는 메모리 누수를 일으키는 또 다른 주범 중 하나이다. 객체 참조를 캐시에 넣고 객체를 다 쓴 뒤에 한참을 그냥 두게 되는 경우가 자주 있는데, 해법은 여러가지이다. 캐시 외부에서 키(key)를 참조하는 동안만 엔트리가 살아 있는 캐시가 필요한 상황이라면 `WeakHashMap`을 사용해서 캐시를 만들면 된다. 다 쓴 엔트리는 그 즉시 자동으로 제거되기 때문이다. 단 `WeakHashMap`은 이러한 상황에서만 유용하다. 캐시를 만들 때 보통은 캐시 엔트리의 유효 기간을 정확히 정의하기 어렵기 때문에 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식을 흔히 사용한다. 쓰지 않는 엔트리를 청소해주는 방식이다. `ScheduledThreadPoolExecutor`과 같은 백그라운드 스레드를 활용하거나 캐시에 새 엔트리를 추가할 때 부수 작업으로 수행하는 방법이 있다. `LinkedHashMap`은 `removeEldestEntry` 메서드를 써서 후자의 방식으로 처리한다.

### 리스너, 콜백

클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면, 뭔가 조치를 하기 전까진 콜백이 계속 쌓일 것이다. 이럴 때 콜백을 약한 참조(weak reference)로 저장하면 가비지 컬렉터가 즉시 수거해 간다.


## 강한 참조와 약한 참조

#### 강한 참조 - 참조가 해제되더라도 연결된 객체의 참조는 살아있다.
~~~java
Integer key = 1000;
String value = "hello";

Map<Integer, String> cache = new HashMap<>();
cache.put(key, value);
key = null;
System.gc();
System.out.println(cache.get(1000)); //hello
~~~

#### 약한 참조 - 참조가 해제되면 연결된 참조도 해제된다.
~~~java
Integer key = 1000;
String value = "hello";

Map<Integer, String> cache = new WeakHashMap<>();
cache.put(key, value);
key = null;
System.gc();
System.out.println(cache.get(1000)); //null
~~~

다만 주의할 점은 `String`의 경우 리터럴로 키를 만들게 되면 `intern` 메서드가 호출 되어 String pool에 저장되기 때문에 가비지 컬렉션이 수거해가지 않는다. 또한 Integer의 경우 -127 ~ 128 범위의 값들도 마찬가지이다.

## 참고

http://blog.breakingthat.com/2018/08/26/java-collection-map-weakhashmap/