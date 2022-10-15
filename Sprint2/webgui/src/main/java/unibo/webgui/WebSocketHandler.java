package unibo.webgui;

import org.json.JSONObject;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import unibo.comm22.utils.ColorsOut;
import unibo.utils.IWsHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketHandler extends AbstractWebSocketHandler implements IWsHandler{
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        ColorsOut.out("WebSocketHandler | Added the session:" + session, ColorsOut.MAGENTA);
        super.afterConnectionEstablished(session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        ColorsOut.out("WebSocketHandler | Removed the session:" + session, ColorsOut.MAGENTA);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        ColorsOut.outappl("WebSocketHandler | handleTextMessage Received: " + message, ColorsOut.GREEN);
        session.sendMessage(message);
    }

    public void sendToAll(String message)  {
        try{
            ColorsOut.outappl("WebSocketHandler | sendToAll String: " + message, ColorsOut.CYAN);
            sendToAll( new TextMessage(message)) ;
        }catch( Exception e ){
            ColorsOut.outerr("WebSocketHandler | sendToAll String ERROR:"+e.getMessage());
        }
    }
    public void sendToAll(TextMessage message) {
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            try{
                WebSocketSession session = iter.next();
                ColorsOut.outappl("WebSocketHandler | sendToAll " +
                        message.getPayload() + " for session " + session.getRemoteAddress() , ColorsOut.MAGENTA);
                //synchronized(session){
                session.sendMessage(message);
                //}
            }catch(Exception e){
                ColorsOut.outerr("WebSocketHandler | TextMessage ERROR:"+e.getMessage());
            }
        }
    }
}