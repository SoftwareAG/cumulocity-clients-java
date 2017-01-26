FROM openjdk:@package.java-version@-alpine

ADD etc/* /etc/@package.directory@/
ADD resources/* /data/
ENTRYPOINT chmod +x /data/entrypoint.sh ;  /data/entrypoint.sh
