#!/bin/bash

echo "config check"

if [[ -z "$GATEWAY_HOST" ]]; then
    echo "check enciron, GATEWAY_HOST"
    exit 1
fi

if [[ -z "$GATEWAY_PORT" ]]; then
    echo "check enciron, GATEWAY_PORT"
    exit 1
fi

if [[ ! -z "$START_OPTION" ]]; then
    echo "Cannot use START_OPTION in k8s-entrypoint.sh"
    exit 1
fi

# generate config
echo "generate config"
cd ${SERVICE_HOME}/conf
python gen-config.py

export START_OPTION=--spring.config.location=file:${SERVICE_HOME}/conf/application.yml
echo "START_OPTION=$START_OPTION"

# app directory
cd ${SERVICE_HOME}
echo "work dir: `pwd`"

# start entrypoint
echo "start ${SERVICE_ID}"
./sbin/start.sh
