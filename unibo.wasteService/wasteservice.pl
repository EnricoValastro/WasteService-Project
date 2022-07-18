%====================================================================================
% wasteservice description   
%====================================================================================
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( wasteserviceactor, ctxwasteservice, "it.unibo.wasteserviceactor.Wasteserviceactor").
  qactor( wastetruckactor, ctxwasteservice, "it.unibo.wastetruckactor.Wastetruckactor").
