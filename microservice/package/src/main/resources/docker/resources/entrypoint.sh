#!/bin/ash
jvm_gc=${JAVA_GC:-"@package.jvm-gc@"}
jvm_mem=${JAVA_MEM:-"@package.jvm-mem@"}
jvm_opts=${JAVA_OPTS:-"-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/log/@package.directory@/heap-dump-%p.hprof"}
arguments=${ARGUMENTS:-"@package.arguments@"}

mkdir -p /var/log/@package.name@; echo "heap dumps  /log/@package.name@/heap-dump-<pid>.hprof"

java  ${jvm_opts}  ${jvm_gc}  ${jvm_mem} -jar /data/@package.name@.jar ${arguments}
