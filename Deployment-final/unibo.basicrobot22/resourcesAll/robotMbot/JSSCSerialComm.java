package robotMbot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class JSSCSerialComm {  

	private List<String> list;
	private Lock object;
	private Semaphore dataAvailable;
	private SerialPort serialPort;
	private String[] portNames;

	public JSSCSerialComm( ) {
		init();
	}
	
	protected void init(){
		list          = new ArrayList<String>();
		dataAvailable = new Semaphore(0);
		object        = new ReentrantLock();
		portNames     = SerialPortList.getPortNames();
        
		if (portNames.length == 0) {
		    System.out.println("JSSCSerialComm: There are no serial-ports");
		    return;
		}else{
			System.out.println("FOUND " + portNames.length + " serial-ports");
			for( int i=0; i<portNames.length;i++){
				System.out.println("JSSCSerialComm: FOUND " + portNames[i] + " PORT");
			}
		}
		
	}

	public SerialPortConnSupport connect(String commPortName) throws Exception{
		System.out.println("JSSCSerialComm: CONNECT TO " + commPortName + " ports num=" + portNames.length);
		commPortName = commPortName.replace("'", "");
		serialPort = null;
		for (int i = 0; i < portNames.length; i++){
			if(portNames[i].equals(commPortName)){
				System.out.println("JSSCSerialComm: CONNECTING TO " + portNames[i]  );
				serialPort = new SerialPort(commPortName);
				serialPort.openPort();
			    serialPort.setParams(SerialPort.BAUDRATE_115200,
				                         SerialPort.DATABITS_8,
				                         SerialPort.STOPBITS_1,
				                         SerialPort.PARITY_NONE);
//				serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
			    return new SerialPortConnSupport( serialPort );
//				break;
			}
		}
		return null;		
 	}
	
	
//	@Override
	public void close() {		
		try {
			serialPort.removeEventListener();
			serialPort.closePort();
		} catch (SerialPortException e) {
			System.out.println("JSSCSerialComm: could not close the port");
		}
		
	}

//	@Override
	public String readData() {
		String result = "";
		try{
			dataAvailable.acquire();
			object.lock();
			result = list.remove(0);
			object.unlock();
		} catch (Exception e){
			System.out.println("JSSCSerialComm: could not read from port");
		}
		return result;		
	}

//	@Override
	public void writeData(String data) {		
		try {
			serialPort.writeString(data);
		} catch (SerialPortException e) {
			System.out.println("JSSCSerialComm: could not write to port");
		}	
	}


	 
	public void serialEvent(SerialPortEvent event) {		
		 if(event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                String receivedData = serialPort.readString(event.getEventValue());               
                object.lock();
    			list.add(receivedData);
    			System.out.println("serialEvent: " + receivedData);
    			object.unlock();
    			dataAvailable.release();
            }
            catch (SerialPortException ex) {
            	System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }		
	}

}
