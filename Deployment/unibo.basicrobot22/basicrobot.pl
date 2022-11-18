%====================================================================================
% basicrobot description   
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( datacleaner, ctxbasicrobot, "rx.dataCleaner").
  qactor( distancefilter, ctxbasicrobot, "rx.distanceFilter").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( envsonarhandler, ctxbasicrobot, "it.unibo.envsonarhandler.Envsonarhandler").
  qactor( pathexec, ctxbasicrobot, "it.unibo.pathexec.Pathexec").
