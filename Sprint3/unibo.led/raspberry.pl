%====================================================================================
% raspberry description   
%====================================================================================
context(ctxwasteservice, "127.0.0.1",  "TCP", "8055").
context(ctxpi, "localhost",  "TCP", "8065").
 qactor( systemstatemanager, ctxwasteservice, "external").
  qactor( led, ctxpi, "it.unibo.led.Led").
