FROM dockerfile/java:oracle-java7

RUN sudo apt-get update
RUN sudo apt-get install -y maven
WORKDIR /opt
RUN git clone https://github.com/xebialabs-community/placeholders-mustachifier-maven-plugin.git
WORKDIR /opt/placeholders-mustachifier-maven-plugin
RUN mvn clean package
RUN mvn clean install -Dgpg.skip=true


