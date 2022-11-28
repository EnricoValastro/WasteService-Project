FROM openjdk:11.0.2 
EXPOSE 8055
ADD ./build/distributions/unibo.wasteservice-1.0.tar  / 
 
WORKDIR /unibo.wasteservice-1.0/bin 
COPY ./*.pl ./ 
COPY ./*.json ./
RUN ls 
CMD ["bash", "unibo.wasteservice"]
