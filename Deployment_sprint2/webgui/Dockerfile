FROM openjdk:11.0.14
EXPOSE 8085
ADD ./build/distributions/webgui-boot-2.0.tar  /
WORKDIR /webgui-boot-2.0/bin
COPY ./*.json ./

RUN ls
CMD ["bash", "webgui"]
