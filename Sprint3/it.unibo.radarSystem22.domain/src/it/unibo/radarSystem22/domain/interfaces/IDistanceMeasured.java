package it.unibo.radarSystem22.domain.interfaces;
  
public interface IDistanceMeasured extends IDistance, IObservable{
	public void setVal( IDistance d );
//	public IDistance getDistance(   );
//	public void addObserver(Observer o);		//implemented by Java's Observable 
//	public void deleteObserver( Observer obs );	//implemented by Java's Observable 
}
