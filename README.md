# spring-boot-oauth2-demo

This application integrates with the Keycloak server and demonstrates Oauth2 authentication and authorization.

The kecloak.json file and the application.yml file contain credentials tha have to match up with entities defined in keycloak.

Spring security requires role names to begin with ROLE_

## Builds an executable jar file

java -jar target/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar 

## To override the port

java -jar target/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar --server.port=8081