FROM openjdk:@package.java-version@-alpine

ADD etc/* /etc/@package.directory@/
ADD resources/* /data/
RUN chmod +x /data/entrypoint.sh
ENTRYPOINT /data/entrypoint.sh
