%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctxtransporttrolley, "localhost",  "TCP", "8056").
 qactor( transporttrolleycore, ctxtransporttrolley, "it.unibo.transporttrolleycore.Transporttrolleycore").
  qactor( transporttrolleymover, ctxtransporttrolley, "it.unibo.transporttrolleymover.Transporttrolleymover").
  qactor( transporttrolleyexecutor, ctxtransporttrolley, "it.unibo.transporttrolleyexecutor.Transporttrolleyexecutor").
