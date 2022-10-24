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
import unibo.utils.ContainerUtils;

@Controller
public class IndexController {
    protected String mainPage = "webGUI";

    @Value("${context.containertip}")
    String containertip;

    @Value("${container.plastic}")
    String plastic;

    @Value("${container.glass}")
    String glass;

    protected String buildThePage(Model viewmodel){
        setConfigParams(viewmodel);
        return mainPage;
    }
    protected void setConfigParams(Model viewmodel) {
        viewmodel.addAttribute("containertip", containertip);
        viewmodel.addAttribute("plastic", plastic);
        viewmodel.addAttribute("glass", glass);
    }

    @GetMapping("/")
    public String entry(Model viewmodel) {
        return buildThePage(viewmodel);
    }

    @PostMapping("/update")
    public String setUpdate(Model viewmodel, @RequestParam String ipaddr){
        containertip = ipaddr;
        viewmodel.addAttribute("containertip", containertip);
        ContainerUtils.connectWithContainerUsingTcp(ipaddr);
        CoapConnection conn = ContainerUtils.connectWithContainerUsingCoap(ipaddr);
        conn.observeResource(new ContainerCoapObserver());
        ContainerUtils.sendMsg();
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
