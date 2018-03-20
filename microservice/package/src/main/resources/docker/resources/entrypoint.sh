#!/bin/ash
jvm_gc=${JAVA_GC:-"@package.jvm-gc@"}
jvm_mem=${JAVA_MEM:-"@package.jvm-mem@"}
jvm_opts=${JAVA_OPTS:-"-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/log/@package.directory@/heap-dump-%p.hprof -XX:NativeMemoryTracking=summary -XX:+PrintGCDetails -XX:+PrintGCDateStamps"}
arguments=${ARGUMENTS:-"@package.arguments@"}

proxy_params=""
if [ -n "$PROXY_HTTP_HOST" ]; then proxy_params="-Dhttp.proxyHost=${PROXY_HTTP_HOST} -DproxyHost=${PROXY_HTTP_HOST}"; fi
if [ -n "$PROXY_HTTP_PORT" ]; then proxy_params="${proxy_params} -Dhttp.proxyPort=${PROXY_HTTP_PORT} -DproxyPort=${PROXY_HTTP_PORT}"; fi
if [ -n "$PROXY_HTTP_NON_PROXY_HOSTS" ]; then proxy_params="${proxy_params} -Dhttp.nonProxyHosts=\"${PROXY_HTTP_NON_PROXY_HOSTS}\""; fi
if [ -n "$PROXY_HTTPS_HOST" ]; then proxy_params="${proxy_params} -Dhttps.proxyHost=${PROXY_HTTPS_HOST}"; fi
if [ -n "$PROXY_HTTPS_PORT" ]; then proxy_params="${proxy_params} -Dhttps.proxyPort=${PROXY_HTTPS_PORT}"; fi
if [ -n "$PROXY_SOCKS_HOST" ]; then proxy_params="${proxy_params} -DsocksProxyHost=${PROXY_SOCKS_HOST}"; fi
if [ -n "$PROXY_SOCKS_PORT" ]; then proxy_params="${proxy_params} -DsocksProxyPort=${PROXY_SOCKS_PORT}"; fi

mkdir -p /var/log/@package.name@; echo "heap dumps  /log/@package.name@/heap-dump-<pid>.hprof"

java ${jvm_opts} ${jvm_gc} ${jvm_mem} ${sys_props} ${proxy_params} -jar /data/@package.name@.jar ${arguments}
