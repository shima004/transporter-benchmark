#!/bin/bash

function monitor_memory() {
    local log_file="/app/results/$1-memory.csv"
    echo "Python Memory Usage,Java Memory Usage" > $log_file
    while true; do
        local python_memory=$(calc_memory python)
        local java_memory=$(calc_memory java)
        echo "$python_memory,$java_memory" >> $log_file
        sleep 0.1
    done
}

function calc_memory(){
    local process_name=$1
    # プロセス名に一致するプロセスのPSSメモリを合計
    local total_pss_memory=0

    # smemコマンドでプロセス情報を取得し、プロセス名に一致するものを抽出
    local process_list=$(smem -r -n -c "name pss" | grep "$process_name")

    while read -r name pss_memory; do
        # シェルスクリプトの算術演算を使用してメモリ使用量を合計
        total_pss_memory=$((total_pss_memory + pss_memory))
    done <<< "$process_list"

    echo $total_pss_memory
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
    kill  $(ps aux | grep '[p]ython server.py' | awk '{print $2}')
    # kill java process
    kill  $(ps aux | grep '[j]ava' | awk '{print $2}')

    # kill memory monitoring process
    kill $monitor_pid
}

# Call the function with the name of the benchmark
run_benchmark gRPC adf-grpc
run_benchmark socket adf-socket
run_benchmark native adf-def
