# MonitoringSystem App

---
Система для сбора и анализа метрик приложения. Состоит из модулей: 

 - MetricsProducer - собирает метрики с помощью библиотеки `spring-actuator` и отправляет их для последующего анализа в модуль MetricsConsumer.
 - MetricsConsumer - принимает метрики от MetricsProducer, сохраняет их в базу данных и выводит информацию о метриках посредством REST-API.
 - MetricsEvent - модуль, содержащий модель передаваемых метрик, используемый в модулях MetricsProducer и MetricsConsumer.

Передача метрик между модулями происходит с помощью брокера сообщений `Apache Kafka`.


---
## Локальный запуск

### Требования

Проект использует синтаксис Java 17. Для локального запуска вам потребуется
Docker Desktop для запуска контейнеров с Kafka и базой данных Postgres.

---
### Запуск приложения в среде разработки

В корне проекта находится файл [docker-compose.yml](docker-compose.yml) c контейнерами Kafka и базой данных PostgreSQL. Так же здесь присутствует контейнер с Kafka-UI для визуального представления работы брокера Kafka.
Откройте файл docker-compose в среде разработки и нажмите на значок запуска напротив строки "services",
или в командной строке перейдите в папку с проектом и запустите командой:

```bash
docker-compose up -d
```

После запуска всех контейнеров запускаем модуль MetricsProducer из класса [MetricsProducerApp.java](metrics-producer/src/main/java/com/example/metricsproducer/MetricsProducerApp.java).
Затем запускаем модуль MetricsConsumer из класса [MetricsConsumerApp.java](metrics-consumer/src/main/java/com/example/metricsconsumer/MetricsConsumerApp.java).

---
## Описание запросов


### Запросы модуля MetricsProducer
По умолчанию приложение слушает порт 8085

- http://localhost:8085/metrics - POST-запрос без тела, вызывает сервис, который обращается с помощью OkHttpClient к `spring-actuator` и отправляет метрики в Kafka; 

### Запросы модуля MetricsConsumer
По умолчанию приложение слушает порт 8086

- http://localhost:8086/metrics - GET-запрос на получение списка всех метрик.
- http://localhost:8080/metrics/{id} - GET-запрос на получение конкретной метрики по ее идентификатору.

---

### Kafka-UI
По умолчанию Kafka-UI доступно на порту http://localhost:9999