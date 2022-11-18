#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>

//#define TRUE 1
//Wiring Pi numbers for radar with stepper
#define TRIG 0  //4
#define ECHO 2  //5

using namespace std;
/*
 * in the directory: of SonarAlone.c:
1)  [ sudo ../../pi-blaster/pi-blaster ] if servo
2)  g++  SonarAlone.c -l wiringPi -o  SonarAlone
sudo ./SonarAlone
In nat/servosonar:
sudo java -jar   SonarAloneP2PMain.jar
sudo python radar.py
 */
void setup() {
	wiringPiSetup();
	pinMode(TRIG, OUTPUT);
	pinMode(ECHO, INPUT);

	//TRIG pin must start LOW
	digitalWrite(TRIG, LOW);
	delay(30);
}

int getCM() {
	//Send trig pulse
	digitalWrite(TRIG, HIGH);
	delayMicroseconds(20);
	digitalWrite(TRIG, LOW);

	//Wait for echo start
	while(digitalRead(ECHO) == LOW);

	//Wait for echo end
	long startTime = micros();
	while(digitalRead(ECHO) == HIGH);
	long travelTime = micros() - startTime;

	//Get distance in cm
	int distance = travelTime / 58;

 	return distance;
}

int main(void) {
	int cm ;	
 	setup();
	while(1) {
		cm = getCM();
		cout <<  cm <<   endl;
		delay(300);
	}
	return 0;
}
