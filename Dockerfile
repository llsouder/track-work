FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/track-work.jar /track-work/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/track-work/app.jar"]
