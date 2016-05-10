package introsde.finalproject.client.util;

public class UrlInfo {
	
	public UrlInfo() {}
	
	static final String businessLogicUrl = "https://fierce-sea-36005.herokuapp.com/sdelab/businessLogic-service";
	static final String storageUrl = "https://warm-hamlet-95336.herokuapp.com/sdelab/storage-service";
	static final String adapterUrl = "https://desolate-scrubland-21919.herokuapp.com/sdelab/adapter-service";
	static final String processCentricUrl = "https://desolate-thicket-56593.herokuapp.com/sdelab/processCentric-service";
	
	/**
	 * This method is used to get the business logic url
	 * @return
	 */
	public String getBusinesslogicURL() {
		return businessLogicUrl;
	}
	
	
	/**
	 * This method is used to get the storage url
	 * @return
	 */
	public String getStorageURL() {
		return storageUrl;
	}

	/**
	 * This method is used to get the adapter url
	 * @return
	 */
	public String getAdapterURL() {
		return adapterUrl;
	}


	/**
	 * This method is used to get the process centric url
	 * @return
	 */
	public String getProcesscentricURL() {
		return processCentricUrl;
	}
}
