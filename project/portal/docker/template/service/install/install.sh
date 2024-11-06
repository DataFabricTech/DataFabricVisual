#!/bin/bash
# NOTE: appId 등은 packaging 과정에서 추가

cd $(dirname $0)/..
basePath=$(pwd)

if [ ! -f $basePath/common/install/install.sh ]; then
    echo "[ERROR] $basePath/common/install/install.sh is not found."
    exit 1
fi

# input: appId, company, basePath
source $basePath/common/install/install.sh
