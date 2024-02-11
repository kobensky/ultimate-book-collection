Тестовое задание "Топ 10 книг"

Датасет взят отсюда: https://www.kaggle.com/datasets/cristaliss/ultimate-book-collection-top-100-books-up-to-2023?resource=download
Копия датасета лежит в папке ресурсов.

В application.properties прописывается путь к вашему файлу.

Запускать из IDE.

Примеры запросов:
  1.  http://localhost:8080/api/top10?year=2010&column=author&sort=ASC
        1. year - необязательный параметр, при наличии выдаёт книги только указанного года публикации.
        2. column - обязательный параметр, наименование поля, по которому сортируются данные.
           
            -Возможные значения: book, author, numPages, publicationDate, rating, numberOfVoters.
        3. sort - обязательный параметр, сортировка по возрастанию/убыванию.
           
            -Возможные значения: ASC, DESC.

  2.  http://localhost:8080/api/allBooks - возвращает все книги из датасета.


// добавить инструкцию по упаковке

// добавить инструкцию по Docker'у
