package org.n52.wps.test;
import java.io.IOException;

import junit.framework.TestCase;


public class DescribeProcessPOSTTester extends TestCase {
	private String url;
	
        @Override
	protected void setUp(){
		url = AllTestsIT.getURL();
	}
	
	/*
	 * *GetCapabilities*
		- DescribeProcess POST request for a single process
		- DescribeProcess POST request for a mutliple processes
		- DescribeProcess POST request with missing "version" paramater
		- DescribeProcess POST request with missing "service" paramater
		- DescribeProcess POST request with missing "identifier" paramater
		- DescribeProcess POST request with wrong "identifier" paramater value
	 */
	
		
	public void testDescribeProcessCompleteSingle(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" version=\"1.0.0\" language=\"en-US\">"+
			"<ows:Identifier>org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ows:Identifier>"+
		"</wps:DescribeProcess>";
		
				String response ="";
				try {
					response = PostClient.sendRequest(url, payload);
//					parseXML(response);
				} catch (Exception e) {
					fail(e.getMessage());
				}
				
		assertTrue(!response.contains("ExceptionReport"));
		assertTrue(response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
	}
	
	public void testDescribeProcessCompleteSingleWrongLanguage(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" version=\"1.0.0\" language=\"en-CA\">"+
			"<ows:Identifier>org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ows:Identifier>"+
		"</wps:DescribeProcess>";
		
				String response ="";
				try {
					response = PostClient.sendRequest(url, payload);
//					parseXML(response);
				} catch (Exception e) {
					fail(e.getMessage());
				}
				
		assertTrue(response.contains("ExceptionReport"));
		assertTrue(response.contains("language"));
		assertTrue(!response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
	}
	
	public void testDescribeProcessCompleteMultiple(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" version=\"1.0.0\" language=\"en-US\">"+
			"<ows:Identifier>org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ows:Identifier>"+
			"<ows:Identifier>org.n52.wps.server.algorithm.simplify.DouglasPeuckerAlgorithm</ows:Identifier>"+
		"</wps:DescribeProcess>";
		
		String response ="";
		try {
			response = PostClient.sendRequest(url, payload);
//			parseXML(response);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(!response.contains("ExceptionReport"));
		assertTrue(response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
		assertTrue(response.contains("org.n52.wps.server.algorithm.simplify.DouglasPeuckerAlgorithm"));
		
	}
	
	public void testDescribeProcessCompleteAll(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" version=\"1.0.0\" language=\"en-US\">"+
			"<ows:Identifier>all</ows:Identifier>"+
			
		"</wps:DescribeProcess>";
		
		String response ="";
		try {
			response = PostClient.sendRequest(url, payload);
//			parseXML(response);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertTrue(!response.contains("ExceptionReport"));
		assertTrue(response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
		assertTrue(response.contains("org.n52.wps.server.algorithm.simplify.DouglasPeuckerAlgorithm"));
		
	}
	

	public void testDescribeProcessMissingVersionParameter(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" language=\"en-US\">"+
			"<ows:Identifier>org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ows:Identifier>"+
			
		"</wps:DescribeProcess>";
		
		String response ="";
		try {
			response = PostClient.sendRequest(url, payload);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertTrue(response.contains("ExceptionReport"));
		assertTrue(!response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
	}
	
	public void testDescribeProcessMissingServiceParameter(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" version=\"1.0.0\" language=\"en-US\">"+
			"<ows:Identifier>org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ows:Identifier>"+
		"</wps:DescribeProcess>";
		
		String response ="";
		try {
			response = PostClient.sendRequest(url, payload);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertTrue(response.contains("ExceptionReport"));
		assertTrue(!response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
	}
	
	public void testDescribeProcessMissingIdentifierParameter(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" version=\"1.0.0\" language=\"en-US\">"+
		"</wps:DescribeProcess>";
		
		String response ="";
		try {
			response = PostClient.sendRequest(url, payload);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertTrue(response.contains("ExceptionReport"));
		assertTrue(!response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
	}
	
	public void testDescribeProcessWrongIdentifierParameter(){
		String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+ 
		"<wps:DescribeProcess xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"+
			"http://schemas.opengis.net/wps/1.0.0/wpsDescribeProcess_request.xsd\" service=\"WPS\" version=\"1.0.0\" language=\"en-US\">"+
			"<ows:Identifier>XXX</ows:Identifier>"+
		"</wps:DescribeProcess>";
		
		String response ="";
		try {
			response = PostClient.sendRequest(url, payload);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertTrue(response.contains("ExceptionReport"));
		assertTrue(!response.contains("org.n52.wps.server.algorithm.SimpleBufferAlgorithm"));
	}

}
