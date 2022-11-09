package unibo.webgui;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;

public class UtilsCoapObserver implements CoapHandler{

    @Override
    public void onLoad(CoapResponse response) {
        ColorsOut.outappl("UtilsCoapObserver changed!" + response.getResponseText(), ColorsOut.GREEN);
        WebSocketConfiguration.wshandler.sendToAll(""+response.getResponseText());
    }

    @Override
    public void onError() {
        ColorsOut.outerr("UtilsCoapObserver observe error!");
    }
}
