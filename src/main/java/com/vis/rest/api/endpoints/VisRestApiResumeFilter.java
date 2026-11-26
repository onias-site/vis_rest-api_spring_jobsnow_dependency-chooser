package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.vis.business.resume.filter.VisBusinessGetTheResumeFilter;
import com.vis.rest.open.api.VisOpenApiResumeFilter;
import com.vis.services.VisServiceResumeFilter;

/**
 * REST controller for the search of resumes by filters at path {@code /resume-filter} (screen "Currículos - Buscar" and
 * the position form, since 2026-11-25): public, with no session.
 */
@RestController
public class VisRestApiResumeFilter implements VisOpenApiResumeFilter {

	@PostMapping("/resume-filter")
	public Map<String, Object> searchResumes(@RequestBody Map<String, Object> filters) {
		Map<String, Object> result = VisServiceResumeFilter.SearchResumes.execute(filters);
		return result;
	}

	@GetMapping("/resume-filter/{id}")
	public Map<String, Object> getResumeFilter(@PathVariable("id") String id) {
		CcpJsonRepresentation json = CcpOtherConstants.EMPTY_JSON.put(VisBusinessGetTheResumeFilter.Fields.id, id);
		Map<String, Object> result = VisServiceResumeFilter.GetResumeFilter.execute(json.content);
		return result;
	}
}
