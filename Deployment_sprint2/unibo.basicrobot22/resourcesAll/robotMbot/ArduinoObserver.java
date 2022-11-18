package robotMbot;
import java.util.Observable;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import it.unibo.is.interfaces.IObserver;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.supports.serial.ISerialPortInteraction;
import it.unibo.system.SituatedPlainObject;

public class ArduinoObserver extends SituatedPlainObject implements IObserver, SerialPortEventListener{
protected ISerialPortInteraction portConn;
protected String curInput = "";
protected int n = 0;
  
	public ArduinoObserver(ISerialPortInteraction portConn,IOutputView outView) {
		super(outView);
		this.portConn = portConn;
 	}
	@Override
	public void update(Observable arg0, Object inputLine) {
		try {
			n++;
			String content = "value(" + n + "," + inputLine + ")";
			System.out.println(content);
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		//println("oEvent " + oEvent.getSource());
//		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		  println("oEvent type=" + oEvent.getEventType());
 		  try {
			 /*
			  * no read => event not consumed
			  */
			 //println("ArduinoObserver curLine=" + curLine );
			 String input = portConn.receiveALine();
			 update( this, input);
			 if( input.contains("BY")) portConn.closeConnection(); 
 			 //println("ArduinoObserver input="+inputLine);
		  } catch (Exception e) {
			 System.out.println("ArduinoObserver ERROR:"+e.getMessage());			 
		  }
		}	
//	}
}
