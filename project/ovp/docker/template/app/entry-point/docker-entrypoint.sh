#!/bin/bash

# app directory
cd ${SERVICE_HOME}
echo "work dir: `pwd`"

# start entrypoint
echo "start ${SERVICE_ID}"
./sbin/start.sh
