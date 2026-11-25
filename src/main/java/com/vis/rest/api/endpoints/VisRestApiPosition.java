package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

	@PostMapping("/recruiter/{email}/positions/restore")
	public Map<String, Object> restorePosition(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServicePosition.RestorePosition.execute(sessionValues);
		return result;
	}

	@PostMapping("/positions/resumes-of-the-filters")
	public Map<String, Object> getResumesOfTheFilters(@RequestBody Map<String, Object> filters) {
		Map<String, Object> result = VisServicePosition.GetResumesOfTheFilters.execute(filters);
		return result;
	}

	@PostMapping("/positions/instant-message-groups")
	public Map<String, Object> getInstantMessageGroups(@RequestBody Map<String, Object> position) {
		Map<String, Object> result = VisServicePosition.GetInstantMessageGroups.execute(position);
		return result;
	}
}
