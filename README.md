# WasteService-Project
Progetto finale del corso di Ingegneria dei Sistemi Software

Partendo dai [requisiti](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint0/userDocs/resouces/html/TemaFinale22.html) forniti dal committente, il team ha sviluppato il progetto seguendo un approccio Agile basato sul framework Scrum. In particolare sono stati eseguiti quattro Sprint.

* [Sprint0](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint0/userDocs/sprint0.html)
* [Sprint1](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint1/userDocs/sprint1.html)
* [Sprint2](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint2/userDocs/sprint2.html)
* [Sprint3](https://htmlpreview.github.io/?https://github.com/EnricoValastro/WasteService-Project/blob/main/Sprint3/userDocs/sprint3.html)

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
