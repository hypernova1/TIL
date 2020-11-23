# 자바 데이터 타입, 변수 그리고 배열

## 데이터 타입이란

모든 프로그래밍 언어는 값을 재사용할 수 있도록 변수에 담아 사용하게 됩니다. 하지만 값은 다음과 같이 여러 종류로 표현할 수 있습니다.

* 안녕하세요
* 24
* 3.14
* true
* ...

이 외에도 프로그래밍에서는 리터럴(위에 표현된 상수 들을 프로그래밍에서는 리터럴이라 합니다) 값을 변수에 담아야 할 때도 있고, 같은 타입의 값 여러개를 담아야 할 때도 있습니다. 또한 리터럴이 아닌 값을 담아야할 때도 있죠. 따라서 값을 구분지어 사용할 필요가 있습니다. 바로 데이터 타입을 통해서 말이죠.

## 정적 타입 언어, 동적 타입 언어

정적 타입 언어란 변수 선언시 타입을 직접적으로 명시해 주어야 하는 언어를 말합니다. C나 C++, 자바 등이 바로 정적 타입 언어입니다. 컴파일시 타입이 체크되며 값이 타입에 맞지 않으면 컴파일 에러가 발생합니다.

~~~java
int num = 1; //ok
String str = "안녕하세요"; //ok
String str2 = 3; //error
~~~

컴파일시 체크를 하기 때문에 속도가 빠르고 타입 안정성이 좋습니다. 또한 우리가 사용하는 개발툴에서도 타입을 명확하게 알 수 있기 때문에 인텔리센스 지원이 정확합니다.

반면 정적 타입은 변수 선언시 타입을 명시하지 않기 때문에 정적 타입의 연어보다 유연하게 프로그래밍이 가능하다는 장점이 있습니다.

~~~js
var num = 1; //ok
num = "str"; //ok
~~~

하지만 런타임시 타입을 정하기 때문에 타입의 불일치에서 오는 버그를 발생시키기 쉽습니다. 대표적으로 자바스크립트, 파이썬, 루비 등이 있습니다.

~~~js
function sub(a, b) {
    return a - b;
}

sub(2, 1) //1

//에러는 발생하지 않지만 값이 숫자가 아니기 때문에 값을 사용하는 곳에서 버그가 발생할 수 있음
sub("a", 1);
~~~

정적 언어 타입은 코딩시 타입을 직접 판단할 수 없기 때문에 인텔리센스 지원이 매우 한정적입니다. 이러한 불편함을 해소하고자 자바스크립트 진영에서는 타입스크립트를 만들어 사용하고 있습니다. 또한 정적 타입 언어는 처음 선언된 타입으로 타입이 고정되는 언어도 존재합니다. 대표적으로 코틀린 등이 있습니다.

~~~kt

var name = "sam";
name = 1; //error

~~~

사실 자바도 컴파일러가 타입을 동적으로 추론할 수 있고 Reflection API를 통해 해당 타입의 메서드도 사용할 수 있습니다. 하지만 그 방법이 복잡하고, 정적 타입의 경우보다 속도가 느립니다.

~~~java
class MyClass {
    public void method() {
        System.out.println("hello");
    }
}

public class Main {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Object a = new MyClass();
        Method method = a.getClass().getMethod("method");
        
        System.out.println(a.getClass()); //class MyClass
        method.invoke(a); //hello
    }

}
~~~

## 원시타입의 종류와 범위

자바에는 총 8가지의 원시타입이 존재합니다. 원시타입이란 인스턴스를 생성하지 않고 그 자체로 값을 가지는 타입을 말합니다. 종류는 다음과 같습니다.

|타입명|크기|범위|기본값|
|---|---|---|--|
|boolean|1 bit|true, false|false|
|byte|1 byte|-128 ~ 127|0|
|char|2 bytes|'\u0000' ~ 'uFFFF'|'\u0000'|
|short|2 bytes|-32,768 ~ 32,767|0|0|
|int|2 bytes|-2,147,483,684 ~ 2,147,483,647|0|
|long|8 bytes|-9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807|0L|
|float|4 bytes|-3.4E+38의 근사값 ~ 3.4E+38의 근사값|0.0f|
|double|8 bytes|-1.7E+308의 근사값 ~ 1.7E+308의 근사값|0.0|

원시타입은 크게 숫자, 문자(char), 논리형(boolean)으로 구분됩니다. 숫자는 다시 정수(byte, short, int, long)와 실수(float, double)로 나뉩니다. 다만 실수형은 정확한 값이 아닌 부동소수점을 이용하기 때문에 실제값과 오차가 발생할 수 있습니다. 따라서 정확한 값이 필요할 때는 `BitDecimal`이라는 타입을 사용해야합니다.

## 원시(Primitive) 타입과 참조(Referece) 타입

자바는 원시 타입과 참조 타입으로 나뉩니다. 원시 타입은 방금 전 설명했던 것 처럼 고정된 범위를 가지며 자바 메모리 영역 중 Stack 영역에 저장되고 메서드가 종료되면 바로 제거가 됩니다. 반면 참조 타입은 Heap 영역에 저장이 되며 동적인 크기로 생성되는, 클래스 기반으로 만들어지는 인스턴스 입니다. 변수에 직접적인 값이 저장되는 것이 아니라 힙영역에 값이 저장되면 해당 주소값과 연결해서 사용하게 됩니다. 따라서 다음과 같은 차이가 존재합니다.

~~~java
int num1 = 1;
int num2 = num1;

num1 = 2;

System.out.println(num1); //2
System.out.println(num2); //1
~~~

원시타입은 변수에 값이 저장되기 때문에 다른 변수로 값을 복사하게 되면 값 자체가 복사되게 됩니다 따라서 위의 예제 처럼 num1을 num2에 할당하고 num1의 값을 바꾸게 되면 서로 다른 값이 되는 것입니다. 하지만 참조 타입의 경우는 다릅니다.

~~~java
public class Main {

    public static void main(String[] args) {

        Person p1 = new Person("sam");
        Person p2 = p1;

        p1.name = "park";

        System.out.println(p1.name); //park
        System.out.println(p2.name); //park

    }

}

class Person {

    String name;

    public Person(String name) {
        this.name = name;
    }
}
~~~

메인 메소드에서 p1이라는 인스턴스를 만들고 p2 변수에 p1를 할당했습니다. Person이라는 클래스는 name이라는 필드를 가지고 있어서 sam이라는 값을 넣어 주었는데 p2에 복사한 후 p1의 name을 park로 변경하게 되면 두 변수 모두 값이 park로 바뀌게 됩니다. 변수에 실제 값이 아닌 주소값이 저장되어 있어 Person 인스턴스가 공유되어지기 때문입니다. 이 것을 얕은 복사(Shallow Copy)라고 합니다. 얕은 복사를 하게 될 경우 위와 같이 하나의 인스턴스를 변경하게 되면 다른 변수에도 영향을 미치기 때문에 사용시 주의가 필요합니다. 따라서 깊은 복사(Deep Copy)를 이용하거나 새로운 인스턴스를 생성해 주는 것이 좋습니다. 여기서 깊은 복사란 주소값을 복사하는 것이 아닌 인스턴스 자체를 복사하여 다른 주소값을 서로 저장하는 것을 말합니다.

#### 새로운 인스턴스 생성
~~~java
Person p1 = new Person("sam");
Person p2 = new Person("sam");

p1.name = "park";

System.out.println(p1.name); //park
System.out.println(p2.name); //sam
~~~

#### 깊은 복사
깊은 복사를 사용할 클래스를 Cloeable 인터페이스를 상속 받도록 하고 clone 함수를 재 정의합니다.
~~~java
public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {

        Person p1 = new Person("sam");
        Person p2 = p1.clone();

        p1.name = "park";

        System.out.println(p1.name); //park
        System.out.println(p2.name); //sam

    }

}

class Person implements Cloneable {

    String name;

    public Person(String name) {
        this.name = name;
    }

    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }
}
~~~

## 리터럴

리터럴이란 데이터 그 자체를 말합니다. 변수에 값을 할당할 때 객체를 생성하는 것이 아닌 값 그 자체를 넣는 것입니다.

~~~java
int num = 1;

String name = "sam"

int[] arr = {1, 2, 3, 4, 5};
~~~

위의 예제와 같이 값을 변수에 할당할 때 값 자체를 넣게 되는데 그 때 넣어지는 것이 바로 리터럴입니다. 원시 타입은 모두 리터럴로 선언하고 참초 타입의 경우에는 래퍼 타입이나, 스트링 타입, 배열을 리터럴로 선언할 수 있습니다.

## 변수 선언 및 초기화하는 법

변수는 값을 다룰 때, 여러번 사용할 수 있게 하거나 그 값에 의미를 부여하고자 할 때 사용합니다. 값이 변할 수 있기 때문에  변수에 값을 재할당할 수도 있습니다.

~~~java
int num; //변수 선언
num = 1; //변수 초기화

int num2 = 2; //변수 선언 및 초기화

num2 = 7; //재할당
~~~

변수를 선언한 후 초기화하거나 변수를 선언하면서 초기화할 수 있으며, 초기화한 후에도 값은 재할당 될 수 있습니다. 반면 상수라는 것도 존재하는데 상수는 초기화한 후 변경할 수 없는 값을 말합니다 자바에서는 `final` 키워드를 통해 상수를 만들 수 있습니다.

~~~java
final int num = 1;
num = 2;//error

final int num2;//error
~~~

한 번 선언하면 변할 수 없기 때문에 메서드 내에서는 변수를 선언함과 동시에 값을 바로 할당해주어야 합니다. 여기서 메서드 내에서는 이라는 전제가 붙었는데요. 그 이유는 인스턴스 변수의 경우에는 생성시에 바로 초기화하지 않고 생성자나 인스턴스 블록을 통해 초기화할 수 있기 때문입니다.

~~~java
class MyClass {
    final int num;
    final String str;

    {
        num = 1; //인스턴스 블록을 통한 상수 초기화
    }

    MyClass() {
        this.str = "안녕하세요"; //생성자를 통한 상수 초기화
    }
}
~~~

물론 이 경우에도 한 번 값을 초기화 하게 되면 값은 변경될 수 없다는 점은 같습니다.

## 변수의 스코프와 라이프타임

변수는 클래스의 필드와 메서드 내부, 블럭에서 사용이 가능합니다. 각각의 경우는 스코프와 라이프타임이 모두 다른데 예제를 통해서 알아보겠습니다.

~~~java
class MyClass {
    int num1 = 1;

    void method1() {
        num1 = 2;

        int num2 = 3;

        {
            num2 = 4;
            int num3 = 5;

            System.out.println(num1); //ok
            System.out.println(num2); //ok
            System.out.println(num3); //ok
        }

        System.out.println(num1); //ok
        System.out.println(num2); //ok
        System.out.println(num3); //error
    }

    void method2() {
        System.out.println(num1); //ok
        System.out.println(num2); //error
        System.out.println(num3); //error
    }
}
~~~
총 세 개의 변수가 선언이 되었는데요. 출력을 해보면서 변수 들의 라이프타임과 스코프를 한 번 알아보도록 합시다.

* num1: 클래스의 필드로 선언이 되게 되면 그 클래스 내부의 모든 메서드에서 사용이 가능합니다. 따라서 `method1`에서 재할당을 할 수 있고 `method1`, `method2` 모두 출력이 가능합니다.
* num2: 메서드 내부에 선언된 변수는 해당 메서드(`method1`)에서 사용이 가능합니다. 따라서 메서드 내부의 블록 영역(if, while 등)에서 재할당이 가능하고 `method1` 내부 어디서든지 사용할 수 있습니다. 하지만 다른 메서드(`method2`)에서는 사용이 불가능합니다.
* num3: 메서드 내부의 블럭에서 선언되었기 때문에 해당 블럭에서만 사용 가능하며 블럭을 빠져나오게 되면 사용할 수 없습니다.

이와 같이 자바는 블록에 따라 범위가 정해지기 때문에 상위 블록에서 하위 블록으로의 참조는 가능하지만 반대의 경우는 참조가 불가능하다는 점을 숙지하고 코딩을 하면 될 것 같습니다.

## 타입 변환, 캐스팅 그리고 타입 프로모션

### 1. 캐스팅
다른 타입으로 변환을 할 때는 캐스팅(Casting)을 해주어야 합니다. 캐스팅은 업 캐스팅(Up Casting)과 다운 캐스팅(Down Casting)이 존재합니다. 업 캐스팅이란 히위 클래스의 인스턴스를 상위 클래스의 인스턴스로 변환하는 것을 말합니다. 반대로 다운  캐스팅은 상위 클래스의 인스턴스를 하위 클래스의 인스턴스로 변환하는 것을 말합니다. 전자는 따로 타입을 명시해줄 필요는 없고(묵시적 형변환), 후자는 꼭 명시를 해 주어야 합니다(명시적 형변환). 다만 업 캐스팅시에는 상위 타입으로 캐스팅이 되기 때문에 하위 클래스에서 추가한 메서드는 사용할 수 없다는 점을 주의해야 합니다.

~~~java
//업캐스팅. 할당시 타입을 명시해주지 않아도 된다.(묵시적 형변환)
Integer a = 1;
Object o = a;

o.byteValue(); //error

//다운캐스팅. 할당시 타입을 변수 앞에 명시해 주어야 한다.(명시적 형변환)
Object o = "hello";
String s = (String) o;
~~~

### 타입 변환

타입 변환은 boolean형을 제외한 원시 타입에서 타입을 서로 변경하는 것을 말합니다. 정수 타입이나 실수 타입끼리 변환시에는 크기가 작은 타입을 큰 타입으로 넣을 땐 자동으로 변환이 되지만(묵시적 형변환) 반대의 경우에는 명시를 해주어야 합니다(명시적 형변환). 또한 정수 타입에서 실수 타입으로 변환시에는 자동 변환이 되지만 반대의 경우에는 명시를 해주어야 합니다.

~~~java
//작은 타입에서 큰 타입으로 변환하기 때문에 따로 타입을 명시해주지 않아도 됨(묵시적 형변환)
int i = 1;
long l = i;

//큰 타입에서 작은 타입으로 변환하기 때문에 타입을 명시해주어야 함(명시적 형변환)
double d = 3.0;
float f = (float) d;

//실수 -> 정수 (명시적 형변환) 소숫점 이하를 잃게 됨
double d = 3.0;
int i = (int) d;

//정수 -> 실수 (암묵적 형변환)
long l = 5L;
float = l;
~~~

다만, 상위 타입에서 하위 타입이나 실수 타입에서 정수 타입으로 변경할 경우 데이터를 손실할 수 있으니 주의해서 사용해야 합니다.

~~~java
long l = Long.MAX_VALUE; //9223372036854775807
int i = (int) l;
System.out.println(i); //-1

double d = 3.23532;
int i = (int) d; //3
~~~

### 타입 프로모션

타입 프로모션이란 산술 연산을 할 때 큰 타입과 작은 타입이 있다면 결과를 큰 타입으로 반환(승격)되는 것을 말합니다.

~~~java
int a = 2;
double b = 2.5;

double c = a * b;
~~~

## 1차 및 2차 배열 선언하기

배열은 두 가지 선언 방법이 있습니다. 첫 번째는 길이만 정하는 방법, 두 번째는 값을 초기화함과 동시에 선언하는 방법입니다. 전자의 경우는 모든 값이 각 자료형의 기본 값으로 초기화가 됩니다.
또한 초과된 인덱스에 할당하려고 하면 ArrayIndexOutOfBoundsException이 발생합니다.
~~~java
//10의 길이를 가진 배열 선언
int[] arr1 = new int[10];
arr1[0] = 1;
arr1[10] = 1; //ArrayIndexOutOfBoundsException 발생

//리터럴 선언 방식: 값을 할당하고 값의 개수만큼 길이가 정해짐
int[] arr2 = {1, 2, 3, 4, 5};
int[] arr3 = new int[] {1, 2, 3, 4, 5};
~~~

위와 같이 후자의 경우는 다시 두 가지로 나뉩니다. 첫 번째 방식은 재할당이 불가능하고 두 번째 방식은 재할당이 가능합니다. 다만 각 인덱스의 값을 재할당하는 것은 모두 가능합니다.

~~~java
int[] arr;
arr = {1, 2, 3, 4, 5}; //error
arr = new int[] {1, 2, 3, 4, 5}; //ok
~~~

### 2차원 배열 선언

2차원 배열의 선언 방법은 1차원 배열의 선언 방법과 똑같습니다만 차원의 수만큼 개수를 정해주어야 합니다. 앞에 선언된 숫자는 행의 개수이며 뒤에 선언된 숫자는 열의 개수입니다.

~~~java
int[][] arr = new int[5][10];
arr[0][0] = 1;
arr[0][10] = 1; //ArrayIndexOutOfBoundsException 발생
~~~

2차원 배열도 각각의 행을 재할당할 수 있습니다.

~~~java
int[][] arr = new int[5][10];
arr[0] = new int[10];
arr[1] = {1, 2, 3, 4, 5};
arr[2] = new int[] {1, 2, 3, 4, 5};
arr[5] = new int[10]; //ArrayIndexOutOfBoundsException 발생
~~~

또한 리터럴 형태로도 선언 가능합니다.

~~~java
int[][] arr = {
    {1, 2, 3, 4, 5},
    {6, 7, 8, 9, 10},
    {11, 12, 13, 14, 15}
};
~~~

여담으로 자바에서는 배열을 선언시 보통 타입 뒤에 `[]`를 붙이는 게 일반적이지만 변수 뒤에 붙여도 사용하는 것엔 아무 지장이 없습니다. 하지만 협업시 통일성을 위해 자바스타일로 선언을 하는 것이 좋으리라 생각됩니다.

~~~java
int[] arr = new int[10]; //자바 스타일
int arr[] = new int[10]; //C 스타일
~~~

## 타입 추론, var

### 타입 추론
타입 추론이란 개발자가 타입을 따로 명시하지 않아도 컴파일러가 스스로 타입을 추론하는 것을 말합니다. 제네릭에서의 다이아몬드 지시자(`<>`)나 람다에서의 타입 생략 등이 이에 해당합니다.

~~~java
//다이아몬드 지시자
Map<String, Object> map = new HashMap<>();

//람다식 타입 추론
Comparator<Integer> comparator = (a, b) -> {
    if (a > b) {
        return 1;
    }
    return -1;
};
~~~

### var

var는 JDK10에서 도입된 타입을 추론하는 타입입니다. 다시 말해 개발자가 변수 선언시 타입을 var로만 선언해 주어도 할당되는 타입에 따라 타입이 정해지는 방식입니다. 이는 똑같은 타입을 변수 선언과 할당시에 두 번할 필요가 없다는 뜻입니다.

~~~java
var i = 3;
var str = "hello";
var b = true;

var arr = str.toCharArray(); //타입을 선언해 주지 않아도 해당 타입의 메서드 사용 가능

ArrayList<Integer> list = new ArrayList<>(); //일반적인 변수 선언

var list = new ArrayList<Integer>(); //var를 사용한 변수 선언
~~~

다만 컴파일시에는 변수 타입이 해당 타입으로 변경이됩니다. 즉 런타임시 동적으로 추론하는 것이 아닌 컴파일 타임에 var를 해당 타입으로 변경한 후 실행하는 것입니다.

#### decompile
~~~java
int i = true;
String str = "hello";
boolean b = true;
char[] arr = str.toCharArray();
~~~

### 참고

https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html  
https://techdifferences.com/difference-between-type-casting-and-type-conversion.html
