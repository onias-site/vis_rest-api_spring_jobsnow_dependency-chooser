package com.vis.rest.api.swagger;

import java.util.List;
import java.util.Map;

public interface VisOpenApiRecruiter {
	
	Map<String, Object> sendResumesToEmail(String sessionValues, List<String> emails, List<String> resumeIds);

	Map<String, Object> getAlreadySeenResumes(String sessionValues, String opinionType);
	
	Map<String, Object> getPositionsFromThisRecruiter(String sessionValues, String positionStatus);
	
	Map<String, Object> changeOpinionAboutThisResume(String sessionValues, String resumeId);
	
	Map<String, Object> saveOpinionAboutThisResume(String sessionValues, String resumeId);
}
