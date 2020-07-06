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
    
    * 예상시간이 넘었을 때 즉시 종료하고 싶다면 사용



## 조건에 따라 테스트 실행

~~~java
@Test
void test() {
	String testEnv = System.getenv("TEST_ENV");
	System.out.println(testEnv);
	assumeTrue("LOCAL".equalsIgnoreCase(test_env)); //조건이 맞지 않으면 다음 줄이 실행되지 않고 종료된다.
	//...
}

@Test
void test2() {
	assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> { //조건이 맞으면 람다식이 실행된다.
  	//...
	});  
}

@EnabledOnOs(OS.MAC) //운영체제가 맥OS일 때만 실행
void test3() {
  //...
}

@DisabledOnOs(OS.MAX) //운영체제가 맥OS가 아닐 때 실행
void test3() {
  //...
}
~~~

### 태깅, 필터링

테스트를 항상 실행시키지 않고 특정 로컬이 아닌 배포서버에서만 실행 시킨다거나 테스트 들을 그룹화 해서 특정 테스트 그룹만을 실행시키고 싶다면 `@Tag` 애노테이션을 사용할 수 있다.

~~~java
@Test
@Tag("one")
void test() {
  //...
}

@Test
@Tag("two")
void test2() {
  //..
}
~~~

#### `pom.xml` 에 설정 추가

~~~xml
<profiles>
	<profile>
  	<id>default</id>
    <activation>
    	<activeByDefault>true</activeByDefault>
    </activation>
    <build>
    	<plugins>
      	<plugin>
        	<artifactId>maven-surefilre-plugin</artifactId>
          <configuration>
          	<group>one</group>
          </configuration>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>
~~~

* profile이 default일 때 `@Tag("one")`이 붙어 있는 테스트 함수만 실행한다.

#### maven 명령어로 실행

~~~
$ mvn test -P one
~~~

## 커스텀 태그

#### 커스텀 태그 정의

~~~java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("one")
public @interface OneTest {
  //...
}
~~~

#### 사용하기

~~~java
@Test
@OneTest
void test() {
  //...
}
~~~

* 위와 같이 작성하게 되면 `@Tag("one")` 으로 사용할 때보다 Type Safe하게 할 수 있다.(오타로 인한 에러 방지)

## 테스트 반복하기

~~~java
@DisplayName("테스트테스트")
@RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepitition}") //반복 횟수. 테스트 이름 설정
void test(RepetitionInfo repeatInfo) {
  System.out.println(
    repetitionInfo.getCurrentRepetition() + "/" + 
    repetitionInfo.getTotalRepetitions()
  ); //반복된 횟수 / 총 반복 횟수
}
              
@ParameterizedTest
@ValueSource(strings = {"이제", "공부만이", "살 길이다!"}) //파라미터의 개수만큼 반복
void test2(String message) {
  System.out.println(message);
}
~~~

기타 애노테이션

* `@EmptySource`: 빈 값을 가지는 파라미터 추가
* `@NullSource`: null 값을 가지는 파라미터 추가
* `@NullAndEmptySource`: 빈 값, null 값을 가지는 파라미터 추가

## 테스트 인스턴스

기본적으로 테스트 간의 의존성을 제거하기 위해(독립적인 실행을 위해) 테스트마다 매번 새로운 인스턴스가 생성된다. 그래서 인스턴스 변수를 활용한다고 해도 값은 계속 초기화 된다.

~~~java
class Test {
  int value = 0;
  
  @Test
  void test() {
    System.out.println(value++);
  }
  
  @Test
  void test2() {
    System.out.println(value++);
  }
  
  @Test
  void test3() {
    System.out.println(value++);
  }
}
~~~

* 위의 모든 함수는 0을 출력한다.

#### 테스트 인스턴스 기본 전략 변경

* @TestInstance(LifeCycle.PER_CLASS): 클래스 마다 인스턴스 생성

## 테스트 순서

순서가 필요한 통합 테스트를 하고 싶을 땐 테스트의 순서를 정하여 순서대로 실행시킬 수 있다.

* OrderAnnotation
* Alphanumeric
* Random

~~~java
//@Order 애노테이션을 통해 순서를 정한다. (spring이 제공하는 애노테이션이 아닌 junit이 제공하는 애노테이션 사용)
@TestMethodOrder(OrderAnnotation.class)
class Test {
  
  @Order(2)
  @Test
  void test() {
    System.out.println("test");
  }
  
  @Order(1)
  @Test
  void test2() {
    System.out.println("test2");
  }
  
  @Order(3)
  @Test
  void test3() {
    System.out.println("test3");
  }
}
~~~

