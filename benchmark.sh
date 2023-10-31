#!/bin/bash

function monitor_memory() {
    local log_file="/app/results/" + $1 + "_memory.csv"
    echo "Python Memory Usage,Java Memory Usage" > $log_file
    while true; do
        local python_memory=$(top -b -n 1 | grep 'python' | awk '{print $6}')
        local java_memory=$(top -b -n 1 | grep 'java' | awk '{print $6}')
        echo "$python_memory,$java_memory" >> $log_file
        sleep 0.1
    done
}

function run_benchmark() {
    # Start memory monitoring in the background
    monitor_memory $1 &

    # Remember the PID of the monitoring process
    local monitor_pid=$!

    # /app/$1/python is  
    cd /app/$1/python
    poetry install
    poetry run python server.py &

    cd /app/$1/java/$2
    ./gradlew jmh
    mv ./app/build/results/jmh/results.txt /app/results/$2.txt

    # kill python process
    kill $(ps aux | grep '[p]ython server.py' | awk '{print $2}')
    # kill java process
    kill $(ps aux | grep '[j]ava -jar' | awk '{print $2}')

    # kill memory monitoring process
    kill $monitor_pid
}

# Call the function with the name of the benchmark
run_benchmark gRPC adf-grpc
run_benchmark socket adf-socket
run_benchmark native adf-def
