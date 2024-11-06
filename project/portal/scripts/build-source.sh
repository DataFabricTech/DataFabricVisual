#!/bin/bash

set -e

cd $(dirname "$0")/..
baseDir=$(pwd)

source scripts/env.sh

srcDir=$baseDir/source
distDir=$baseDir/dist-source

function clear() {
    echo "========================================================="
    echo "## Source Clear"
    echo "========================================================="

    echo '-remove dist dir'
    rm -rf ${distDir}

    echo ''
}

function build() {
    echo "========================================================="
    echo "## Source Build"
    echo "========================================================="

    echo '-source build'
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
    clear
    build
    copy_dist

    echo "========================================================="
    echo "## Source Build End"
    echo "========================================================="
fi

echo 'done'
