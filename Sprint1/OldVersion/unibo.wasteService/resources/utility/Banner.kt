package utility

import com.google.gson.Gson
import wasteservice.state.ServiceAreaState

class Banner {

    companion object{

        fun printBannerWasteService(){
            println("\n\n\n\n")
            print(" _       __           __      _____                 _         \n" +
                    "| |     / /___ ______/ /____ / ___/___  ______   __(_)_______ \n" +
                    "| | /| / / __ `/ ___/ __/ _ \\\\__ \\/ _ \\/ ___/ | / / / ___/ _ \\\n" +
                    "| |/ |/ / /_/ (__  ) /_/  __/__/ /  __/ /   | |/ / / /__/  __/\n" +
                    "|__/|__/\\__,_/____/\\__/\\___/____/\\___/_/    |___/_/\\___/\\___/ \n" +
                    "                                                              ")
            println("\n\n\n\n")
        }
    }
}