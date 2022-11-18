#!/bin/bash
# -----------------------------------------------------------------
# nanoMotorDrive.sh
# test for nano0
# Key-point: we can manage a GPIO pin  by using the GPIO library.
# On a PC, edit this file as UNIX
# -----------------------------------------------------------------

in1=2 #WPI 8 BCM 2  PHYSICAL 3
in2=3 #WPI 9 BCM 3  PHYSICAL 5
inwp1=8   
inwp2=9   
 
if [ -d /sys/class/gpio/gpio2 ]
then
 echo "in1 gpio${in1} exist"
 gpio export ${in1} out
else
 echo "creating in1 gpio${in1}"
 gpio export ${in1} out
fi

if [ -d /sys/class/gpio/gpio3 ]
then
 echo "in2 gpio${in2} exist"
 gpio export ${in2} out
else
 echo "creating in2  gpio${in2}"
 gpio export ${in2} out
fi

gpio readall

echo "run 1"
 gpio write ${inwp1} 0
 gpio write ${inwp2} 1
 sleep 1.5

echo "run 2"
 gpio write ${inwp1} 1
 gpio write ${inwp2} 0
 sleep 1.5

echo "stop"
 gpio write ${inwp1} 0
 gpio write ${inwp2} 0

gpio readall
