FROM minio/minio

COPY ./preload-data /preload-data
COPY minio-entrypoint.sh /minio-entrypoint.sh
RUN chmod +x /minio-entrypoint.sh

ENTRYPOINT ["/minio-entrypoint.sh"]