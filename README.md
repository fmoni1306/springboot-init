# springboot-init

* spring init library

![image](https://user-images.githubusercontent.com/69664189/224232557-54128e75-0477-4272-8708-d3cee55aa8d8.png)


* build
```./gradlew clean bootjar ```


* run test
```  java -Dspring.profiles.active=local -Durl=jdbc:mariadb://host.docker.internal:3306/spring-init -jar build/libs/*.jar ```


* docker build test
``` docker build -t spring-init . ```

  

* spring devtools livereload, restart application edit

![img.png](img.png)