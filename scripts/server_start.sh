#!/usr/bin/env bash
cd /home/ec2-user/server/processor

sudo rm -rf /home/ec2-user/server/processor/processor-service.pid

echo "eliminando archvo"

sudo java -jar -Dspring.datasource.url=jdbc:mysql://clickescuela.ccmmeszml0xl.us-east-2.rds.amazonaws.com:3306/clickescuela -Dspring.datasource.username=root -Dspring.datasource.password=secret123 -Dlogging.file.name=/home/ec2-user/server/processor/debug.log \
    processor-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null & echo $! > processor-service.pid
