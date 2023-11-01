#!/bin/bash


# Monitor the memory usage of a process
# @param $1: name of the process 
# @param $2: name of the benchmark method 
function monitor() {
    if [ $# -eq 2 ]; then
        local log_file="/app/results/$1-$2-memory.csv"
    else
        local log_file="/app/results/$1-all-memory.csv"
    fi
    echo "Python Memory Usage,Java Memory Usage" > $log_file
    while true; do
        local python_memory=$(calc_memory python)
        local java_memory=$(calc_memory java)
        local python_cpu=$(calc_cpu python)
        local java_cpu=$(calc_cpu java)
        echo "$python_memory,$java_memory,$python_cpu,$java_cpu" >> $log_file
        sleep 0.1
    done
}

# Calculate the memory usage of a process
# @param $1: name of the process 
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

# Calculate the CPU usage of a process
# @param $1: name of the process
function calc_cpu(){
    local process_name=$1
    # プロセス名に一致するプロセスのCPU使用率を合計
    local total_cpu=0

    local top_output=$(top -b -n 1)
    if [ $? -ne 0 ]; then
        echo 0
        return
    fi

    # topコマンドでプロセス情報を取得し、プロセス名に一致するものを全て抽出し、CPU使用率を合計
    while read -r pid cpu; do
        # Check if pid and cpu are not empty
        if [[ -n $pid && -n $cpu ]]; then
            # Use bc to add the CPU usage to the total
            total_cpu=$(echo "$total_cpu + $cpu" | bc)
        fi
    done <<< $(echo "$top_output" | grep "$process_name" | awk '{print $1, $9}')

    echo $total_cpu
}

# Run the benchmark
# @param $1: name of the benchmark
# @param $2: name of the java project
# @param $3: name of the benchmark method 
function run_benchmark() {
    # python server
    cd /app/$1/python
    # Install dependencies
    poetry install
    # Start the python server in the background
    poetry run python server.py &

    # java client
    cd /app/$1/java/$2
    # Start memory monitoring in the background
    monitor $1 $3 &
    # Remember the PID of the monitoring process
    local monitor_pid=$!

    # Run the benchmark
    if [ $# -eq 3 ]; then
        ./gradlew jmh -Djmh.includes=$3
    else
        ./gradlew jmh
    fi
    # Move the results to the results folder
    if [ $# -eq 3 ]; then
        mv ./app/build/results/jmh/results.txt /app/results/$2-$3.txt
    else
        mv ./app/build/results/jmh/results.txt /app/results/$2-all.txt
    fi

    # kill python process
    kill  $(ps aux | grep '[p]ython server.py' | awk '{print $2}')
    # kill java process
    kill  $(ps aux | grep '[j]ava' | awk '{print $2}')

    # kill memory monitoring process
    kill $monitor_pid
}

run_benchmark gRPC adf-grpc
run_benchmark socket adf-socket Unix
run_benchmark socket adf-socket Inet
run_benchmark native adf-def
