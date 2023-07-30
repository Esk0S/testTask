# Startup instructions
Java Version - 17.0.6

The build system used is Gradle 8.0

The Apache Commons CLI library was used to parse the arguments.

Gradle dependency:
```groovy
implementation 'commons-cli:commons-cli:1.3.1'
```

## Launch examples: 

For testTask.exe
```console
testTask.exe -d -i out in1 in2 in3
```

For testTask.jar
```console
java -jar testTask.jar -d -i out in1 in2 in3
```
