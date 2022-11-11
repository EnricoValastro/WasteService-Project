/**
 * ClientWebsockJavax
 *
 * @author AN - DISI - Unibo
 */

package it.unibo.wenv;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@ClientEndpoint
public class ClientWebsockJavax {

    Session userSession    = null;
    private MessageHandler messageHandler;
    private org.json.simple.parser.JSONParser simpleparser ;
    /**
     * Message handler.
     *
     * @author AN - DISI - Unibo
     */
    public static interface MessageHandler {
        public void handleMessage(String message) throws ParseException;
    }

    public ClientWebsockJavax(String addr) {
            System.out.println("ClientWebsockJavax |  CREATING ...");
            init(addr);
    }

    protected void init(String addr){
        try {
            simpleparser = new JSONParser();
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            System.err.println("ClientWebsockJavax | URISyntaxException exception: " + ex.getMessage());
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("ClientWebsockJavax | opening websocket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("ClientWebsockJavax | closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) throws ParseException {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message ) throws Exception {
        System.out.println("ClientWebsockJavax | sendMessage " + message);
        //this.userSession.getAsyncRemote().sendText(message);
        this.userSession.getBasicRemote().sendText(message);    //synch: blocks until the message has been transmitted
    }

/*
BUSINESS LOGIC
 */
public interface IGoon  {
    public void nextStep( boolean collision ) throws Exception;
}

    protected void setObserver( IGoon goon ){
        // add listener
        addMessageHandler( new MessageHandler() {
            public void handleMessage(String message) {
                try {
                    //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
                    System.out.println("ClientWebsockJavax | handleMessage:" + message);
                    org.json.simple.JSONObject jsonObj = (JSONObject) simpleparser.parse(message);
                    if (jsonObj.get("endmove") != null) {
                        boolean endmove = jsonObj.get("endmove").toString().equals("true");
                        String  move    = jsonObj.get("move").toString();
                        System.out.println("ClientWebsockJavax | handleMessage " + move + " endmove=" + endmove);
                        if( endmove ) goon.nextStep(false);
                    } else if (jsonObj.get("collision") != null) {
                        boolean collision = jsonObj.get("collision").toString().equals("true");
                        String move = jsonObj.get("move").toString();
                        System.out.println("ClientWebsockJavax | handleMessage collision=" + collision + " move=" + move);
                        //if( ! move.equals("unknown") )
                        goon.nextStep(collision);
                    } else if (jsonObj.get("sonarName") != null) {
                        String sonarNAme = jsonObj.get("sonarName").toString();
                        String distance = jsonObj.get("distance").toString();
                        System.out.println("ClientWebsockJavax | handleMessage sonaraAme=" + sonarNAme + " distance=" + distance);
                    }

                } catch (Exception e) {
                }
            }});
        };//setObserver

    private int count = 0;

    public void doJob() throws Exception {
        setObserver( new IGoon() {
            @Override
            public void nextStep( boolean collision ) throws Exception {
                //System.out.println(" %%% nextStep collision=" + collision + " count=" + count);
                if (count > 4) {
                    System.out.println("ClientWebsockJavax | BYE (from nextStep)" );
                    return;
                }
                //Thread.sleep(500) ;   //interval before the next move
                //System.in.read();
                 if( collision ) {
                    if (count++ <= 4) {
                        //count++;
                        String cmd = "{\"robotmove\":\"turnLeft\" , \"time\": 300}";
                        sendMessage( cmd );
                    }
                } else {  //no collision
                     String cmd = "{\"robotmove\":\"moveForward\" , \"time\": 600}";
                     sendMessage(cmd);
                };

                }
        }); //setObserver
        count = 1;
        sendMessage("{\"robotmove\":\"moveForward\"}");
        System.out.println("ClientWebsockJavax | doJob ENDS ======================================= " );
    }
/*
MAIN
 */
    public static void main(String[] args) {
        try{
            new ClientWebsockJavax("localhost:8091").doJob();
            // wait  for messages from websocket
            Thread.sleep(30000);
        } catch( Exception ex ) {
            System.err.println("ClientWebsockJavax | InterruptedException exception: " + ex.getMessage());
        }
    }

}