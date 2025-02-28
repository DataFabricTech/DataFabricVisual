#현재 파일 경로의 상위 폴더를 기준으로 실행

export DATA_CATALOG_SOLUTION_DATA=/DATA_CATALOG/data
export DATA_CATALOG_PORTAL_IMG_VERSION=0.1

DIR="$( cd "$( dirname "$0" )" && pwd -P )"
cd $DIR/..
docker-compose -f docker/docker-compose.yml up -d