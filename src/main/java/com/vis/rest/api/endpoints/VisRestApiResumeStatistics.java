package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constants.CcpOtherConstants;
import com.vis.business.statistics.VisBusinessResumeStatisticsOfTheRegion;
import com.vis.rest.open.api.VisOpenApiResumeStatistics;
import com.vis.services.VisServiceResumeStatistics;

/**
 * REST controller for the statistics of the resumes at path {@code /statistics/resumes}: public, with no session.
 */
@RestController
@RequestMapping("statistics/resumes")
public class VisRestApiResumeStatistics implements VisOpenApiResumeStatistics {

	/**
	 * The statistics of the resumes of a region. Delegates to the matching service.
	 * @param ddd the {@code ddd} path variable
	 * @return the response body
	 */
	@GetMapping("/{ddd}")
	public Map<String, Object> getResumeStatisticsOfTheRegion(@PathVariable("ddd") Integer ddd) {
		var json = CcpOtherConstants.EMPTY_JSON.put(VisBusinessResumeStatisticsOfTheRegion.Fields.ddd, ddd);
		var execute = VisServiceResumeStatistics.GetResumeStatisticsOfTheRegion.execute(json.content);
		return execute;
	}

	/**
	 * The totals of every region. Delegates to the matching service.
	 * @return the response body
	 */
	@GetMapping("/totals")
	public Map<String, Object> getResumeTotalsByRegion() {
		var noInput = CcpOtherConstants.EMPTY_JSON.content;
		var execute = VisServiceResumeStatistics.GetResumeTotalsByRegion.execute(noInput);
		return execute;
	}
}
