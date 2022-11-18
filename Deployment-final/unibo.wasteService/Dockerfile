FROM openjdk:11.0.14
EXPOSE 8055
## ADD extracts the tar
ADD ./build/distributions/unibo.wasteservice-1.0.tar /
WORKDIR /unibo.wasteservice-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./

RUN ls
CMD ["bash", "unibo.wasteservice"]
