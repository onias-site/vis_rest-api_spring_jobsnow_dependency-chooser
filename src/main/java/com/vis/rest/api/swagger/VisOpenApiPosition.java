package com.vis.rest.api.swagger;

import java.util.Map;

public interface VisOpenApiPosition {
	
	Map<String, Object> changeStatus(String sessionValues);
	
	Map<String, Object> getData(String sessionValues);

	Map<String, Object> getImportantSkillsFromText(String sessionValues, String title);
	
	Map<String, Object> getResumeList(String sessionValues, String fromIndex, String listSize, String title);

	Map<String, Object> save(String sessionValues);	
	 
	Map<String, Object> suggestNewSkills(String sessionValues, String title);
}
