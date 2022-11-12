package it.unibo.radarSystem22.domain.concrete;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.DistanceMeasured;
import it.unibo.radarSystem22.domain.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domain.interfaces.ISonarForObs;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
 
public class SonarConcreteForObs extends SonarModel implements ISonarForObs  {
	private  BufferedReader reader ;
	private int lastSonarVal      = 0;
	private Process p             = null;
 	protected IDistanceMeasured observableDistance ; 
	
	@Override
	public IDistanceMeasured getDistance() {
		return observableDistance;
	}

	@Override
	protected void sonarSetUp() {//called by SonarModel constructor
 		observableDistance = new DistanceMeasured( );
	 	observableDistance.setVal( new Distance(lastSonarVal) ); 
	} 	
	
	@Override
	public void activate() {
		ColorsOut.out("SonarConcreteObservable | activate p=" + p, ColorsOut.GREEN);
 		if( p == null ) {
 			try { 
				p      = Runtime.getRuntime().exec("sudo ./SonarAlone");
		        ColorsOut.out("SonarConcreteObservable | sonarSetUp p="+p, ColorsOut.GREEN);
		        reader = new BufferedReader( new InputStreamReader(p.getInputStream()));	
 	       	}catch( Exception e) {
 	       		ColorsOut.outerr("SonarConcreteObservable | sonarSetUp ERROR " + e.getMessage() );
 	    	}
 		}
 		super.activate();
 	}

 
	//Like  SonarConcrete
	@Override
	protected void sonarProduce( ) {
        try {
			String data = reader.readLine();
			if( data == null ) return;
				int v = Integer.parseInt(data);
				//Colors.out("SonarConcreteObservable | v=" + v );
				if( lastSonarVal != v && v < DomainSystemConfig.sonarDistanceMax) {	
					//Eliminiamo dati del tipo 3430 //TODO: filtri in sottosistremia stream
 					lastSonarVal = v;
 	 				updateDistance( v );
				}
 				//Utils.delay(RadarSystemConfig.sonarDelay);
       }catch( Exception e) {
       		ColorsOut.outerr("SonarConcreteObservable | sonarProduce: " + e.getMessage() );
       }		
	}

	@Override
	public void deactivate() {
		ColorsOut.out("SonarConcreteObservable | deactivate", ColorsOut.GREEN);
		if( p != null ) {
			p.destroy();  //Block the runtime process
			p=null;
		}
		super.deactivate();
 	}


  
}
