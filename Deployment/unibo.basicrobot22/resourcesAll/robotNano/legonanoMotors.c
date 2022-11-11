#include <iostream>
#include <wiringPi.h>
#include <fstream>
#include <cmath>
#include <stdio.h>

/*
g++  legonanoMotors.c -l wiringPi -o  Motors
 */
 
// #define TRUE 1   //alredy done in wiringPi
#define inp1m1 8
#define inp2m1 9

#define inp1m2 12
#define inp2m2 13

using namespace std;

int rotLeftTime  = 610;
int rotRightTime = 610;
int rotStepTime  = 58;



void h(){
	digitalWrite(inp1m1, LOW);
	digitalWrite(inp2m1, LOW);
	digitalWrite(inp1m2, LOW);
	digitalWrite(inp2m2, LOW);
}

void w(){
	digitalWrite(inp1m1, HIGH);
	digitalWrite(inp2m1, LOW);
	digitalWrite(inp1m2, LOW);
	digitalWrite(inp2m2, HIGH);
}
void s(){
	digitalWrite(inp1m1, LOW);
	digitalWrite(inp2m1, HIGH);
	digitalWrite(inp1m2, HIGH);
	digitalWrite(inp2m2, LOW);
}
void a(){
	digitalWrite(inp1m1, HIGH);
	digitalWrite(inp2m1, LOW);
	digitalWrite(inp1m2, HIGH);
	digitalWrite(inp2m2, LOW);
}
void d(){
	digitalWrite(inp1m1, LOW);
	digitalWrite(inp2m1, HIGH);
	digitalWrite(inp1m2, LOW);
	digitalWrite(inp2m2, HIGH);
}
void r(){
	d();
	delay(rotRightTime);
	h();
}
void l(){
	a();
	delay(rotLeftTime);
	h();
}
void z(){
	d();
	delay(rotStepTime);
	h();
}
void x(){
	a();
	delay(rotStepTime);
	h();
}

void configureRotationTime(){
}


void setup() {
	cout << "motorsC setUp STARTS" << endl;
	wiringPiSetup();
	pinMode(inp1m1, OUTPUT);
	pinMode(inp2m1, OUTPUT);
	pinMode(inp1m2, OUTPUT);
	pinMode(inp2m2, OUTPUT);
	h();
 	delay(30);
	cout << "motorsC setUp ENDS" << endl;
}

void remoteCmdExecutor(){
int input = 'h';
        input = getchar( );
        if( input != 10 ){
	        cout << input << endl;        
	        switch( input ){
	          case 99  : configureRotationTime(); break;  //c... | cl0.59 or cr0.59  or cx0.005 or cz0.005
	          case 119 : w(); break;  //w		ruota continuo sinistra
	          case 115 : s(); break;  //s		ruota continuo destra
	          case 97  : a(); break;  //a		ruota continuo destra
	          case 122 : z(); break;  //z		sinistra
	          case 120 : x(); break;  //x		destra
	          case 100 : d(); break;  //d		ruota continuo sinistra
	          case 104 : h();  break;  //h
	          case 114 : r();  break;  //r		l
	          case 108 : l(); break;    //l		r
	          case 10  :  break;    //lf
	          //case 102 : break;  //f
	          default  : h();
	        }
        }
}

int main(void) {
  	setup();
	while( 1 ) 	remoteCmdExecutor();
	//return 0;
}
