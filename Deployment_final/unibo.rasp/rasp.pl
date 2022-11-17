%====================================================================================
% rasp description   
%====================================================================================
context(ctxwasteservice, "wasteservice",  "TCP", "8055").
context(ctxpi, "localhost",  "TCP", "8065").
 qactor( sonardatasource, ctxpi, "sonarSupport2022").
  qactor( datacleaner, ctxpi, "dataCleaner").
  qactor( led, ctxpi, "it.unibo.led.Led").
  qactor( sonarqak22varesi, ctxpi, "it.unibo.sonarqak22varesi.Sonarqak22varesi").
