%====================================================================================
% wasteservietest description   
%====================================================================================
context(ctxwasteservice, "localhost",  "TCP", "8055").
 qactor( wasteservice, ctxwasteservice, "it.unibo.wasteservice.Wasteservice").
