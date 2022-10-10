%====================================================================================
% wasteservice description   
%====================================================================================
context(ctxwasteservice, "localhost",  "TCP", "8055").
context(ctxtransporttrolley, "127.0.0.1",  "TCP", "8056").
 qactor( transportrolleycore, ctxtransporttrolley, "external").
  qactor( containermanager, ctxwasteservice, "it.unibo.containermanager.Containermanager").
  qactor( wasteservicehandler, ctxwasteservice, "it.unibo.wasteservicehandler.Wasteservicehandler").
  qactor( wasteservicecore, ctxwasteservice, "it.unibo.wasteservicecore.Wasteservicecore").
