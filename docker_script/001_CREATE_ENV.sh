docker network create docker_batch_network

docker run -d --name docker_batch_db \
    --network docker_batch_network \
    -e MYSQL_DATABASE=r102 \
    -e MYSQL_USER=user01 \
    -e MYSQL_PASSWORD=user01 \
    -e MYSQL_ROOT_PASSWORD=password \
    -v /var/lib/mysql:/var/lib/mysql \
    mysql

echo 'batch-core 라이브러리 검색 이후 배치에 필요한 기본 테이블 정보 세팅'