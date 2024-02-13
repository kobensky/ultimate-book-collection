# скачиваем нужную версию jdk
FROM openjdk:19
MAINTAINER Nizhevich Roman
# указываем путь к собранному jar файлу
COPY target/ultimate-book-collection-0.0.1-SNAPSHOT.jar topbooks.jar
ENTRYPOINT ["java", "-jar", "topbooks.jar"]