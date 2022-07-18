package unibo.testwasteservice;

import org.junit.Assert;
import unibo.actor22comm.utils.CommUtils;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unibo.ctxwasteservice.MainCtxwasteserviceKt;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;

public class TestWasteServiceActor {
	
	static Interaction2021 conn;

	@BeforeClass
	public static void initTest() {
		System.out.println("TestWasteServiceActor	|	initTest...");

		new Thread() {
			public void run() {
				MainCtxwasteserviceKt.main();
			}
		}.start();

		ActorBasic wasteserviceactor = QakContext.Companion.getActor("wasteserviceactor");
		while(wasteserviceactor == null){
			System.out.println("TestWasteServiceActor	|	waiting for application start...");
			CommUtils.delay(100);
			wasteserviceactor = QakContext.Companion.getActor("wasteserviceactor");
		}
		try {
			conn = TcpClientSupport.connect("localhost", 8055, 4);
		} catch (Exception e) {
			System.out.println("TestWasteServiceActor	|	connection failed...");
		}
	}

	@Test
	public void testAccept() {
		String storeWaste = "msg(storeWaste, request, testunit, wasteserviceactor, storeWaste(Plastic,200),1)";
		System.out.println("TestWasteServiceActor	|	testaccept on message: "+ storeWaste);
		String asw = "";
		try {
			asw = conn.request(storeWaste);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(asw.contains("loadaccept"));
	}

	@Test
	public void testRejected(){
		String storeWaste = "msg(storeWaste, request, testunit, wasteserviceactor, storeWaste(Glass,700),1)";
		System.out.println("TestWasteServiceActor	|	testrejected on message: "+ storeWaste);
		String asw = "";
		try {
			asw = conn.request(storeWaste);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(asw.contains("loadrejected"));
	}

}
