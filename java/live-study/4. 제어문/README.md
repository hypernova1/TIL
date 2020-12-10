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

