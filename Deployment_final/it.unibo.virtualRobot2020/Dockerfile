## ELIMINARE node-modules dalle dir 
FROM node:17-alpine
RUN mkdir -p /home/node
	
### FROM openjdk:12.0.2		
EXPOSE 8090
EXPOSE 8091

COPY ./node/WEnv/server /home/node/WEnv/server 
COPY ./node/WEnv/WebGLScene /home/node/WEnv/WebGLScene

# set default dir so that next commands executes in it
WORKDIR /home/node/WEnv/WebGLScene
RUN npm install

WORKDIR /home/node/WEnv/server
RUN npm install

WORKDIR /home/node/WEnv/server/src
CMD ["node", "WebpageServer"]

#### docker build -f Dockerfile -t wasteservicerobot:3.0 .
#### docker build --no-cache --rm=false .

## Tag the image
## type docker_password.txt | docker login --username natbodocker --password-stdin
## docker tag virtualrobotdisi:4.0 natbodocker/wasteservicerobot:3.0
## Register the image on DockerHub
## docker push natbodocker/wasteservicerobot:3.0



#### docker run -p 8090:8090 -p 8091:8091 --rm wasteservicerobot:3.0
#### docker run -d -p 8090:8090 -p 8091:8091 --rm wasteservicerobot:3.0   (Detach mode)
#### docker cp sceneConfig.js  		( )
#### docker cp sceneConfig.js 23df308fae4e:/home/node/WEnv/WebGLScene/sceneConfig.js

### docker inspect --format='{{.Id}} {{.Parent}}' $(docker images --filter since=1cc99e24ab2d -q)
### It will kill every children

## docker-compose -f wasteservicerobotOnly:3.0.yaml up