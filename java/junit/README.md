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
