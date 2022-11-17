FROM openjdk:11.0.14
EXPOSE 8065
## ADD extracts the tar
ADD ./build/distributions/unibo.rasp-1.0.tar /
WORKDIR /unibo.rasp-1.0/bin
COPY ./*.pl ./
COPY ./*.sh ./
COPY ./SonarAlone ./

RUN apt install -y build-essential g++
RUN apt install ./wiringpi-2.46.deb

RUN ls
##RUN g++ SonarAlone.c -l wiringPi -o SonarAlone 
CMD ["bash", "unibo.rasp"]
