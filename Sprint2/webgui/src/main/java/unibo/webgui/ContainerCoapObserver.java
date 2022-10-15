package unibo.webgui;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;

public class ContainerCoapObserver implements CoapHandler {

    @Override
    public void onLoad(CoapResponse response) {
        ColorsOut.outappl("ContainerCoapObserver changed!" + response.getResponseText(), ColorsOut.GREEN);
        //send info over the websocket
        WebSocketConfiguration.wshandler.sendToAll(""+response.getResponseText());
    }

    @Override
    public void onError() {
        ColorsOut.outerr("ContainerCoapObserver observe error!");
    }
}
