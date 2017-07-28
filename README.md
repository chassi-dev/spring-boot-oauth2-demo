# spring-boot-oauth2-demo

This application integrates with the Keycloak server and demonstrates Oauth2 authentication and authorization.

The kecloak.json file and the application.yml file contain credentials tha have to match up with entities defined in keycloak.

Spring security requires role names to begin with ROLE_

## Building an executable jar file

'mvn clean install' will also run tests which require a running Keycloak server instance with seeded test data.

java -jar target/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar 

From a browser, go to http://localhost:8080/

## To override the port at runtime

java -jar target/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar --server.port=8081

## To use without a browser,

1. Use curl command to authenticate use with keycloak

curl -X POST http://localhost:8080/auth/realms/demo/protocol/openid-connect/token -d "username=test" -d 'password=test123' -d 'grant_type=password' -d 'client_id=spring-boot-demo'

2. If authenticated successfully, copy value of "access_token" from json response

3. Use curl command to request resource from spring boot demo REST service - paste value of access_token from previous
   authentication step as Bearer token"

curl -k http://localhost:8081/greeting -H "Authorization: Bearer eyJhbGciOiJSU....."

4. Should get response similar to: {"id":1,"content":"Hello, Test Test!"}

5. If authenticated user has developer assigned, then this command will request the user's profile

curl -k http://localhost:8081/greeting/profile -H "Authorization: Bearer eyJhbGciOiJSU....."

6. Should get response similar to: {"authority":[{"authority":"ROLE_developer"},{"authority":"uma_authorization"}],"userDetail":{"family Name":"Test","name":"Test Test","email":"test@demo.com","preferred_username":"test","subject_id":"fffd3ccc-0fee-46bc-8d90-f63d05bcc3bb"}

7. If authenticated user does NOT have the the role developer assigned, then this command will fail with response similar to: 
   
{"error":"access_denied","error_description":"Access is denied"}

8. Requests with an unauthenticated user should fail with something similar:

curl -k http://localhost:8081/greeting

{"timestamp":1497988846507,"status":401,"error":"Unauthorized","message":"Unauthorized","path":"/greeting"}




   
   





