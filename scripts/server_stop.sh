#!/bin/bash
chmod +x /home/ec2-user/server/processor/logs
chmod +x /home/ec2-user/server/processor/logs/error.log
chmod +x /home/ec2-user/server/processor/logs/debug.log
var="$(cat /home/ec2-user/server/processor/processor-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/processor/processor-service.pid

