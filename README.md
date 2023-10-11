# Prerequisites
------------
### Maven >=3.8.1 and Java 11

```
# Download SDK man https://sdkman.io/install
$ curl -s "https://get.sdkman.io" | bash

# java 11
sdk install java 11.0.20-librca

# maven for building
sdk install maven
```

# Local Development

## Compile and Test

Use Maven to compile and install the dependencies

```bash
mvn clean install
```


## Maven Spring Boot Plugin
Start locally using the Maven Spring Boot plugin

* Start the app on localhost

```
mvn spring-boot:run
```

## Note
Make sure to do the changes required in Application.kt, to run the Application.
If you face any concerns or issues, feel free to reach out to [Ekansh Saxena](mailto:esaxena@expediagroup.com)
