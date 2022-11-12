package it.unibo.radarSystem22.domain.models;
 
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcreteForObs;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.mock.SonarMockForObs;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModelForObs extends SonarModel  {
	protected IDistanceMeasured observableDistance ; 
 		
	public static ISonar   create() {
		if( DomainSystemConfig.simulation )  return new SonarMockForObs();
		else  return new SonarConcreteForObs();		
	}
	
	@Override
	public IDistance getDistance() {
  		return observableDistance;
 	}
	@Override
	protected void updateDistance( int d ) {
		//ColorsOut.out("SonarModelObservable call updateDistance | d=" + d, ColorsOut.GREEN );
 		observableDistance.setVal( new Distance( d ) ); //notifies
 	}	
 
  
}
