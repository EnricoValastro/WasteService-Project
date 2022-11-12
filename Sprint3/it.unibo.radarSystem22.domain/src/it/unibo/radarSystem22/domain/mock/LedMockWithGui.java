package it.unibo.radarSystem22.domain.mock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.BasicUtils;


public class LedMockWithGui extends LedMock {  
	private static int delta        = 0;

private Panel p ; 
private Frame frame;
private final Dimension sizeOn  = new Dimension(100,100);
private final Dimension sizeOff = new Dimension(30,30);
	public static ILed createLed(  ){
		LedMockWithGui led = new LedMockWithGui( initFrame(150,150) );
		return led;
	}
	public void destroyLedGui(  ){
		frame.dispose();
	}
 	//Constructor
	public LedMockWithGui( Frame frame ) {
		super();
		//Colors.out("create LedMockWithGui");
		this.frame = frame;
 		configure( );
  	}	
	protected void configure( ){
		p = new Panel();
		p.setSize( sizeOff );
		p.setBackground(Color.red);
		frame.add(BorderLayout.CENTER,p);
		delta = delta+50;
		frame.setLocation(delta, delta);
 		//p.validate();
 		//this.frame.validate();
  	}
	@Override //LedMock
	public void turnOn(){
		super.turnOn();
		p.setSize( sizeOn );
		p.setBackground(Color.red);
		p.validate();
	}
	@Override //LedMock
	public void turnOff() {
		super.turnOff();
 		p.setSize( sizeOff );
		p.setBackground(Color.GRAY);
		p.validate();
		//p.revalidate();
	}
	
//
	public static Frame initFrame(int dx, int dy){
 		Frame frame         = new Frame();
 		BorderLayout layout = new BorderLayout();
 		frame.setSize( new Dimension(dx,dy) );
 		frame.setLayout(layout);		
 		frame.addWindowListener(new WindowListener() {			
			@Override
			public void windowOpened(WindowEvent e) {}				
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);				
			}			
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		}); 	
		frame.setVisible(true);
		return frame;
		
	}
 	public static Frame initFrame(){
 		return initFrame(400,200);
 	}
}
