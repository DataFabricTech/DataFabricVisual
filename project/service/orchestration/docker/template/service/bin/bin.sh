#!/usr/bin/env bash
# NOTE: appId 등은 packaging 과정에서 추가

cd $(dirname $0)/..

basePath=$(pwd)
propertyPath=$(pwd)/conf/$appId

# 각각 docker run하는 것은 프로젝트 별로 달라질 수 있다.
function fnRunDocker() {
    echo "[INFO] Random Port <- ${DOCKER_PORT}"

    docker run \
        -p ${DOCKER_PORT} \
        -v ${basePath}/logs/${appId}:${SERVICE_HOME}/logs \
        -v ${basePath}/conf/${appId}:${SERVICE_HOME}/conf \
        -v /etc/localtime:/etc/localtime:ro \
        -e TZ=Asia/Seoul -e LANG=ko_KR.UTF-8 -e LANGUAGE=ko_KR.UTF-8 -e LC_ALL=ko_KR.UTF-8 \
        -e START_OPTION=${START_OPTION} \
        -d --name $instanceId $IMAGE_NAME:$version \
        ${CONTAINER_HOME}/entry-point/docker-entrypoint.sh
}

if [ ! -d ${basePath}/logs/${appId} ]; then
    mkdir ${basePath}/logs/${appId} -p
fi


if [ ! -f $basePath/common/bin/bin.sh ]; then
    echo "[ERROR] $basePath/common/bin/bin.sh is not found."
    exit 1
fi

# input: appId, company, basePath
source $basePath/common/bin/bin.sh
