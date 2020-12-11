# 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

대부분의 클래스는 하나 이상의 클래스에 의존한다. 예를 들어 맞춤법 검사기의 경우에는 사전에 의존하게 되는데, (여기서 사전을 의존성이라 한다) 이런 클래스를 정적 유틸리티 클래스나 싱글턴으로 구현하는 경우가 많다.

#### 정적 유틸리티 클래스 - 유연하지 않고 테스트하기 어렵다.
~~~java
public class SpellChecker {
    private static final Lexicon dictionary = new EnglishDictionary();

    private SpellChekcker() {}

    public static boolean isValid(String word) { ... }

    public static List<String> suggestions(String typo) { ... }
}
~~~

#### 싱글턴 - 유연하지 않고 테스트하기 어렵다.
~~~java
public class SpellChecker {
    private final Lexicon dictionary = new EnglishDictionary();

    public static SpellChecker INSTANCE = new SpellChecker();

    private SpellChekcker() {}

    public static boolean isValid(String word) { ... }

    public static List<String> suggestions(String typo) { ... }
}
~~~

두 경우 모두 단 하나의 사전만을 사용할 수 있다는 점에서 유연하지 않다. 또한 사전의 경우에는 어휘용 사전도 필요할수 있으며, 테스트용 사전이 필요할 수도 있다. 여러 의존성이 필요할 경우에는 `dictionary` 필드의 `final` 한정자를 제거하고 다른 사전으로 교체하는 메서드를 추가할 수 있지만 이 방식은 오류가 발생하기 쉬우며(인스턴스 생성 시점에 완전하지 않기 때문에) 멀티스레드 환경에서는 사용할 수 없다. 따라서 사용하는 자원(의존성)에 따라 동작이 달라지는 **클래스를 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.**

클래스(SpellChecker)가 여러 자원 인스턴스를 지원하며, 클라이언트가 원하는 자원(dictionary)를 사용하려면 **인스턴스를 생성시 생성자에 필요한 자원을 넘겨주는 방식**을 쓰면 된다. 이를 의존 객체를 주입했다고 표현하며, 맞춤법 검사기를 사용할 때 의존 객체인 사전을 주입해주면 된다.

~~~java
public class SpellChecker {
    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = dictionary;
    }

    public static boolean isValid(String word) { ... }

    public static List<String> suggestions(String typo) { ... }
}
~~~

~~~java
Lexicon englishDictionary = new EnglishDictionaty();
Lexicon koreanDictionary = new EnglishDictionaty();

SpellChecker englishChecker = new SpellChecker(englishDictionary);

SpellChecker koreanChecker = new SpellChecker(koreanDictionary);
~~~

위의 예에서는 단 하나의 의존성만을 주입했지만 여러 의존성을 주입해도 잘 작동한다. 의존성 주입은 생성자나 정적 팩터리, 빌더에 모두 사용할 수 있다.

이 패턴의 변형으로 생성자에 자원 팩터리를 넘기는 방식이 있다. 팩터리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다. 즉 팩터리 메서드를 구현한 것이다. `Supplier<T>` 인터페이스가 팩터리를 표현한 예이며 `Supplier<T>`를 입력으로 받는 메서드는 일반적으로 한정적 와일드 타입(bounded wildcard type)을 사용하여 팩터리의 타입 매개변수를 제한해야 한다. 이 방식을 사용해 명시한 타입이라면 무엇이든 생성할 수 있는 팩터리를 넘길 수 있다. 아래의 예제는 매개변수에 상위 타입 제한자(`<? extends T>`)를 사용하여 Tile을 상속받은 클래스만 받을 수 있도록 제한하였다(상위 클래스 제한).

~~~java
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
~~~

의존 객체의 주입이 유연성과 테스트 용이성을 개선하긴 하지만 의존성이 많아질수록 관리하기 힘들어진다. 이때 스프링과 같은 의존성 주입을 자동으로 해주는 프레임워크를 사용하면 된다.

정리하자면 클래스가 내부적으로 하나 이상의 자원에 의존한다면 정적 유틸리티 클래스나 싱글턴은 사용하지 않는 것이 좋다. 자원들을 내부에서 생성하게 해도 안되며 대신 필요한 자원이나 자원을 생성하는 팩터리를 생성자 또는 정적 팩터리 등에 넘겨서 사용하자. **의존 객체 주입 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 개선해준다.**

