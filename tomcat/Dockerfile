FROM tomcat:9-jdk11

# MariaDB 드라이버 필요시 추가
#COPY mariadb-java-client-2.7.12.jar /usr/local/tomcat/lib/

# Maven 빌드로 생성된 WAR 파일을 Tomcat에 배포
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

#COPY context.xml /usr/local/tomcat/conf/context.xml

EXPOSE 8080
