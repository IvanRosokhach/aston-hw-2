FROM tomcat:latest
COPY target/*.war /usr/local/tomcat/webapps/web-app.war
CMD ["catalina.sh", "run"]