FROM sphenrie/deb-jre8
MAINTAINER sphenrie	
ADD build/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar /deployment/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar
RUN sh -c 'touch /deployment/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-Xms512m", "-Xmx700m", "-Djava.security.egd=file:/dev/./urandom",  "-jar", "/deployment/spring-boot-oauth2-demo-1.0-SNAPSHOT.jar"]


