package it.unibo.radarSystem22.domain.models;
 
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarHCSR04Concrete;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModel  implements ISonar{
	protected  IDistance curVal = new Distance(90);
	protected boolean stopped   = true;
	protected abstract void sonarSetUp() ;
	protected abstract void sonarProduce() ;

	public static ISonar create() {
		if( DomainSystemConfig.simulation )
			return createSonarMock();
		else
			return createSonarConcrete();
	}
	public static ISonar createSonarMock() {
		ColorsOut.out("createSonarMock", ColorsOut.BLUE);
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete() {
		ColorsOut.out("createSonarConcrete", ColorsOut.BLUE);
		if(DomainSystemConfig.sonarType.equals("HCSR04")){
			return new SonarHCSR04Concrete();
		}
		else{
			return null;
		}
	}
	protected SonarModel() {//hidden costructor, to force setup
		ColorsOut.out("SonarModel | calling (specialized) sonarSetUp ", ColorsOut.BLUE );
		sonarSetUp();   
	}
	@Override
	public void activate() {
		curVal = new Distance( 90 );
 		ColorsOut.out("SonarModel | activate" );
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					sonarProduce(  );
				}
				ColorsOut.out("SonarModel | ENDS" );
		    }
		}.start();
	}
	@Override
	public void deactivate() {
		ColorsOut.out("SonarModel | deactivate", ColorsOut.BgYellow );
		stopped = true;
	}
	@Override
	public boolean isActive() {
		return ! stopped;
	}
	@Override
	public IDistance getDistance() {
		return curVal;
	}
	protected void updateDistance( int d ) {
		curVal = new Distance( d );
		ColorsOut.out("SonarModel | updateDistance "+ d, ColorsOut.BLUE);
	}

}

  
