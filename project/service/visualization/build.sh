#!/bin/bash

sh scripts/build-source.sh $1
sh scripts/build-docker.sh $1
