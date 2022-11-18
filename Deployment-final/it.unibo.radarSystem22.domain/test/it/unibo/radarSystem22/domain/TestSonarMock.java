package it.unibo.radarSystem22.domain;
import static org.junit.Assert.assertTrue;
import org.junit.*;

import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 

public class TestSonarMock {
	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	@Test 
	public void testSonarMock() {
		DomainSystemConfig.simulation = true;
		DomainSystemConfig.testing    = false;
		DomainSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta = 1;
		
		ISonar sonar = DeviceFactory.createSonar();
		new SonarConsumerForTesting( sonar, delta ).start();  //consuma
		sonar.activate();		
 		while( sonar.isActive() ) { BasicUtils.delay(1000);} //to avoid premature exit
 	}
	
 
}
