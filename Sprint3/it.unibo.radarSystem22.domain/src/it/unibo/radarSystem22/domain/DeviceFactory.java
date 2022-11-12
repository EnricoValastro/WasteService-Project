package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.concrete.RadarDisplay;
import it.unibo.radarSystem22.domain.concrete.SonarConcreteForObs;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.mock.SonarMockForObs;
import it.unibo.radarSystem22.domain.models.LedModel;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class DeviceFactory {

	public static ILed createLed() {
		//Colors.out("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			return LedModel.createLedMock();
		}else {
			return LedModel.createLedConcrete();
		}
	}
	public static ISonarForObs createSonarForObs( ) {
		if( DomainSystemConfig.simulation)  {
			return new SonarMockForObs();
		}else { 
			return new SonarConcreteForObs();
		}
	}

	public static ISonar createSonar() {
		//Colors.out("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			return SonarModel.createSonarMock();
		}else { 
			return SonarModel.createSonarConcrete();
		}
	}
 
	//We do not have mock for RadarGui
	public static IRadarDisplay createRadarGui() {
		return RadarDisplay.getRadarDisplay();
	}
	
}
