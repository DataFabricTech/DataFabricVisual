#!/bin/bash

if [[ -z ${SINGUP} ]]; then
    echo "SINGUP default setting: "
fi

echo "#############"
echo "SINGUP=${SINGUP}"
echo "START_OPTION=${START_OPTION}"
echo "SERVICE_ID=${SERVICE_ID}"
echo "CONTAINER_HOME=${CONTAINER_HOME}"
echo "SERVICE_HOME=${SERVICE_HOME}"
