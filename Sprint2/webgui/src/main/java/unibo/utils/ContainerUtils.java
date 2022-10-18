package unibo.utils;

import it.unibo.kactor.IApplMessage;
import unibo.comm22.coap.CoapConnection;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;
import unibo.comm22.utils.CommUtils;
import unibo.webgui.ContainerCoapObserver;

public class ContainerUtils {
    private static Interaction2021 conn;

    private static Interaction2021 connTCP;

    public static void connectWithContainerUsingTcp(String addr) {
        try {
            String x[] = addr.split(":");
            int y = Integer.parseInt(x[1]);
            CommSystemConfig.tracing = true;
            connTCP = TcpClientSupport.connect(x[0], y, 10);
            ColorsOut.out("ContainerUtils | connect Tcp conn:" + connTCP);
            ColorsOut.outappl("ContainerUtils | connect Tcp conn:" + connTCP, ColorsOut.CYAN);
        } catch (Exception e) {
            ColorsOut.outerr("ContainerUtils | connectWithContainerUsingTcp ERROR:" + e.getMessage());
        }
    }
    public static CoapConnection connectWithContainerUsingCoap(String addr){
        try {
            CommSystemConfig.tracing = true;
            String ctxqakdest       = "ctxwasteservice";
            String qakdestination 	= "containermanager";
            String path   = ctxqakdest+"/"+qakdestination;
            conn           = new CoapConnection(addr, path);
            ColorsOut.out("ContainerUtils | connect Tcp conn:" + conn );
            ColorsOut.outappl("ContainerUtils | connect Coap conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("ContainerUtils | connectWithContainerUsingTcp ERROR:"+e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public static void sendMsg() {
        try {
            IApplMessage msg = CommUtils.buildDispatch("webgui", "getDatatoG", "getDatatoG(_)", "containermanager");
            ColorsOut.outappl("ContainerUtils | sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg.toString());
            conn.forward(msg.toString());
        } catch (Exception e) {
            ColorsOut.outerr("ContainerUtils | sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }
}
