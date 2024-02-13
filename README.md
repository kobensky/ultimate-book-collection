# Тестовое задание "Топ 10 книг"

Датасет взят [отсюда](https://www.kaggle.com/datasets/cristaliss/ultimate-book-collection-top-100-books-up-to-2023?resource=download). 
Он содержит лучшие 100 книг за каждый год с 1980 по 2023 по рейтингу Goodreads, представленный файлом CSV.
Копия датасета лежит в папке ресурсов. В application.properties прописывается путь к вашему файлу в переменную csv.path.

Краткое ТЗ:

Требуется написать REST сервис, реализующий метод (GET) top10, возвращающий первые 10 книг из датасета,
отфильтрованные в соответствии с параметрами запроса. 
В ответ на некорректные входные данные сервис должен реагировать понятными сообщениями об ошибках.
Датасет загружается в память при старте приложения.

Примеры REST запросов:
  1.  http://localhost:8080/api/top10?year=2010&column=author&sort=ASC
        1. "year" - необязательный параметр, при наличии выдаёт книги только указанного года публикации.
        2. "column" - обязательный параметр, наименование поля, по которому сортируются данные.
           
            -Возможные значения: book, author, numPages, publicationDate, rating, numberOfVoters.
        3. "sort" - обязательный параметр, сортировка по возрастанию/убыванию.
           
            -Возможные значения: ASC, DESC.

  2.  http://localhost:8080/api/allBooks - возвращает все книги из датасета.
  3.  http://localhost:8080/api/book?bookName=Harry&sort=ASC
        1. "bookName" - обязательный параметр, имя книги в любом регистре.
        2. "sort" - обязательный параметр, сортировка по возрастанию/убыванию.

           -Возможные значения: ASC, DESC.

Используемый стек:
Java 17, Spring boot 3.2.2, OpenCSV 5.7.1, Lombok, Maven.

# Инструкция по запуску приложения
Варианты:
1. Скачать проект и запустить из IDE.
2. Скачать проект и собрать jar файл (в IDE с помощью кнопки package или командой mvn clean package). После чего запустить этот jar в терминале используя стандартный запуск java - jar имяджарфайла.jar
3. Скачать проект. 
В файле Dockerfile указать путь к вашему jar файлу.
Далее выполнить команду "docker build -t имяобраза ."
где имяобраза - это любое имя которое вы хотите.
Далее можно запустить командой "docker run -d -p 8080:8080 имяобраза".

После любого из этих вариантов приложение будет доступно по адресу http://localhost:8080
