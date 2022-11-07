package utility

object DirectionFixer {

    fun getPathForFixDir(actual : String, expected : String) : String{
        var path = ""
        //println("actual : $actual, expected : $expected")
        when(actual){
            "upDir" -> {
                when(expected){
                    "rightDir" -> path = "r"
                    "leftDir" -> path = "l"
                    "downDir" -> path = "rr"
                }
            }
            "downDir" -> {
                when(expected){
                    "rightDir" -> path = "l"
                    "leftDir" -> path = "r"
                    "upDir" -> path = "rr"
                }
            }
            "leftDir" -> {
                when(expected){
                    "upDir" -> path = "r"
                    "downDir" -> path = "l"
                    "rightDir" -> path = "rr"
                }
            }
            "rightDir" -> {
                when(expected){
                    "upDir" -> path = "l"
                    "downDir" -> path = "r"
                    "leftDir" -> path = "rr"
                }
            }
        }
        return path
    }





}