FROM ubuntu:latest
LABEL authors="poerl"

ENTRYPOINT ["top", "-b"]