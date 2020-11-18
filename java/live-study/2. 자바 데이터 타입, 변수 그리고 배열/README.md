# 자바 데이터 타입, 변수 그리고 배열

## 데이터 타입이란

모든 프로그래밍 언어는 값을 재사용할 수 있도록 변수에 담아 사용하게 됩니다. 하지만 값은 다음과 같이 여러 종류로 표현할 수 있습니다.

* 안녕하세요
* 24
* 3.14
* true
* ...

이 외에도 프로그래밍에서는 리터럴 값이 아닌(위에 표현된 상수 들을 프로그래밍에서는 리터럴이라 표현합니다)를 변수에 담아야 할 때도 있고, 같은 타입의 값 여러개를 담아야 할 때도 있습니다. 따라서 값을 구분지어 사용할 필요가 있습니다. 바로 데이터 타입을 통해서 말이죠.

## 정적 타입 언어, 동적 타입 언어

정적 타입 언어란 변수 선언시 타입을 직접적으로 명시해 주어야 하는 언어를 말합니다. C나 C++, 자바 등이 바로 정적 타입 언어입니다. 컴파일시 타입이 체크되며 값이 타입에 맞지 않으면 컴파일 에러가 발생합니다.

~~~java
int num = 1; //ok
String str = "안녕하세요"; //ok
String str2 = 3; //error
~~~

컴파일시 체크를 하기 때문에 속도가 빠르고 타입 안정성이 좋습니다. 또한 개발툴에서도 타입을 직접 적으로 알 수 있기 때문에 인텔리센스 지원이 정확합니다.

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

정적 언어 타입은 코딩시 타입을 직접 판단할 수 없기 때문에 인텔리센스 지원이 매우 한정적입니다. 이러한 불편함을 해소하고자 자바스크립트 진영에서는 타입스크립트를 만들어 사용하고 있습니다. 또한 정적 타입 언어는 처음 선언된 타입으로 타입이 고정되는 언어도 존재합니다. 대표적으로 코틀린이 있습니다.

~~~kt

var name = "sam";
name = 1; //error

~~~

자바나 C# 같은 정적 타입 언어에도 var라는 키워드를 통해 동적 타입을 사용할 수 있습니다(자바는 JDK 10부터 사용 가능). 물론 이 두 언어에서는 후자의 동적 타입 방식을 사용합니다.

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

자바는 원시 타입과 참조 타입으로 나뉩니다. 원시 타입은 방금 전 설명했던 것 처럼 고정된 범위를 가지며 자바 메모리 영역 중 Stack 영억에 저장되고 메서드가 종료되면 바로 제거가 됩니다. 반면 참조 타입은 Heap 영역에 저장이 되며 동적인 크기로 생성되는, 클래스 기반으로 만들어지는 인스턴스 입니다. 변수에 직접적인 값이 저장되는 것이 아니라 힙영역에 값이 저장되면 해당 주소값과 연결해서 사용하게 됩니다. 따라서 다음과 같은 차이가 존재합니다.

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


