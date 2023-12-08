#FROM amazoncorretto:17-alpine-jdk
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM tomcat:latest

COPY target/*.war /usr/local/tomcat/webapps/web-app.war

CMD ["catalina.sh", "run"]