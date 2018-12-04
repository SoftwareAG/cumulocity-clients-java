FROM buildpack-deps:stretch-scm
RUN apt-get update \
      && apt-get upgrade -y \
      && apt-get install -y sudo libltdl-dev rpm openssh-client openssl gnupg mercurial curl zip unzip\
      && curl -s "https://get.sdkman.io" | bash \
      && bash -c 'source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install java 7.0.201-zulu'\
      && rm -rf /var/lib/apt/lists/* \
      && apt-get clean
ENV MAVEN_OPTS="-Xms1024m -Xmx4096m -XX:PermSize=1024m -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Dhttps.protocols=TLSv1.2"
ENV JAVA_HOME="/root/.sdkman/candidates/java/current/bin/java"