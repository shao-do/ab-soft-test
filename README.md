# 'AB Soft' test automation framework
###### This tool is developed for [AB Soft](https://ab-soft.net/).

## Main purpose
- to complete the test task for AQA engineer position interview 

## Tools and technologies
- Java
- Maven
- TestNG
- Selenide
- Lombok
- Allure
- 'Intellij IDEA' IDE



## Prerequisites
In order to utilise this project you need to have the following installed locally:
- Java SE Development Kit 11
- Apache Maven 3


## How to install
Install JDK and Maven using [SDKMAN](https://sdkman.io/)

1.[Install SDKMAN](https://sdkman.io/install) 

For UNIX-like platforms:
- Open a new terminal and enter:

```shell. 
curl -s "https://get.sdkman.io" | bash
```
- Then, open a new terminal or enter:
```shell. 
source "$HOME/.sdkman/bin/sdkman-init.sh"
```
- Lastly, run the following code snippet to ensure that installation succeeded:
```shell. 
sdk version
```


2.[Install JDK](https://sdkman.io/jdks#AdoptOpenJDK) (11 version or higher)
1. To install JDK - run in terminal:
```shell
sdk install java
````
2. Find available JDK versions to use:
```shell
sdk list java
````
3. Use specific version (e.g. 11.0.11.hs-adpt):
```shell
sdk use java 11.0.11.hs-adpt
````
4. Verify the version of JDK currently in use:
```shell
sdk current java
````

3.[Install MAVEN](https://sdkman.io/sdks#maven) (3.x version)
1. To install Maven - run in terminal:
```shell
sdk install maven   
```
2. Find available Maven versions to use:
```shell
sdk list maven
````
3. Use specific version (e.g. 3.8.1):
```shell
sdk use maven 3.8.1
````
4. Verify version of Maven currently in use:
```shell
sdk current maven
````

## How to run tests
### Run all tests
Go to project folder in terminal and run:
```shell
mvn clean test
```
### Run specific test
##### NOTE: Currently, only this test class is present
```shell
mvn clean test -Dtest=ArticlesTagsTest
```

## Reporting
### After test run use Allure report generator
Run one of these commands from the command line:
1. Report will be generated into temp folder. Web server with results will start:
```shell
mvn allure:serve
```
2. Report will be generated tо {project root directory}/allure-report/index.html
```shell
mvn allure:report
```


## Additional tools which need to be installed:
['Lombok' plugin](https://projectlombok.org/setup/intellij) needs to be installed and annotation processing needs to be enabled.
Via Intellij IDEA (v.2021.1.1)
####1. Install lombok plugin 
- Go to Preferences > Plugins
- Search for Lombok plugin 
- Click on Install plugin
- Save changes 

####2. Enable annotation processing
- Go to Preferences > Build, Execution, Deployment > Compiler > Annotation processors
- Enable 'Annotation processing' checkbox
- Save changes

####3. Restart Intellij IDEA