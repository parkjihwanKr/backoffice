#!/bin/bash

output_file="redis_commands.txt"

# 기존 파일 삭제
> "$output_file"

for boardId in {20..300}; do
  for memberId in {10..120}; do
    echo "SET boardId:${boardId}:memberId:${memberId} 0" >> "$output_file"
    echo "INCR boardId:${boardId}:memberId:${memberId}" >> "$output_file"
    echo "INCR boardId:${boardId}:memberId:${memberId}" >> "$output_file"
    echo "INCR boardId:${boardId}:memberId:${memberId}" >> "$output_file"
  done
done