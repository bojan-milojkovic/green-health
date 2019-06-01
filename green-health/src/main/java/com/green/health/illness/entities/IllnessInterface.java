package com.green.health.illness.entities;

public interface IllnessInterface {

	public String getDescription();

	public void setDescription(String description);

	public String getSymptoms();
	
	public void setSymptoms(String sympthoms);
	
	public String getLocalName();

	public void setLocalName(String localName);
	
	public String getCause();
	
	public void setCause(final String cause);
	
	public String getTreatment();

	public void setTreatment(String treatment);
}
