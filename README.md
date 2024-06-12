# Kotlin Jar Loader

Example project to showcase how to load jar files at runtime using Kotlin.

## How to run

1. Build the example jar files

    ```shell
    ./gradlew buildExampleJars
    ```

2. Build this project
    ```shell
    ./gradlew shadowJar
    ```

3. Run the project
    ```shell
    java -jar build/libs/kotlin-jar-loader.jar
    ```