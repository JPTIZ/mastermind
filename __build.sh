#!/usr/bin/bash
rm mastermind/*.class
javac src/*.java -d ./mastermind -cp src/framework
if [ $? -eq 0 ]; then
    ./__run.sh
fi
