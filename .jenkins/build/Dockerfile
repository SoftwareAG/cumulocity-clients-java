FROM openjdk:7-jdk
RUN apt-get update \
      && apt-get upgrade -y \
      && apt-get install -y sudo libltdl-dev rpm \
      && rm -rf /var/lib/apt/lists/* \
      && apt-get clean
