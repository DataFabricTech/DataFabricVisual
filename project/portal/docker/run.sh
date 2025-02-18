#현재 파일 경로의 상위 폴더를 기준으로 실행

DIR="$( cd "$( dirname "$0" )" && pwd -P )"
cd $DIR/..
docker-compose -f docker/docker-compose.yml up -d