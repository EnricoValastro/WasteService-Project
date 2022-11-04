package unibo.webgui.utils;

import it.unibo.kactor.IApplMessage;
import unibo.comm22.coap.CoapConnection;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;
import unibo.comm22.utils.CommUtils;

public class UtilsGUI {

    private static Interaction2021 conn;

    private static Interaction2021 connTCP;

    public static CoapConnection connectWithUtilsUsingCoap(String addr){
        try{
            CommSystemConfig.tracing = true;
            String ctx = "ctxwasteservice";
            String qak = "systemstatemanager";
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
            String x[] = addr.trim().split(":");
            connTCP = TcpClientSupport.connect(x[0], Integer.parseInt(x[1]), 10);
            ColorsOut.out("UtilsGUI | connect Tcp conn:" + conn );
            ColorsOut.outappl("UtilsGUI | connect Tcp conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("UtilsGUI | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
    }

   public static void sendMsg() {

        try {
            String msg = "msg(getdata, dispatch, webgui, systemstatemanager, getdata(_), 1)";
            ColorsOut.outappl("UtilsGUI | sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
            //conn.forward(msg.toString());
        } catch (Exception e) {
            ColorsOut.outerr("UtilsGUI | sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }
}
