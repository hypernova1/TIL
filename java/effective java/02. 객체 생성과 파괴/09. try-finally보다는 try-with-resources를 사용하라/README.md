# try-finally보다는 try-with-resources를 사용하라

`InputStream`, `OutputStream`, `java.sql.Connection`과 같은 라이브러리는 사용 후 `clase` 메서드를 호출하여 자원을 직접 닫아줘야 한다. 자원 닫기는 놓치기 쉬운 부분이라 놓친다면 예측할 수 없는 성능 문제로 이어질 수 있다. 이런 문제를 해결하기 위해 `finalizer`를 활용하고 있지만 예측이 불가능하기 때문에 믿을만 하지 못하다.

## try-finally

자바 7 이전에는 다 쓴 자원을 닫기 위해서 `try-finally`를 사용했다.

~~~java
static String firstLineOfFile(String path) throws IOException {
    BufferedReader br = new BufferedReacer(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
~~~

닫아주어야 하는 자원이 하나일 땐 코드가 복잡해 보이지 않다. 하지만 자원을 하나 이상 사용한다면 어떻게 될지 살펴보자.

~~~java
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
~~~

위의 코드는 결점이 있다. 예를 들어 기기에 물리적 문제가 생긴다면 `firstLineOfFile` 메서드 안의 `readLine` 메서드가 예외를 던지고, 같은 이유로 `close` 메서드도 실패를 할 것이다. 이런 상횡이라면 두 번째 예외가 첫 번째 예외를 삼켜버리기 때문에 스택 추적 내역에 첫 번째 예외에 관한 내용은 남지 않게 되어 디버깅이 어려워 진다. 두 번째 `copy` 메서드의 경우에는 `try-finally` 안에 중첩으로 `try-finally`가 선언되어 있기 때문에 코드가 매우 더러워 보인다. 이 경우도 `firstLineOfFIle` 메서드의 문제점이 똑같이 발생할 것이다.

## try-with-resources

자바 7부터 `try-with-resource`가 생기면서 위의 문제는 모두 해결되었다. 단 `try-with-resources`를 사용하려면 해당 자원이 `AutoCloseable` 인터페이스를 구현해야 한다. 해당 인터페이스는 아무 반환 값이 없는 `close` 메서드만 정의되어 있다. 많은 자바 라이브러리와 서드파티 라이브러리들은 `AutoCloseable`을 구현하거나 확장해뒀고, 우리도 개발을 할 때 자원을 뜻하는 클래스를 작성한다면 `AutoCloseable`을 반드시 구현해야 한다.

위의 `copy` 메서드를 `try-with-resources`로 바꾸면 아래외 같다. 이경우 `try-with-resources` 블록을 벗어나면 알아서 `close`를 호출하기 때문에 코드가 더 짧아졌다.

~~~java
static void copy(String src, String dst) throws IOException {
    try (InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new Byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) >= 0) {
            out.write(buf, 0, n);
        }
    }
}
~~~

기존의 `firstLineOfFile` 메서드는 `readLine`과 `close` 메서드 호출 양쪽에서 예외가 발생한다면 `close`에서 발생한 예외는 숨겨지고 `readLine`에서 발생한 예외가 기록된다. 이처럼 예외가 여러개 발생해도 하나만 기록되고 나머지 예외는 숨겨질 수 있다. 물론 이렇게 숨겨진 예외들도 그냥 버려지진 않고 '숨겨졌다(suppressed)'는 꼬리표를 달고 출력된다. 또한 자바 7에서 `Throable`에 추가된 `getSuppressed` 메서드를 사용하여 숨겨진 예외를 가져올 수도 있다.

## try-with-resources와 catch

`try-with-resources`도 `try-finally` 처럼 `catch`절을 사용할 수 있다. `catch` 절 덕분에 `try`문을 더 중첩하지 않고도 다수의 예외를 처리할 수 있다.

~~~java
static String firstLineOfFile(String path) throws IOException {
    
    try (BufferedReader br = new BufferedReacer(new FileReader(path))) {
        return br.readLine();
    } catch {
        return defaultValue;
    }
}
~~~