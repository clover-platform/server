FROM azul/zulu-openjdk-alpine:17-latest
WORKDIR /app
RUN wget https://mirrors.aliyun.com/apache/skywalking/java-agent/9.2.0/apache-skywalking-java-agent-9.2.0.tgz
RUN tar -xvf apache-skywalking-java-agent-9.2.0.tgz
RUN mv skywalking-agent agent
RUN rm apache-skywalking-java-agent-9.2.0.tgz
RUN mv agent/plugins/* agent/optional-plugins/
RUN cp  \
    agent/optional-plugins/apm-mybatis-3.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-nacos-client-2.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-annotation-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-async-annotation-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-cloud-feign-2.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-cloud-gateway-4.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-concurrent-util-4.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-core-patch-9.2.0.jar \
    agent/optional-plugins/apm-spring-scheduled-annotation-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-tx-plugin-9.2.0.jar \
    agent/optional-plugins/apm-spring-webflux-6.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-springmvc-annotation-6.x-plugin-9.2.0.jar \
    agent/optional-plugins/apm-springmvc-annotation-commons-9.2.0.jar \
    agent/optional-plugins/resttemplate-commons-9.2.0.jar \
    agent/optional-plugins/spring-commons-9.2.0.jar \
    agent/optional-plugins/spring-webflux-6.x-webclient-plugin-9.2.0.jar \
    agent/plugins/
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-javaagent:/app/agent/skywalking-agent.jar", "-jar", "app.jar"]
