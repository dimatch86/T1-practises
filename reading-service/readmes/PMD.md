# PMD Maven Plugin
Плагин для Apache Maven, который интегрирует PMD в процесс сборки вашего проекта. 
Он позволяет анализировать ваш код на наличие потенциальных ошибок, плохих практик и нарушений код-стиля. 

---

## Требования

Для данного проекта используется плагин версии 3.15.0

---

## Использование

Добавьте pmd-plugin в блок `plugins` в файле [POM.xml](../pom.xml)

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>${pmd-plugin.version}</version>
    <configuration>
        <rulesets>
            <ruleset>rulesets/java/basic.xml</ruleset>
            <ruleset>rulesets/java/braces.xml</ruleset>
            <ruleset>rulesets/java/design.xml</ruleset>
            <ruleset>rulesets/java/unusedcode.xml</ruleset>
        </rulesets>
        <minimumTokens>100</minimumTokens>
        <targetDirectory>./target/custom-pmd-reports</targetDirectory>
        <failOnViolation>false</failOnViolation>
    </configuration>
    <executions>
        <execution>
            <phase>validate</phase>
            <goals>
                <goal>check</goal>
                <goal>cpd-check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Плагин срабатывает на фазе `validate` жизненного цикла сборки проекта, поэтому при запуске используйте команду не ниже `mvn clean validate`
После выполнения проверки, сгенерированный отчет PMD будет расположен в файле `/target/site/pmd.html`, 
а также будет сгенерирован отчет CPD о выявленных дубликатов в коде, расположенный в файле `/target/site/cpd.html`.

---

## Настраиваемые параметры в секции configuration:

`<rulesets>` - определяем какие правила должны быть включены в процесс анализа. Можно добавлять/удалять требуемые правила. 
Детальный список всех правил можно посмотреть в [официальной документации](https://pmd.github.io/pmd/pmd_rules_java.html) 

`<minimumTokens>` - определяет минимальное количество токенов, которые должны совпадать, чтобы фрагменты кода считались дублирующимися. 
Токены — это основные элементы кода, такие как ключевые слова, операторы, идентификаторы и т.д. 

`<targetDirectory>` - позволяет задавать целевую директорию для отчетов

`<failOnViolation>` - определяет, будет ли сборка Maven завершаться с ошибкой, если будут обнаружены нарушения правил PMD 