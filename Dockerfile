FROM openjdk:11

ADD ./build/libs/*.jar appBatch.jar

ADD ./batchRun.sh batchRun.sh
