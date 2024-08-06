FROM @package.docker.baseImage@
RUN command -v apk >/dev/null 2>&1 && ! command -v coreutils >/dev/null 2>&1 && apk add --no-cache coreutils
RUN command -v apk >/dev/null 2>&1 && ! command -v java >/dev/null 2>&1 && apk add --no-cache openjdk@package.java-version@
COPY etc/ /etc/@package.directory@/
ADD resources/* /data/
RUN chmod +x /data/entrypoint.sh
ENTRYPOINT /data/entrypoint.sh
