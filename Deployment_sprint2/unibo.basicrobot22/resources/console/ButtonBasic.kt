package consolegui

import java.awt.Font
import java.awt.Panel
import java.awt.event.ActionListener

class ButtonBasic(p: Panel?, label: String?, listener: ActionListener?) : java.awt.Button(label) {
	init {
		val myFont = Font("Arial", Font.PLAIN, 24)
		setFont(myFont)
		this.addActionListener(listener)
		p!!.add(this)
		p!!.validate()
		p!!.isVisible()
//println("BUTTON BASIC CREATED");
	}

	companion object {
		/**
		 *
		 */
		private val serialVersionUID = 1L
	}
}