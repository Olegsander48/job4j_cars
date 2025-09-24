## job4j_cars
[![Java CI with Maven](https://github.com/Olegsander48/job4j_cars/actions/workflows/maven.yml/badge.svg)](https://github.com/Olegsander48/job4j_cars/actions/workflows/maven.yml) 
## Описание проекта:
job4j_car является проектом по продаже автомобилей. Мы имеем возможность публиковать объявления и просматривать информацию. Владелец объявления может его редактировать и удалять

## Стек технологий:
- Java 21
- PostgreSQL 17
- Maven 3.9.9
- Liquibase 4.31.1
- Spring boot-web 3.5.3
- Spring boot-thymeleaf 3.5.3
- Spring boot-test 3.5.3
- Hibernate 5.6.11.Final
- H2 db 2.2.214
- Lombok 1.18.30
- Jacoco 0.8.10

## Требования к окружению:
- Java 21
- PostgreSQL 17
- Maven 3.9.9

## Запуск проекта:
1. Необходимо создать базу данных **cars**
```SQL
create database cars
```
2.  Теперь небходимо создать таблицы про помощи скриптов Liquibase. Необходимо выполнить фазу **через Maven**:
```
liquibase:update
```
Теперь у нас БД готова к использованию

3. Перейдите на [страницу](https://github.com/Olegsander48/job4j_cars/actions/workflows/jar.yml) и выберите последний выполненный worflow, скачайте my-app-jar
4. Из архива достаньте файл job4j_cars-0.0.1-SNAPSHOT.jar
5. Перейдите в директорию с jar-файлом, выполните команду:
```CMD
java -jar job4j_cars-0.0.1-SNAPSHOT.jar
```
6. Приложение запущено, перейдите по [адресу, указанному в командной строке,](http://localhost:8080/) и пользуйтесь приложением
   
## Интерфейс приложения:
1. Страница регистрации пользователя
![Страница регистрации пользователя](https://github.com/Olegsander48/job4j_cars/blob/master/images/registratioin.png)
2. Страница аунтификации пользователя
![Страница аунтификации пользователя](https://github.com/Olegsander48/job4j_cars/blob/master/images/authentication.png)
3. Страница объявлений автомобилей в продаже
![Страница объявлений автомобилей](https://github.com/Olegsander48/job4j_cars/blob/master/images/posts.png)
4. Страница просмотра информации об автомобиле
![Страница просмотра информации об авто в продаже](https://github.com/Olegsander48/job4j_cars/blob/master/images/info_page.png)
5. Если мы зашли под пользователем-автором объявления - становятся активными кнопки редактирования и удаления объявлений
![Страница просмотра объявления для автора](https://github.com/Olegsander48/job4j_cars/blob/master/images/active_info_page.png)
6. Владелец объявления может перейти на страницу редактирования объявления
![Страница редактирования объявления](https://github.com/Olegsander48/job4j_cars/blob/master/images/edit_page.png)
7. Авторизованный пользователь может публиковать новые объявления
![Страница редактирования объявления](https://github.com/Olegsander48/job4j_cars/blob/master/images/creation_page.png)

## Мои контакты:
Обращайтесь по любым вопросам и обратной связи, буду рад ответить :blush::blush:
<p align="center">
<a href="https://t.me/Olegsander48" target="blank"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/telegram.svg" alt="olegsander48" height="30" width="30" /></a>&nbsp;
<a href="https://linkedin.com/in/aleksandr-prigodich-b7028a1b3" target="blank"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/linkedin.svg" alt="aleksandr-prigodich" height="30" width="30" /></a>&nbsp;
<a href="http://discord.com/users/olegsander48" target="blank"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/discord.svg" alt="olegsander48" height="40" width="30" /></a>&nbsp;
<a href="mailto:prigodichaleks@gmail.com?subject=Hi%20Aleks.%20I%20saw%20your%20GitHub%20profile%20&body=I'm%20writing%20to%20you%20because%20...%0A"><img align="center" src="https://cdn.jsdelivr.net/npm/simple-icons@3.0.1/icons/gmail.svg" alt="prigodichaleks@gmail.com" height="40" width="30" /></a>&nbsp;
</p>
