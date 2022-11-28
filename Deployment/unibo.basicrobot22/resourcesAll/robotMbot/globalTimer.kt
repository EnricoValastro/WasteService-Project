package robotMbot;

object globalTimer{
    private var timeAtStart: Long = 0

    fun startTimer() {
        timeAtStart = System.currentTimeMillis()
    }

    fun stopTimer(source: String) : Int{
        val duration = (System.currentTimeMillis() - timeAtStart).toInt()
        println("TIME STOPPED in $source = $duration")
        return duration
    }
}