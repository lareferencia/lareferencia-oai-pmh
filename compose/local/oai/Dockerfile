FROM maven:latest

WORKDIR /app

ADD pom.xml /app
RUN mvn verify clean --fail-never

COPY . /app

RUN mvn -v
RUN mvn clean package install -DskipTests -Dmaven.javadoc.skip=true

EXPOSE 8092

ENTRYPOINT ["java","-jar","/app/oai-pmh.jar"]