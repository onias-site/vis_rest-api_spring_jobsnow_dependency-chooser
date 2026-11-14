package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constants.CcpOtherConstants;
import com.vis.rest.open.api.VisOpenApiCuriosities;
import com.vis.services.VisServiceResumeStatistics;

/**
 * REST controller for the charts of the screen Curiosities at path {@code /statistics/curiosities}: public, with no
 * session.
 */
@RestController
@RequestMapping("statistics/curiosities")
public class VisRestApiCuriosities implements VisOpenApiCuriosities {

	/**
	 * The charts of the screen Curiosities. Delegates to the matching service.
	 * @return the response body
	 */
	@GetMapping
	public Map<String, Object> getCuriosities() {
		var noInput = CcpOtherConstants.EMPTY_JSON.content;
		var execute = VisServiceResumeStatistics.GetCuriosities.execute(noInput);
		return execute;
	}
}
