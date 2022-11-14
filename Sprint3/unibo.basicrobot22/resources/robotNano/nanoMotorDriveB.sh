#!/bin/bash
# -----------------------------------------------------------------
# nanoMotorDrive.sh
# test for nano0
# Key-point: we can manage a GPIO pin  by using the GPIO library.
# On a OC, edit this file as UNIX
# -----------------------------------------------------------------

in1=10 #WPI 12  BCM 10 PHYSICAL 19
in2=9  #WPI 13  BCM 9  PHYSICAL 21

inwp1=12  #WPI 12  BCM 10 PHYSICAL 19
inwp2=13  #WPI 13  BCM 9  PHYSICAL 21

 
if [ -d /sys/class/gpio/gpio10 ]
then
 echo "in1 gpio${in1} exist"
 gpio export ${in1} out
else
 echo "creating in1 gpio${in1}"
 gpio export ${in1} out
fi

if [ -d /sys/class/gpio/gpio9 ]
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
