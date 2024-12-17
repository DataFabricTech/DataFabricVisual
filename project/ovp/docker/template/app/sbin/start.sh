#!/bin/bash
cd ${SERVICE_HOME}

source env.sh

jarPath="${SERVICE_HOME}/lib/${SERVICE_ID}.jar"

if [ ! -f "${jarPath}" ]; then
    echo "[Failed] There is no ${jarPath} file."
    exit 1
fi

if [[ ${SINGUP} = 'OFF' ]]; then
    java -jar ${jarPath} --spring.profiles.active=singup_off
elif [[ ${SINGUP} = 'ON' ]]; then
    java -jar ${jarPath} --spring.profiles.active=prod
else
    java -jar ${jarPath} ${START_OPTION}
fi
