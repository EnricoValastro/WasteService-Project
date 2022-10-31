package unibo.webgui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.comm22.coap.CoapConnection;
import unibo.webgui.utils.UtilsGUI;

@Controller
public class UtilsController{

    protected String mainPage = "webGUI";

    @Value("${webgui.addr}")
    String addr;

    @Value("${container.glasscurrent}")
    String glasscurrent;

    @Value("${container.plasticcurrent}")
    String plasticcurrent;

    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }

    protected void setConfigParams(Model viewmodel){
        viewmodel.addAttribute("addr", addr);
        viewmodel.addAttribute("glasscurrent", glasscurrent);
        viewmodel.addAttribute("plasticcurrent",  plasticcurrent);
    }

    @GetMapping("/")
    public String entry(Model viewmodel) {
        return buildThePage(viewmodel);
    }

    @PostMapping("/update")
    public String update(Model viewmodel, @RequestParam String ipaddr  ){
        addr = ipaddr;
        viewmodel.addAttribute("addr", addr);
        CoapConnection conn = UtilsGUI.connectWithUtilsUsingCoap(ipaddr);
        conn.observeResource( new UtilsCoapObserver() );
        return buildThePage(viewmodel);
    }
}
