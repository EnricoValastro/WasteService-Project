package consolegui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.ImageIcon

object Utils {
	fun showSystemInfo(msg : String="at start") {
		println(
			"Utils $msg | COMPUTER memory= ${Runtime.getRuntime().totalMemory()} " +
					" num of processors=${Runtime.getRuntime().availableProcessors()}" +
					" num of threads=" + Thread.activeCount() + " currentThread=" + Thread.currentThread()
		)
	}

	@JvmOverloads
	fun initFrame(dx: Int = 400, dy: Int = 200): Frame? {
		val frame = Frame()
		val img = ImageIcon("./resources/consolegui/mbot-S.jpg")
		frame!!.setIconImage(img!!.getImage())
		val layout = BorderLayout()
		frame!!.setSize(Dimension(dx, dy))
		frame!!.setLayout(layout)

		frame!!.addWindowListener(
			object : WindowListener{
				override fun windowOpened(e: WindowEvent?) {}
				override fun windowIconified(e: WindowEvent?) {}
				override fun windowDeiconified(e: WindowEvent?) {}
				override fun windowDeactivated(e: WindowEvent?) {}
				override fun windowClosing(e: WindowEvent?) {
					System.exit(0)
				}
				override fun windowClosed(e: WindowEvent?) {}
				override fun windowActivated(e: WindowEvent?) {}
			}
		)
		frame!!.setVisible(true)
		return frame
	}

	fun delay(n: Long) {
		try {
			Thread.sleep(n)
		} catch (e: InterruptedException) {
			e!!.printStackTrace()
		}
	}
}