#!/bin/bash

# 서비스 ID
SERVICE_ID="iris-wdfs"

# Home path (in docker container)
CONTAINER_HOME="/home/docker"
SERVICE_HOME="${CONTAINER_HOME}/app/${SERVICE_ID}"
