%====================================================================================
% wasteservicesystem description   
%====================================================================================
context(ctxpi, "127.0.0.2",  "TCP", "8033").
context(ctxrobot, "127.0.0.3",  "TCP", "8034").
context(ctxtruck, "127.0.0.4",  "TCP", "8035").
context(ctxserver, "127.0.0.5",  "TCP", "8036").
context(ctxwasteservice, "localhost",  "TCP", "8050").
 qactor( basicrobot, ctxrobot, "external").
  qactor( wasteservicestatusgui, ctxserver, "it.unibo.wasteservicestatusgui.Wasteservicestatusgui").
  qactor( wasteservice, ctxwasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctxwasteservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( led, ctxpi, "it.unibo.led.Led").
  qactor( sonar, ctxpi, "it.unibo.sonar.Sonar").
  qactor( smartdevice, ctxtruck, "it.unibo.smartdevice.Smartdevice").
