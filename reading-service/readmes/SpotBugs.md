# SpotBugs Maven Plugin
Плагин для статического анализа кода в Java-проектах. Он интегрирует инструмент SpotBugs с Apache Maven, позволяя разработчикам 
автоматически обнаруживать и сообщать о потенциальных ошибках и проблемах с качеством кода в процессе сборки.

---

## Требования

Для данного проекта используется плагин версии 4.8.6

---

## Использование

Добавьте spotbugs-maven-plugin в блок `plugins` в файле [POM.xml](../pom.xml)

```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>${spotbugs-maven-plugin.version}</version>
    <dependencies>
        <dependency>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs</artifactId>
            <version>${spotbugs.version}</version>
        </dependency>
    </dependencies>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <effort>Max</effort>
        <threshold>Medium</threshold>
        <xmlOutput>true</xmlOutput>
        <failOnError>false</failOnError>
    </configuration>
</plugin>
```

Плагин срабатывает на фазе `compile` жизненного цикла сборки проекта, поэтому при запуске используйте команду не ниже `mvn clean compile`
После выполнения отчета можно сгенерировать полный отчет командой `mvn site`, после чего открыть в браузере файл `index.html` находящийся по пути `target/site/index.html`
Так же можно отдельно вызвать удобный графический интерфейс с помощью команды `mvn clean compile spotbugs:gui`

---

## Настраиваемые параметры в секции configuration:

`<effort>` - используется для настройки уровня усилий, которые SpotBugs будет прикладывать для анализа вашего кода, может принимать одно из следующих значений:
- **`Min`** (минимум): Минимальный уровень усилий. Анализ будет выполнен быстрее, но с меньшей глубиной.
- **`Default`** (по умолчанию): Стандартный уровень усилий. Это значение по умолчанию, которое обеспечивает баланс между временем выполнения и глубиной анализа
- **`Max`** (максимум): Максимальный уровень усилий. Анализ будет более тщательным и глубоким, но займет больше времени.

`<threshold>` - используется для настройки порога серьезности ошибок, которые будут включены в отчет, может принимать одно из следующих значений:
- **`low`** (низкий): Включает все ошибки, включая наименее серьезные.
- **`medium`** (средний): Включает ошибки средней и высокой серьезности. Это значение по умолчанию, которое позволяет сосредоточиться на более значимых проблемах, игнорируя менее важные предупреждения.
- **`high`** (высокий): Включает только самые серьезные ошибки.