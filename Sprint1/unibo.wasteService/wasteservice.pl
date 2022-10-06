%====================================================================================
% wasteservice description   
%====================================================================================
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( containermanager, ctxwasteservice, "it.unibo.containermanager.Containermanager").
  qactor( wasteservicehandler, ctxwasteservice, "it.unibo.wasteservicehandler.Wasteservicehandler").
  qactor( wasteservicecore, ctxwasteservice, "it.unibo.wasteservicecore.Wasteservicecore").
