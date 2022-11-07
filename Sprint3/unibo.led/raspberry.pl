%====================================================================================
% raspberry description   
%====================================================================================
context(ctxwasteservice, "127.0.0.1",  "TCP", "8055").
context(ctxpi, "localhost",  "TCP", "8090").
 qactor( sonarsimulator, ctxpi, "sonarSimulator").
  qactor( sonardatasource, ctxpi, "sonarHCSR04Support2021").
  qactor( systemstatemanager, ctxwasteservice, "external").
  qactor( led, ctxpi, "it.unibo.led.Led").
  qactor( sonar, ctxpi, "it.unibo.sonar.Sonar").
