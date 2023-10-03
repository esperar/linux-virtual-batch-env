docker run -itd name docker_batch_app \
    --network docker_batch_network \
    -e SPRING_PROFILES_ACTIVE=docker \
    -v /APP/Q102/INFILES:/INFILES \
    docker_batch_app.image