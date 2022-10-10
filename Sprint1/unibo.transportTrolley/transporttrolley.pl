%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctxtransporttrolley, "localhost",  "TCP", "8056").
context(ctxwasteservice, "127.0.0.1",  "TCP", "8055").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( pathexec, ctxbasicrobot, "external").
  qactor( wasteservicecore, ctxwasteservice, "external").
  qactor( transporttrolleycore, ctxtransporttrolley, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxtransporttrolley, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxtransporttrolley, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").
