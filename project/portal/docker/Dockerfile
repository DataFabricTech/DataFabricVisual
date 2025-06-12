#base 이미지 선언
FROM rockylinux:8.9

# ps 설치
RUN dnf -y install procps-ng

#timezone 설정
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && dnf install -y tzdata && dnf clean all
ENV TZ=Asia/Seoul

# java 설치
COPY ../java21 /java21
RUN dnf install /java21/*.rpm -y
RUN java -version

#경로 지정
WORKDIR /data_catalog_portal

#필요한 내부 폴더 생성
RUN mkdir /data_catalog_portal/logs
RUN mkdir /data_catalog_portal/keystore
RUN mkdir /data_catalog_portal/conf
RUN mkdir /data_catalog_portal/data
RUN mkdir /data_catalog_portal/csv
# RUN mkdir /data_catalog_portal/sample
RUN mkdir /data_catalog_portal/hive_system_config
RUN mkdir /data_catalog_portal/unstructured_data

#volume 구성을 위한 기본 복제
RUN mkdir /defaultfile

#필요 데이터를 도커 안으로 이동
COPY ../scripts/docker_start.sh /data_catalog_portal/docker_start.sh
COPY ../conf/application-docker.yml /data_catalog_portal/conf/application.yml
COPY ../conf/logback.xml /data_catalog_portal/conf/logback.xml
COPY ../mobigen-catalog.jar /data_catalog_portal/mobigen-catalog.jar

#기본 데이터 보존용 파일 복제
RUN cp -r /data_catalog_portal /defaultfile/data_catalog_portal

#권한 설정
RUN chmod 755 /data_catalog_portal/*.sh

# bashrc 설정 추가
RUN echo 'alias ll="ls -alF"' >> /root/.bashrc

CMD ["sh", "-c", "set -e; /data_catalog_solution/docker_start.sh"]