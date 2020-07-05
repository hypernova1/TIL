# JUnit

자바 개발자가 가장 많이 사용하는 테스팅 프레임워크

## JUnit 5

#### 의존성 추가 방법

스프링 부트(2.2+) 프로젝트를 만들거나, 아래와 같이 직접 의존성 추가

#### gradle 의존성 추가

~~~
testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '5.6.2'
~~~

#### maven 의존성 추가

~~~
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
~~~



* 자바 8 이상이 필요함
* JUnit Platform
  * 테스트를 실행해주는 런처 제공
  * Test Engine API 제공
* Jupiter
  * Test Engine API 구현체
  * JUnit 5 제공
* Vintage
  * JUnit3, 4를 제공하는 Test Engine 구현체



## JUnit 5 시작하기

#### Annotation 설명

* `@Test`

  * 이 애노테이션이 붙은 함수가 테스트 함수가 됨

  * ~~~java
    @Test
    void test() {
      //...
    ~~~

* `@BeforeAll`

  * 모든 테스트가 실행되기 전 한 번만 실행 됨

  * 정적 메소드로 선언해야 함

  * ~~~java
    @BeforeAll
    static void beforeAll() {
      //...
    ~~~

* `@AfterAll`

  * 모든 테스트가 끝나면 실행 됨

  * 정적 메소드로 선언해야 함

  * ~~~java
    @AfterAll
    static void afterAll() {
      //...
    ~~~

* `@BeforeEach`

  * 각각의 테스트가 실행되기 전 실행 된다.

  * ~~~java
    @BeforeEach
    void beforeEach() {
      //...
    ~~~

* `@AfterEach` 

  * 각각의 테스트가 실행된 후 실행 된다.

  * ~~~java
    @AfterEach
    void afterEach() {
      //...
    ~~~

* `@Disabled`
  
  * 전체 테스트 실행시 무시된다.

### 테스트 이름 표시하기

함수명으로 테스트를 설명하기에는 조금 부족할 수도 있다. 그래서 아래와 같은 애노테이션을 사용하여 해당 테스트가 무엇을 테스트하는지 정확하게 명시해 줄 수 있다.

* `@DisplayNameGeneration`

  * 클래스, 메서드에 사용 가능

  * 클래스 내의 모든 메서드에 적용 됨

  * ~~~java
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    public class Test {
      //...
    ~~~

* `@DisplayName`

  * 메서드에 사용 가능

  * ~~~java
    @Test
    @DisplayName("테스트 만들기")
    void test() {
      //...
    ~~~



## Assertion

해당 값이 예상과 같은지 테스트하는 함수 모음을 제공한다. 모든 함수는 마지막 인자에 실패시 표시할 메시지를 정의할 수 있다.

* `assertNotNull(actual)`
  * 값이 null이 아닌지 확인
  * ~~~java
    assertNotNull(study);
    ~~~

* `assertEquals(expected, actual)`
  * 값이 기대한 값과 같은지 확인
  * ~~~java
    //직접 스트링 입력
    assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디 처음 만들면 상태값이 DRAFT 이어야 한다.");
    //Supplier 사용
    assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
      @Override
      public String get() {
        return "스터디를 처음 만들면 상태값이 DRAFT 이어야 한다."
      }
    );
    //람다식으로 줄이기
    assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디 처음 만들면 상태값이 DRAFT 이어야 한다.");
    ~~~

* `assertTrue(boolean)`
  * 조건이 참인지 확인
  * ~~~java
    assertTrue(isActive);
    ~~~

* `assertAll(executables...)`
  * 모든 확인 구문 확인
  * assertion 함수들은 이전 테스트가 실패하면 더 이상 실행되지 않아 다음 테스트의 성공 여부를 알 수 없지만 assertAll은 모든 함수의 결과를 반환한다.
  * ~~~java
    assertNotNull(study);
    assertEquals(StudyStatus.DRAFT, study.getStatus());//실패!
    assertTrue(isActive);//실행되지 않음
    
    assertAll(
    	() -> assertNotNull(study),
      () -> assertEquals(StudyStatus.DRAFT, study.getStatus()),
      () -> assertTrue(isActive)
    );
    ~~~

* `assertThrows(expectedType, executable)`
  * 예외 발생 확인
  * ~~~java
    assertThorws(IlligalArgumentException.class, () -> new Study(-10));
    ~~~

* `assertTimeout(duration, executable)`
  * 특정 시간 안에 실행이 완료 되는지 확인
  * ~~~java
    assertTimeout(Dureation.ofMillis(100), () -> {
      new Study());
      Thread.sleep(10 * 1000);
    });
    ~~~

  * 실제로 테스트가 종료되기 까지 기다리기 때문에 테스트 로직이 10초가 걸린다고 하면 10초(이상)동안 기다려야된다는 단점이 있다.
  * `assertTimeoutPreemptively`
    * 예상시간이 넘었을 때 바로 바로 종료하고 싶다면 사용

