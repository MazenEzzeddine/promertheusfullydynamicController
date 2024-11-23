FROM openjdk:11

COPY  target/promertheusfullydynamic-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


#Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Djava.net.preferIPv4Stack=true -javaagent:/home/prometheus/jmx_prometheus_javaagent-0.17.2.jar=$JMX_PROMETHEUS_PORT:/home/prometheus/kafka-2_0_0.yml

#ENV JMX_PORT=10102



#ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.0/jmx_prometheus_javaagent-0.3.0.jar /home
#ADD https://github.com/hariramesh9a/kafka-producer-jmx/blob/master/kafka.yml /home


#RUN wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.0/jmx_prometheus_javaagent-0.3.0.jar
#RUN wget https://github.com/hariramesh9a/kafka-producer-jmx/blob/master/kafka.yml




#COPY  kafka.yml /bin/kafka.yml
#COPY jmx_prometheus_javaagent-1.0.1.jar /bin/jmx_prometheus_javaagent-1.0.1.jar
#
#
#RUN chmod   +r /bin/jmx_prometheus_javaagent-1.0.1.jar
#
#
#RUN chmod  +r /bin/kafka.yml
#
#EXPOSE 10103


#ENTRYPOINT ["java -Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=10103 -Djava.rmi.server.hostname=localhost -javaagent:/jmx_prometheus_javaagent-1.0.1.jar=10102:/kafka-2_0_0.yml","-jar","/app.jar"]


#ENTRYPOINT ["java", "-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=10103 -Dcom.sun.management.jmxremote.rmi.port=10102  -Djava.rmi.server.hostname=localhost", "-javaagent:/jmx_prometheus_javaagent-1.0.1.jar=10102:/bin/kafka.yml","-jar","/app.jar"]


#ENTRYPOINT ["java -jar /app.jar"]


#ENTRYPOINT ["java", "-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=10103 -Dcom.sun.management.jmxremote.rmi.port=10102  -Djava.rmi.server.hostname=localhost", "-javaagent:jmx_prometheus_javaagent-1.0.1.jar=10102:kafka.yml","-jar","/app.jar"]


#ENTRYPOINT ["java", "-Dcom.sun.management.jmxremote=true", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.rmi.port=10103", "-javaagent:jmx_prometheus_javaagent-1.0.1.jar=10102:kafka.yml","-jar","/app.jar"]




#ENTRYPOINT ["java", "-Dcom.sun.management.jmxremote=true", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.rmi.port=10103", "-Dcom.sun.management.jmxremote.port=10103" ,"-jar","/app.jar"]








#-Dcom.sun.management.jmxremote.ssl=false
#-Dcom.sun.management.jmxremote.authenticate=false
#-Dcom.sun.management.jmxremote.port=1098
#-Dcom.sun.management.jmxremote.rmi.port=1098
#-Djava.rmi.server.hostname=localhost
#-Dcom.sun.management.jmxremote.local.only=false