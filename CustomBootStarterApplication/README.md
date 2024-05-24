<a id = "top"></a>
# Custom Rest-Intercepting Starter

---
Spring Boot Starter, который предоставляет возможность логировать HTTP запросы в приложении на базе Spring Boot.
Логирование включает в себя метод запроса, URL, заголовки запроса и ответа, код ответа, время обработки запроса.
Механизм перехвата и логирования HTTP запросов реализован с помощью интерцепторов Spring и logback.xml.

---
## Локальный запуск

### Требования

Проект использует синтаксис Java 17.

---
### Запуск приложения в среде разработки

Для работы со стартером вам понадобится установить его в локальный maven-репозиторий. Откройте проект `HttpInterceptingStarter` в среде разработки и выполните команду mvn clean install. 
После этого стартер можно подключить в ваш проект с помощью зависимости maven:

```
   <dependency>
      <groupId>com.t1</groupId>
      <artifactId>http-interceptoring-starter</artifactId>
      <version>1.0-SNAPSHOT</version>
   </dependency>
```
Вместе со стартером в папке AbstractApp находится тестовое приложение с подключенным стартером, откройте его в среде разработки и запустите из класса
[AbstractApp/src/main/java/com/t1/loggableapplication/LoggableApplication.java](AbstractApp/src/main/java/com/t1/loggableapplication/LoggableApplication.java).
После запуска приложение будет доступно на порту http://localhost:8080.

---
## Настраиваемые параметры

Настройки стартера должны быть определены в файле [application.yml](AbstractApp/src/main/resources/application.yml) вашего приложения.

```
rest-interceptor:
  enable: true
  logger:
    destination: FILE
    path: logs/
    filename: myfile
    level: info
```
Описание параметров:

`rest-interceptor.enable` - включает и отключает стартер в зависимости от значения `true` или `false`;

`rest-interceptor.logger.destination` - выбор куда писать логи, в файл или в консоль;

`rest-interceptor.logger.path` - позволяет задать путь сохранения файла с логами;

`rest-interceptor.logger.filename` - позволяет задать произвольное название для файла;

`rest-interceptor.logger.level` - позволяет выбрать уровень логирования


---
## Описание запросов тестового приложения

В корне проекта в папке 'rest-examples' находится [файл](rest-examples/T1.postman_collection.json) с коллекцией запросов, которую можно импортировать в Postman.
Описание запросов:

- http://localhost:8080/app/addProduct - добавить продукт, в теле запроса передается объект с полями "name" и "price"
- http://localhost:8080/app/getProduct?name=milk - получить продукт по его названию
- http://localhost:8080/app/addProducts - добавить список продуктов, в теле запроса передается массив объектов с полями "name" и "price"
- http://localhost:8080/app/getProducts?price=10 - получить продукты со стоимостью больше 10

[В начало](#top)
