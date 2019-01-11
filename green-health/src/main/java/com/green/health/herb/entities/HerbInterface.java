package com.green.health.herb.entities;

public interface HerbInterface {
	
	public String getLocalName();

	public void setLocalName(String engName);

	public String getDescription();

	public void setDescription(String description);

	public String getGrowsAt();

	public void setGrowsAt(String growsAt);

	public String getWhenToPick();

	public void setWhenToPick(String whenToPick);

	public String getWhereToBuy();

	public void setWhereToBuy(String whereToBuy);

	public String getProperties();

	public void setProperties(String properties);

	public String getWarnings();

	public void setWarnings(String warnings);
}
