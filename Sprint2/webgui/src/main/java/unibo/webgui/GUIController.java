package unibo.webgui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.comm22.coap.CoapConnection;
import unibo.utils.UtilsGUI;

@Controller
public class GUIController {

    protected String mainPage = "webGUI";

    @Value("${webgui.addr}")
    String addr;
    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }

    protected void setConfigParams(Model viewmodel){
        viewmodel.addAttribute("addr", addr);

    }

    @GetMapping("/")
    public String entry(Model viewmodel) {
        return buildThePage(viewmodel);
    }

    @PostMapping("/update")
    public String update(Model viewmodel, @RequestParam String ipaddr) {
        addr = ipaddr;
        viewmodel.addAttribute("addr", addr);
        CoapConnection conn = UtilsGUI.connectWithUtilsUsingCoap(addr);
        conn.observeResource(new GUICoapObserver());
        return buildThePage(viewmodel);
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
}
