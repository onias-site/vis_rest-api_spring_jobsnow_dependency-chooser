package com.vis.rest.api.swagger;

import java.util.Map;

public interface VisOpenApiResume {
	
	Map<String, Object> save(Map<String, Object> sessionValues);
	
	Map<String, Object> delete(Map<String, Object> sessionValues);
	
	Map<String, Object> changeStatus(Map<String, Object> sessionValues);
	
	Map<String, Object> getData(Map<String, Object> sessionValues);
	
	Map<String, Object> getFile(Map<String, Object> sessionValues, String viewMode);

}
