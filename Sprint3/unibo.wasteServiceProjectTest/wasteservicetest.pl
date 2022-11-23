%====================================================================================
% wasteservicetest description   
%====================================================================================
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( sonardatasource, ctxwasteservice, "sonarSupport2022").
  qactor( datacleaner, ctxwasteservice, "dataCleaner").
  qactor( wasteservice, ctxwasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolleycore, ctxwasteservice, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxwasteservice, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxwasteservice, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").
  qactor( systemstatemanager, ctxwasteservice, "it.unibo.systemstatemanager.Systemstatemanager").
  qactor( sonarfilter, ctxwasteservice, "it.unibo.sonarfilter.Sonarfilter").
  qactor( led, ctxwasteservice, "it.unibo.led.Led").
  qactor( sonarqak22varesi, ctxwasteservice, "it.unibo.sonarqak22varesi.Sonarqak22varesi").
  qactor( pathexec, ctxwasteservice, "it.unibo.pathexec.Pathexec").
