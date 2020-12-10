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
int point;
if (item.getPrice > 10000) {
    point = item.getPrice * 0.5;
} else if (item.getPrice > 5000) {
    point = item.getPrice * 0.3;
} else {
    point = item.getPrice * 0.1;
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
break는 위의 예처럼 while 블록 다음 라인으로 넘어가게 할 수 있고 조건문이 충첩된다면 `break` 뒤에 `label`를 사용하여 특정 라인으로 돌아가게 만들 수 있습니다. `label`은 모든 반복문에서 사용이 가능합니다.

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

