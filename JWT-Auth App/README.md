# JWT-Auth App

---
Базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей.

---
Стек используемых технологий:
- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- Mapstruct
- AOP
- Lombok
- Testcontainers
- Mockito
- Swagger-UI


---
## Локальный запуск

### Требования

Проект использует синтаксис Java 17. Для локального запуска вам потребуется
Docker Desktop для запуска контейнера с базой данных Postgres.

---
### Запуск приложения в среде разработки

В корне проекта находится файл docker-compose.yml c контейнерами базы данных PostgreSQL.
Откройте файл docker-compose в среде разработки и нажмите на значок запуска напротив строки "services",
или в командной строке перейдите в папку с проектом и запустите командой:

```bash
docker-compose up -d
```
После этого запускаем приложение из класса [src/main/java/com/t1/jwt_auth_app/JwtAuthAppApplication.java](src/main/java/com/t1/jwt_auth_app/JwtAuthAppApplication.java).

---

```
В своей реализации при регистрации пользователей я не передаю роли в запросе, 
вместо этого автоматически создается суперпользователь с email `admin@mail.ru` и паролем `admin`, 
из которого можно добавлять права определенному пользователю 
```

## Описание API:

В проект подключена система генерации API `SpringDoc-OpenAPI`. При запущенном приложении вы можете просмотреть описание API, а также
выполнять запросы используя интерфейс Swagger.

http://localhost:8080/swagger-ui/index.html

---

Также в корне проекта в папке `rest-examples` находится [файл](rest-examples/security_app.postman_collection.json) с коллекцией запросов, которую можно импортировать в Postman.