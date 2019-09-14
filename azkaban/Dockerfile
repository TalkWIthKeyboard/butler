FROM openjdk:8-jdk-slim
COPY . /usr/src/app
ENV TZ=Asia/Shanghai
ENV NEW_RELIC_APP_NAME=spring-boot-application-template
ENV NEW_RELIC_LICENSE_KEY=a85c8e3679c94ce2f39fc4ceadc2413d29f6a32e
ENV NEW_RELIC_LOG=STDOUT
WORKDIR /usr/src/app
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", \
  "-XX:+UseCGroupMemoryLimitForHeap", \
  "-XX:MaxRAMFraction=2", \
  "-XX:ParallelGCThreads=4", \
  "-XX:CICompilerCount=4", \
  "-javaagent:target/newrelic/newrelic.jar", \
  "-jar", "target/spring-boot-application-template-1.0-SNAPSHOT.jar"]
CMD [""]



