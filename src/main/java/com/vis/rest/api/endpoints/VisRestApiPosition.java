package com.vis.rest.api.endpoints;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constants.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.vis.business.position.VisBusinessResumesOfTheDdds;
import com.vis.rest.open.api.VisOpenApiPosition;
import com.vis.services.VisServicePosition;

@RestController
public class VisRestApiPosition implements VisOpenApiPosition {

	@PostMapping("/recruiter/{email}/positions")
	public Map<String, Object> createPosition(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServicePosition.CreatePosition.execute(sessionValues);
		return result;
	}

	@PostMapping("/recruiter/{email}/positions/edit")
	public Map<String, Object> editPosition(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServicePosition.EditPosition.execute(sessionValues);
		return result;
	}

	@PostMapping("/recruiter/{email}/positions/delete")
	public Map<String, Object> deletePosition(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServicePosition.DeletePosition.execute(sessionValues);
		return result;
	}

	@PostMapping("/recruiter/{email}/positions/inactivate")
	public Map<String, Object> inactivatePosition(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServicePosition.InactivatePosition.execute(sessionValues);
		return result;
	}

	@PostMapping("/recruiter/{email}/positions/reactivate")
	public Map<String, Object> reactivatePosition(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServicePosition.ReactivatePosition.execute(sessionValues);
		return result;
	}

	@GetMapping("/positions/resumes-of-the-ddds/{ddds}")
	public Map<String, Object> getResumesOfTheDdds(@PathVariable("ddds") List<Integer> ddds) {
		CcpJsonRepresentation json = CcpOtherConstants.EMPTY_JSON.put(VisBusinessResumesOfTheDdds.Fields.ddd, ddds);
		Map<String, Object> result = VisServicePosition.GetResumesOfTheDdds.execute(json.content);
		return result;
	}

	@GetMapping("/positions/resumes-of-the-ddds")
	public Map<String, Object> getResumesOfAllTheDdds() {
		Map<String, Object> noDdd = CcpOtherConstants.EMPTY_JSON.content;
		Map<String, Object> result = VisServicePosition.GetResumesOfTheDdds.execute(noDdd);
		return result;
	}
}
