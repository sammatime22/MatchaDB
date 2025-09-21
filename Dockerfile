FROM openjdk:14-jdk-alpine as builder

# Set up the Working Directory
WORKDIR /MatchaDB
COPY . .

# Get Gradle 6.6.1
RUN apk add curl
RUN curl -LJO https://services.gradle.org/distributions/gradle-6.6.1-bin.zip
RUN unzip gradle-6.6.1-bin.zip
ENV GRADLE_HOME=/MatchaDB/gradle-6.6.1
ENV PATH=$GRADLE_HOME/bin:$PATH

# Build the application
RUN set -x && gradle build && ls -l /MatchaDB/build/libs

# Take the .war from the last build, and develop the container
FROM openjdk:14-jdk-alpine
WORKDIR /app
COPY --from=builder /MatchaDB/build/libs/MatchaDB-betav1.0.3-SNAPSHOT.war .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "--enable-preview", "-DdatabaseFile=/app/matchadb.json", "-DdatabaseName=\"MatchaDB\"", "MatchaDB-betav1.0.3-SNAPSHOT.war"]