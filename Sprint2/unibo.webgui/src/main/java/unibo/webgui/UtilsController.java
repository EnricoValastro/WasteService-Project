package unibo.webgui;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import unibo.webgui.utils.UtilsGUI;


@Controller
public class UtilsController{

    protected String mainPage = "webGUI";

    protected String updatePage = "update";

    String addr;
    String glasscurrent;
    String plasticcurrent;
    String glassmax;
    String plasticmax;
    String trolleystate;
    String trolleyposition;
    String ledstate;

    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }

    protected String buildTheUpdatePage(Model viewmodel){
        setConfigParams(viewmodel);
        return updatePage;
    }

    protected void setConfigParams(Model viewmodel){
        viewmodel.addAttribute("addr", addr);
        viewmodel.addAttribute("glasscurrent", glasscurrent);
        viewmodel.addAttribute("plasticcurrent",  plasticcurrent);
        viewmodel.addAttribute("glassmax",  glassmax);
        viewmodel.addAttribute("plasticmax",  plasticmax);
        viewmodel.addAttribute("trolleystate",  trolleystate);
        viewmodel.addAttribute("trolleyposition",  trolleyposition);
        viewmodel.addAttribute("ledstate", ledstate);
    }

    @GetMapping("/")
    public String entry(Model viewmodel) {
        return buildThePage(viewmodel);
    }

    @PostMapping("/update")
    public String update(Model viewmodel, @RequestParam String ipaddr  ){
        addr = ipaddr;
        viewmodel.addAttribute("addr", addr);
        UtilsGUI.connectWithUtilsUsingTcp(ipaddr);
        UtilsGUI.connectWithUtilsUsingCoap(ipaddr).observeResource(new UtilsCoapObserver());
        UtilsGUI.sendMsg();
        return buildTheUpdatePage(viewmodel);
    }
}
