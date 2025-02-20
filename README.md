<h1 align="center">Локальный поисковой джижок</h1>

<p align="center">
  <img alt="Static Badge" src="https://img.shields.io/badge/java-17v-red">
  <img alt="Static Badge" src="https://img.shields.io/badge/java-36.6-blue">
  <img alt="Static Badge" src="https://img.shields.io/badge/spring-2.7.1v-green">
  <img alt="Static Badge" src="https://img.shields.io/badge/thymeleaf-yellow">
  <img alt="Static Badge" src="https://img.shields.io/badge/MySql-orange">
</p>

<div align="center">
  <img src="https://github.com/user-attachments/assets/72316d1c-3e69-4ed4-827a-d3d10a7481c8">
</div>

## Описание
<p align="center">
  <img src="https://github.com/user-attachments/assets/a5b398ce-774a-4860-bef3-f8c4e123ec23">
</p>

<p> 
Поиковой движок, реализованный на Spring Framework. Он производит индексацию и поиск по сайтам заданные в файле конфигурации.
Вся информация об сайтах находиться во вкладке ГЛАВНАЯ.
</p>
<p align="center"> 
  <img src="https://github.com/user-attachments/assets/f1a9c4eb-28bb-4f4b-ad98-40288928fe60">
</p>

<p> 
Перед тем как искать информацию по сайтам, их нужно проиндексировать и всю полученную информацию сохранить в базе данных. Индексировать можно не только сайты
  со всеми их страницами, но какую-нибудь одну страницу, но она должна находиться в пределах любого из введенных сайтов.
</p>
<p align="center"> 
  <img src="https://github.com/user-attachments/assets/ec5b844e-8043-41fa-b9c5-62abe4ae7a4a">
</p>

<p> 
После того как процесс индексации завершен можно искать нужные страницы по поисковому запросу, либо на всех сайтах, либо выбрать какой-нибудь один.
</p>
<p align="center"> 
  <img src="https://github.com/user-attachments/assets/b3a8e7c3-0775-4c6e-9cb9-94a5da82e263">
</p>

## Установка

Для успешного скачивания и подключения к проекту зависимостей из GitHub необходимо настроить Maven конфигурацию в файле settings.xml.
А зависимостях, в файле pom.xml добавлен репозиторий для получения jar файлов:
```
<repositories>
  <repository>
    <id>skillbox-gitlab</id>
    <url>https://gitlab.skillbox.ru/api/v4/projects/263574/packages/maven</url>
  </repository>
</repositories>
```
Так как для доступа требуется авторизации по токену для получения данных из публичного репозитория, для указания токена, найдите файл settings.xml
в корне проекта.
Актуальный токен, строка которую надо вставить в тег <value>...</value> находится в [документе по ссылке](https://docs.google.com/document/d/1rb0ysFBLQltgLTvmh-ebaZfJSI7VwlFlEYT9V5_aPjc/edit?tab=t.0).

```
docker-compose -f docker-compose.app.yaml up --build
```
