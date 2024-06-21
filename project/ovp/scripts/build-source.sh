#!/bin/bash

set -e

cd $(dirname "$0")/..
baseDir=$(pwd)

source scripts/env.sh

srcDir=$baseDir/server
frontDir=$baseDir/frontend
distDir=$baseDir/dist-source

# Node 버전 확인
function checkNode() {
    DEP_NODE_VER="v18"

    if which node > /dev/null
    then
        # ex) v16.20.2
        node_ver=$(node -v 2>&1)
        node_ver_major=$(echo $node_ver | awk -F '.' '{sub("^$", "0", $2); print $1}')
        if [[ "$node_ver_major" != "${DEP_NODE_VER}" ]]; then
            echo "Need Node version ${DEP_NODE_VER}. Current version is ${node_ver}" 1>&2;
            exit 1;
        else
            echo "Current node version is ${node_ver}"
        fi
    else
        echo "Not found node. Need Node.js version ${DEP_NODE_VER}" 1>&2;
        exit 1;
    fi
}

# JDK 버전 확인
function checkJava() {
    DEP_JAVA_VER="17"

    if which java > /dev/null
    then
        _java=java
    elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]]; then
        _java="$JAVA_HOME/bin/java"
    else
        echo "Not found java. Need Java version ${DEP_JAVA_VER}"
        exit 1;
    fi

    if [[ "$_java" ]]; then
        # ex) openjdk version "17.0.1" 또는 "1.8.0_292"
        java_ver=$("$_java" -version 2>&1 | awk -F[\".] '/version/ {print $2}')
        echo "Java Major version ${java_ver}"
        if [[ "$java_ver" != "$DEP_JAVA_VER" ]]; then
            echo "Need Java version ${DEP_JAVA_VER}. Current version is ${java_ver}" 1>&2
            exit 1
        else
            echo "Current java version is ${java_ver}"
        fi
    fi
}




function clear() {
    echo "========================================================="
    echo "## Source Clear"
    echo "========================================================="

    echo '-remove dist dir'
    rm -rf ${distDir}

    echo ''
}

function buildJS() {
    echo "========================================================="
    echo "## Source Build"
    echo "========================================================="

    echo '-source build - JavaScript'
    cd ${frontDir}
    npm i --legacy-peer-deps
    npm run build
    cd ${baseDir}
}

function buildJava() {
    echo "========================================================="
    echo "## Source Build"
    echo "========================================================="

    echo '-source build - Java'
    cd ${srcDir}
    mvn clean
    mvn install -Dmaven.test.skip=true
    cd ${baseDir}
}

function copy_dist() {
    echo '-copy jar'
    mkdir -p ${distDir}
    cp -r ${srcDir}/target/${SERVICE_ID}.jar ${distDir}/${SERVICE_ID}.jar

    echo ''
}

# ex) ./build-source.sh clear
if [[ $1 == 'clear' ]]; then
    clear
else
    checkNode
    checkJava
    clear
    buildJS
    buildJava
    copy_dist

    echo "========================================================="
    echo "## Source Build End"
    echo "========================================================="
fi

echo 'done'
