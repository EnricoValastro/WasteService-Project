package robotMbot;
//import gnu.io.SerialPort;
import jssc.SerialPort;
import it.unibo.is.interfaces.IObservable;
import it.unibo.is.interfaces.IObserver;
public interface ISerialPortConnection extends IObserver, IObservable{
	public SerialPort connect( String portName, Class userClass) throws Exception;
	public SerialPort connect( String portName, String name) throws Exception;
	public SerialPort connect( String portName) throws Exception;
	public SerialPort getPort();
 	public void closeConnection( String portName ) throws Exception;
}
