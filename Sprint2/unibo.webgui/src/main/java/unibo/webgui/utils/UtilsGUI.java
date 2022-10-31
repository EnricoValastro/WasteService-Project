package unibo.webgui.utils;

import unibo.comm22.coap.CoapConnection;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;

public class UtilsGUI {

    private static Interaction2021 conn;

    public static CoapConnection connectWithUtilsUsingCoap(String addr){
        try{
            CommSystemConfig.tracing = true;
            String ctx = "ctxwasteservice";
            String qak = "containermanager";
            String path = ctx+"/"+qak;
            conn = new CoapConnection(addr, path);
            ColorsOut.out("UtilsGUI | connect Tcp conn:" + conn );
            ColorsOut.outappl("UtilsGUI | connect Coap conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("UtilsGUI | connectWithUtilsUsingTcp ERROR:"+e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public static void connectWithUtilsUsingTcp(String addr){
        try {
            CommSystemConfig.tracing = true;
            String x[] = addr.split(":");
            conn = TcpClientSupport.connect(x[0], Integer.parseInt(x[1]), 10);
            ColorsOut.out("UtilsGUI | connect Tcp conn:" + conn );
            ColorsOut.outappl("UtilsGUI | connect Tcp conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("UtilsGUI | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
    }
}
