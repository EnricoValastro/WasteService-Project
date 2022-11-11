%====================================================================================
% wasteservice description   
%====================================================================================
context(ctxbasicrobot, "robot",  "TCP", "8020").
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( pathexec, ctxbasicrobot, "external").
  qactor( wasteservice, ctxwasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolleycore, ctxwasteservice, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxwasteservice, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxwasteservice, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").
  qactor( systemstatemanager, ctxwasteservice, "it.unibo.systemstatemanager.Systemstatemanager").
