FROM openjdk:11.0.11-9-slim

ARG SERVICE_ID
ARG CONTAINER_HOME
ARG SERVICE_HOME

# locales 설치
RUN sed -i 's/deb.debian.org/ftp.kr.debian.org/g' /etc/apt/sources.list \
 && apt-get -o Acquire::Check-Valid-Until=false update \
 && apt-get install -y --no-install-recommends locales \
 && apt-get clean \
 && localedef -f UTF-8 -i ko_KR ko_KR.UTF-8

# directory 구조
# /home/docker/                         // ${CONTAINER_HOME}
#     app/
#         ${SERVICE_ID}/               // ${SERVICE_HOME}
#             conf/
#                 docker.conf
#                 *.yml
#             lib/
#                 ${SERVICE_ID}.jar
#             sbin/
#                 start.sh
#             env.sh
#     entry-point/
#         *-entrypoint.sh

ENV DOCKER_ENV=true \
    SERVICE_ID=${SERVICE_ID} \
    CONTAINER_HOME=${CONTAINER_HOME} \
    SERVICE_HOME=${SERVICE_HOME}

# 빌드 파일 복사
COPY target/app ${CONTAINER_HOME}/app
COPY target/entry-point ${CONTAINER_HOME}/entry-point

# create user & home dir
RUN mkdir -p ${CONTAINER_HOME} \
 && useradd -d ${CONTAINER_HOME} -u 1001 -g root -p hello.docker docker \
 && chown -R docker:root ${CONTAINER_HOME}

# JAVA 버전 확인
# RUN echo java --version

# NOTE: windows 환경에서 빌드시 실행파일 권한 문제 해결을 위해 chmod 추가 수행
RUN find ${CONTAINER_HOME} -name "*.sh" -exec chmod 0755 {} \;

# change user
USER docker
WORKDIR ${SERVICE_HOME}

# CMD ["java", "-jar", "${SERVICE_ID}.jar"]
