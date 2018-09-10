FROM openjdk:7-jdk
ENV MAVEN_OPTS="-Xms1024m -Xmx4096m -XX:PermSize=1024m -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Dhttps.protocols=TLSv1.2"
RUN apt-get update \
      && apt-get upgrade -y \
      && apt-get install -y sudo libltdl-dev rpm \
      && rm -rf /var/lib/apt/lists/* \
      && apt-get clean
