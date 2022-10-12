%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctxwasteservice, "127.0.0.1",  "TCP", "8055").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxtransporttrolley, "localhost",  "TCP", "8056").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( pathexec, ctxbasicrobot, "external").
  qactor( wasteservicecore, ctxwasteservice, "external").
  qactor( transporttrolleycore, ctxtransporttrolley, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxtransporttrolley, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxtransporttrolley, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").
