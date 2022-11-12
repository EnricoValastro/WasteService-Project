package it.unibo.radarSystem22.domain.interfaces;

public interface ISonar extends IDevice{
	public void activate();		 
	public void deactivate();
	public IDistance getDistance();	
	public boolean isActive();
}
