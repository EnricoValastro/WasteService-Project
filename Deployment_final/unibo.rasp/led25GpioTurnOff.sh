# -------------------------------------------------------------
# led25GpioTurnOff.sh
# Key-point: we can manage a device connected on a GPIO pin by
# using the GPIO shell library. 
# The pin 25 is physical 22 and Wpi 6.
#	sudo bash led25GpioTurnOff.sh
# -------------------------------------------------------------
gpio mode 6 out #
gpio write 6 0 #