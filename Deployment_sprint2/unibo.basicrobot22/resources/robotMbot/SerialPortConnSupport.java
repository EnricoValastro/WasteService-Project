package robotMbot;

import java.util.ArrayList;
import java.util.List;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.serial.SerialConnection;

public class SerialPortConnSupport extends SerialConnection implements SerialPortEventListener{
final static int SPACE_ASCII = 32;
final static int DASH_ASCII = 45;
final static int NEW_LINE_ASCII = 10;
final static int CR_ASCII = 13;
	
private SerialPort serialPort;
private List<String> list;
private String curString = "";

	public SerialPortConnSupport( SerialPort serialPort  ) {
 		super( serialPort );
		init();
	}	
	protected void init( ){
		try {
			serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
			list          = new ArrayList<String>();			
 		} catch (Exception e) {
 			e.printStackTrace();
		}		
	}

	public void sendALine(String msg) throws Exception {
		//msg = msg+"\n";
		//System.out.println("SerialPortConnSupport sendALine ... " + msg);
  		serialPort.writeBytes(msg.getBytes());
  		 
 //		System.out.println("SerialPortConnSupport has sent   " + msg);
	}
	//EXTENSION for mBot
	public void sendCmd(String msg) throws Exception {
		System.out.println("SerialPortConnSupport sendCmd ... " + msg);
  		serialPort.writeBytes(msg.getBytes()); 		 
 	}
	public void sendCmd(byte[] cmd) throws Exception {
  		serialPort.writeBytes(cmd);
 	}

	public void sendALine(String msg, boolean isAnswer) throws Exception {
		sendALine( msg );		
 	}	

	public synchronized String receiveALine() throws Exception { 	
		//System.out.println(" SerialPortConnSupport receiveALine ...  " );
 		String result = "no data";
 		while( list.size() == 0 ){
// 			println("list empty. I'll wait" );
 			wait();
 		} 
		result = list.remove(0);
 		//System.out.println(" SerialPortConnSupport receiveALine " + result);
		return result;
  	}

	public void closeConnection() throws Exception {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.closePort();
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {		
		 if(event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String data  = serialPort.readString(event.getEventValue());   
                String[] ds  = data.split("\n");
                if( ds.length > 0 ) data = ds[0]; else return;  
                //println("SerialPortConnSupport serialEvent ... " + data );                                           
//	            this.notifyTheObservers(data);  //an observer can see the received data as it is
	            updateLines(data+"\n");
               }
            catch (SerialPortException ex) {
            	System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }		
	}
	
	protected synchronized void updateLines(String data){
		//System.out.println("SerialPortConnSupport updateLines ... " + data.endsWith("\n") );    
		if( data.length()>0) {
            curString = curString + data;
            if( data.endsWith("\n")    ) {
  	 			//System.out.println("updateLines curString= " +  curString + " / " + curString.length());
				list.add(curString);
				this.notifyAll();
				curString = "";
			}
		}
	}

	@Override
	public void forward(String s) throws Exception {

	}

	@Override
	public String request(String s) throws Exception {
		return null;
	}

	@Override
	public void reply(String s) throws Exception {

	}

	@Override
	public String receiveMsg() throws Exception {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}
