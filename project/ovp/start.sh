SERVICE_ID="ovp"
CONTAINER_HOME="/home/docker"
SERVICE_HOME="${CONTAINER_HOME}/app/${SERVICE_ID}"

IMAGE_NAME=repo.iris.tools/datafabric/ovp
VERSION=2024-ready-RC20240823.0-d8eeb50-2

DOCKER_PORT=8889
LOCAL_MOUNT_PATH=$(pwd)

docker run -itd --name ovp --restart unless-stopped \
    -p $DOCKER_PORT:8080 \
    -v "${LOCAL_MOUNT_PATH}"/${SERVICE_ID}/logs/:${SERVICE_HOME}/logs \
    -v "${LOCAL_MOUNT_PATH}"/${SERVICE_ID}/conf/:${SERVICE_HOME}/conf \
    -v /etc/localtime:/etc/localtime:ro \
    -e TZ=Asia/Seoul -e LANG=ko_KR.UTF-8 -e LANGUAGE=ko_KR.UTF-8 -e LC_ALL=ko_KR.UTF-8 \
    -e SERVICE_ID=${SERVICE_ID} \
    --platform linux/amd64 \
    -e SERVICE_HOME=${SERVICE_HOME} \
    $IMAGE_NAME:$VERSION \
    ${CONTAINER_HOME}/entry-point/docker-entrypoint.sh
