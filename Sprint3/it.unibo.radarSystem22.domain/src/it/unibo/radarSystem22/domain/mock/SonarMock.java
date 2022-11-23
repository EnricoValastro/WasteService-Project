package it.unibo.radarSystem22.domain.mock;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import jdk.javadoc.internal.doclets.toolkit.CommentUtils;

public class SonarMock extends SonarModel implements ISonar{
private int delta = 5;
	@Override
	protected void sonarSetUp() {
		curVal = new Distance(90);		
		ColorsOut.out("SonarMock | sonarSetUp curVal="+curVal);
	}
	
	@Override
	public IDistance getDistance() {
		return curVal;
	}	
	@Override
	protected void sonarProduce( ) {
		int v;
		if( DomainSystemConfig.testing ) {	//produces always the same value
			updateDistance( DomainSystemConfig.testingDistance );			      
			//stopped = true;  //one shot
		}
		else {
				if(curVal.getVal() >= 1){
					while(curVal.getVal() >= 1){
						v = curVal.getVal() - delta;
						updateDistance(v);
						BasicUtils.delay(500);
					}
				}
				else{
					while(curVal.getVal() < 90){
						v = curVal.getVal() + delta;
						updateDistance(v);
						BasicUtils.delay(500);
					}
				}


		}
 	}
}
