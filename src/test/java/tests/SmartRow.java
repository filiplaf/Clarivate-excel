package tests;



public class SmartRow {

	
	private String Environment;
	private String ServiceUrl;

	
	public 	SmartRow (String environment, String serviceUrl) {
		Environment = environment;
		ServiceUrl = serviceUrl;
		
	}
	
	public String toString() {
		return Environment + ServiceUrl;	
	}
	
}