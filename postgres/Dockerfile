FROM postgres:15-alpine

COPY postgresql.conf /var/lib/postgresql/data/postgresql.conf

CMD ["postgres", "-c", "config_file=/var/lib/postgresql/data/postgresql.conf"]
