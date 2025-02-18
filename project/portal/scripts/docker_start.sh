DIR="$( cd "$( dirname "$0" )" && pwd -P )"
cd $DIR


if [ ! "$(ls -A /data_catalog_portal/conf)" ]; then
  cp -r /defaultfile/data_catalog_portal/conf/* /data_catalog_portal/conf
fi


chmod 755 mobigen-catalog.jar
java -Dlogging.config=conf/logback.xml -jar mobigen-catalog.jar --spring.config.location=conf/application.yml 2>&1 | tee logs/mobigen-catalog.out
