# WasteService-Project
Progetto finale del corso di Ingegneria dei Sistemi Software

Partendo dai [requisiti](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint0/userDocs/resouces/html/TemaFinale22.html) forniti dal committente, il team ha sviluppato il progetto seguendo un approccio Agile basato sul framework Scrum. In particolare sono stati eseguiti quattro Sprint.

* [Sprint0](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint0/userDocs/sprint0.html)
* [Sprint1](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint1/userDocs/sprint1.html)
* [Sprint2](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint2/userDocs/sprint2.html)
* [Sprint3](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint3/userDocs/sprint3.html)

## Demo

https://user-images.githubusercontent.com/79710717/205872727-915a6720-cf2e-44b9-aab4-7bc880811816.mp4

## Start the System
> Start smartdevice
```bash
  cd WasteService-Project/Sprint1/unibo.smartdevice
  python3 smartdevice.py
```

> Start WasteServiceStatusGUI
```bash
  cd WasteService-Project/Sprint2/webgui
  ./gradlew run
```
Open a browser(Firefox) and connect to `http://localhost:8085`

>Start basicorobot22 and virtualRobot2020
```bash
  cd WasteService-Project/Deployment/
  docker-compose -f basicrobot22.yaml up
```
Open a browser(Firefox) and connect to `http://localhost:8090`

>Start WasteService
```bash
  cd WasteService-Project/Deployment/
  docker-compose -f wasteservice.yaml up
```
>Start RaspberryPi Software

On a raspberryPi connect a led and a HCSR04 sonar, upload on raspberry the [software](https://github.com/EnricoValastro/WasteService-Project/tree/main/Sprint3/unibo.rasp), compile the SonarAlone.c file and start the software.
```bash
  ssh pi@raspberrypi.local
  cd unibo.rasp-1.0/bin
  g++ SonarAlone.c -l wiringPi -o SonarAlone
  ./unibo.rasp
```

