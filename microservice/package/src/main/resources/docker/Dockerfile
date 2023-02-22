FROM @package.docker.baseImage@
RUN apk add  --no-cache coreutils openjdk@package.java-version@
COPY etc/ /etc/@package.directory@/
ADD resources/* /data/
RUN chmod +x /data/entrypoint.sh
ENTRYPOINT /data/entrypoint.sh
