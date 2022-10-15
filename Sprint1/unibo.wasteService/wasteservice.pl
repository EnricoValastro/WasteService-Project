%====================================================================================
% wasteservice description   
%====================================================================================
context(ctxtransporttrolley, "127.0.0.1",  "TCP", "8056").
context(ctxtruck, "127.0.0.1",  "TCP", "8066").
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( transporttrolleycore, ctxtransporttrolley, "external").
  qactor( smartdevice, ctxtruck, "external").
  qactor( containermanager, ctxwasteservice, "it.unibo.containermanager.Containermanager").
  qactor( wasteservicehandler, ctxwasteservice, "it.unibo.wasteservicehandler.Wasteservicehandler").
  qactor( wasteservicecore, ctxwasteservice, "it.unibo.wasteservicecore.Wasteservicecore").
