#!/bin/ash
if [ -n "$MEMORY_LIMIT" ];
 then
  value=$(numfmt  --from=auto  --grouping $MEMORY_LIMIT)
  value=$(($value/1048576)) # convert to MB
  value=$(awk "BEGIN { print(int($value - 50))}") # leave 50MB of memory space for system
  if [ $value -le "256" ]; # if less then 256MB fail
  then
    echo "memory limit is to small must be at lest 256MB"
    exit 1;
   else
    perm=$(awk "BEGIN { print(int($value * 0.1))}") # take 10% of available memory to perm/metaspace
    if [ $perm -gt "1024" ];
    then
      perm=1024
    fi
    if [ $perm -lt "128" ];
    then
      perm=128
    fi
    heap=$(($value - $perm))
    echo "Memory: perm/metaspace=$perm  heap=$heap from ${value} for $MEMORY_LIMIT"
  fi
   case $JAVA_VERSION in
   7*)
    echo " Using JDK7 memory settings"
    export JAVA_MEM="-XX:MaxPermSize=${perm}m -Xmx${heap}m"
   ;;
   *)
    echo " Using JDK8+ memory settings"
    export JAVA_MEM="-XX:MaxMetaspaceSize=${perm}m -Xmx${heap}m"
   ;;
   esac
fi
jvm_gc=${JAVA_GC:-"@package.jvm-gc@"}
jvm_mem=${JAVA_MEM:-"@package.jvm-mem@"}
jvm_opts=${JAVA_OPTS:-"-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/log/@package.directory@/heap-dump-%p.hprof"}
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
