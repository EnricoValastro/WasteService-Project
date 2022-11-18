FROM hypriot/rpi-java
EXPOSE 8055
## ADD extracts the tar
ADD ./build/distributions/unibo.rasp-1.0.tar /
WORKDIR /unibo.rasp-1.0/bin
COPY ./*.pl ./
COPY ./*.json ./
COPY ./SonarAlone ./
COPY ./*.sh ./

RUN ls
CMD ["sh", "unibo.rasp"]
