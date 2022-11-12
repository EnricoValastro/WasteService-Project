package it.unibo.radarSystem22.domain;
import static org.junit.Assert.assertTrue;
import org.junit.*;

import it.unibo.radarSystem22.domain.interfaces.IObserver;
import it.unibo.radarSystem22.domain.interfaces.ISonarForObs;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


public class TestSonarMockForObs {
//	@Before
//	public void up() {
//		DomainSystemConfig.simulation      = true;
//		DomainSystemConfig.testingDistance = 22;
//	}
//	
//	@After
//	public void down() {
//		System.out.println("down");		
//	}	
	
	@Test 
	public void testSingleshotSonarForObsMock() {
 		DomainSystemConfig.testing = true;
		DomainSystemConfig.simulation      = true;
		DomainSystemConfig.testingDistance = 22;
		boolean oneShot           = true;			
		ISonarForObs  sonar       = DeviceFactory.createSonarForObs();
		IObserver obs1            = new SonarObserverFortesting("obs1",sonar,oneShot) ;
		sonar.getDistance().addObserver( obs1 );	 
		sonar.activate();
 		BasicUtils.delay(100);  //setup
		int v0 = sonar.getDistance().getVal();
 		System.out.println("testSingleshotSonarObservableMock v0=" + v0);
 		assertTrue(  v0 == DomainSystemConfig.testingDistance );
	}
	
	@Test 
	public void testSonarForObsMock() {
		DomainSystemConfig.testing    = false;
 		DomainSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta       = 1;
		boolean oneShot = false;
				
		ISonarForObs sonar = DeviceFactory.createSonarForObs();
		
		IObserver obs1          = new SonarObserverFortesting("obs1",sonar,oneShot);
		IObserver obs2          = new SonarObserverFortesting("obs2",sonar,oneShot);
		
		sonar.getDistance().addObserver( obs1 );	//add an observer
		sonar.getDistance().addObserver( obs2 );	//add an observer

		new SonarConsumerForTesting( sonar, delta ).start();  //consuma

		sonar.activate();
		while( sonar.isActive() ) { BasicUtils.delay(100);}
		BasicUtils.delay(1000);

	}
	
 
}
