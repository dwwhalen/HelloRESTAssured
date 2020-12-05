# References
Check out my [blog post](https://dev.to/dwwhalen/series/9704) for some more details about this repo.  

# pre-reqs
- Java
- Node


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

```
json-server --watch music-db.json
```

# To run test and generate Allure report
Report will be generated to target/site/allure-maven-plugin folder:
```
$ mvn clean test site
```

To open/serve the report you can use the following command:
```
$ mvn io.qameta.allure:allure-maven:serve
```

FYI regarding schema validation...to infer JSON schema from JSON: https://www.liquid-technologies.com/online-json-to-schema-converter