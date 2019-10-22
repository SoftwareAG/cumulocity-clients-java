#!/bin/sh
if [ -n "$MEMORY_LIMIT" ];
 then
  value=$(numfmt  --from=auto  --grouping $MEMORY_LIMIT)
  value=$(($value/1048576)) # convert to MB
  echo "MEMORY_LIMIT: ${value}MB"
  memory_left=$(awk "BEGIN { memory = int($value * 0.1); if (memory <50) {memory = 50} print memory} ")
  echo "${memory_left}MB is left for system"
  value=$(awk "BEGIN { print(int($value - $memory_left))}") # leave memory space for system
  echo "${value}MB is left for application"
  if [ $value -lt "128" ]; # if less then 128MB fail
  then
    echo "Memory left for application is to small must be at lest 128MB"
    exit 1;
   else
    perm=$(awk "BEGIN { memory= int($value * 0.1); if (memory >1024) {memory = 1024} else if ( memory < 64 ){ memory = 64 } print memory} ") # take 10% of available memory to perm/metaspace
    heap=$(($value - $perm))
    echo "Java Memory Settings: perm/metaspace=${perm}MB heap=${heap}MB  from ${value}MB the ${memory_left}MB is left for system for $MEMORY_LIMIT"
  fi
   case $JAVA_VERSION in
   7*)
    echo "Using JDK7 memory settings"
    export JAVA_MEM="-XX:MaxPermSize=${perm}m -Xmx${heap}m"
   ;;
   *)
    echo "Using JDK8+ memory settings"
    export JAVA_MEM="-XX:MaxMetaspaceSize=${perm}m -Xmx${heap}m"
   ;;
   esac
fi
jvm_gc=${JAVA_GC:-"@package.jvm-gc@"}
jvm_mem=${JAVA_MEM:-"@package.jvm-mem@"}
jvm_opts=${JAVA_OPTS:-"-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log/@package.directory@/heap-dump-%p.hprof"}
arguments=${ARGUMENTS:-"@package.arguments@ --package.name=@package.name@ --package.directory=@package.directory@"}

proxy_params=""
if [ -n "$PROXY_HTTP_HOST" ]; then proxy_params="-Dhttp.proxyHost=${PROXY_HTTP_HOST} -DproxyHost=${PROXY_HTTP_HOST}"; fi
if [ -n "$PROXY_HTTP_PORT" ]; then proxy_params="${proxy_params} -Dhttp.proxyPort=${PROXY_HTTP_PORT} -DproxyPort=${PROXY_HTTP_PORT}"; fi
if [ -n "$PROXY_HTTP_NON_PROXY_HOSTS" ]; then proxy_params="${proxy_params} -Dhttp.nonProxyHosts=\"${PROXY_HTTP_NON_PROXY_HOSTS}\""; fi
if [ -n "$PROXY_HTTPS_HOST" ]; then proxy_params="${proxy_params} -Dhttps.proxyHost=${PROXY_HTTPS_HOST}"; fi
if [ -n "$PROXY_HTTPS_PORT" ]; then proxy_params="${proxy_params} -Dhttps.proxyPort=${PROXY_HTTPS_PORT}"; fi
if [ -n "$PROXY_SOCKS_HOST" ]; then proxy_params="${proxy_params} -DsocksProxyHost=${PROXY_SOCKS_HOST}"; fi
if [ -n "$PROXY_SOCKS_PORT" ]; then proxy_params="${proxy_params} -DsocksProxyPort=${PROXY_SOCKS_PORT}"; fi


mkdir -p /var/log/@package.name@; echo "heap dumps  /var/log/@package.name@/heap-dump-<pid>.hprof"

java ${jvm_opts} ${jvm_gc} ${jvm_mem} ${proxy_params} -jar /data/@package.name@.jar ${arguments}
