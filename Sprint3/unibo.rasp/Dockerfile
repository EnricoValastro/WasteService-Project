FROM hypriot/rpi-java
EXPOSE 8065
## ADD extracts the tar
ADD ./build/libs/unibo.rasp-1.0.jar /
WORKDIR /unibo.rasp-1.0.jar/

COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.sh ./

CMD ["java", "-jar ", "unibo.rasp-1.0.jar"]
