FROM openjdk:@package.java-version@-alpine
RUN apk add  --no-cache coreutils
COPY etc/ /etc/@package.directory@/
ADD resources/* /data/
RUN chmod +x /data/entrypoint.sh
ENTRYPOINT /data/entrypoint.sh
