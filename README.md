# References
https://techbeacon.com/app-dev-testing/how-perform-api-testing-rest-assured
https://github.com/allure-examples/allure-testng-example/blob/master/pom.xml

# pre-reqs
Java
Node


# Clone this repo
```
$ git clone https://github.com/dwwhalen/HelloRESTAssured.git
```
#install json-server locally 
https://github.com/typicode/json-server
```
npm install -g json-server
```

#start json-server

json-server --watch music-db.json

# To run test and generate Allure report
```
$ mvn clean test site
```
Report will be generated to target/site/allure-maven-plugin folder. To open the report you can use the following command:
```
$ mvn io.qameta.allure:allure-maven:serve
```