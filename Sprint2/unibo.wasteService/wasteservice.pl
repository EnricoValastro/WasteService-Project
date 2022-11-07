%====================================================================================
% wasteservice description   
%====================================================================================
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( wasteservice, ctxwasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolleycore, ctxwasteservice, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxwasteservice, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxwasteservice, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").
  qactor( systemstatemanager, ctxwasteservice, "it.unibo.systemstatemanager.Systemstatemanager").
