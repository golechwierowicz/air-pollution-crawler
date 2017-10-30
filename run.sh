#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
pushd $DIR

java -Xmx1024M -Dcrawler.deployment.ports.0=2552 -Dcrawler.deployment.ports.1=2552 -Dcrawler.deployment.hosts.0=192.168.0.101 -Dcrawler.deployment.hosts.1=192.168.0.220 -Dakka.remote.netty.tcp.hostname=192.168.0.101 -jar target/air-pollution-crawler-1.0-SNAPSHOT-shaded.jar

popd
