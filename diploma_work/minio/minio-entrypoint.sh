#!/bin/sh
# Start MinIO in the background
/usr/bin/minio server /data --console-address ":9001" &

# Wait for MinIO to start
sleep 5

mc alias set myminio http://localhost:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD"

mc mb myminio/library
mc cp --recursive /preload-data/* myminio/library/
mc anonymous set download myminio/library

# Bring MinIO back to the foreground
wait