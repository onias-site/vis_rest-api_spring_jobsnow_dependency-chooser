package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vis.rest.open.api.VisOpenApiRecruiterResumes;
import com.vis.services.VisServiceRecruiterResumes;

@CrossOrigin
@RestController
public class VisRestApiRecruiterResumes implements VisOpenApiRecruiterResumes {

	@PostMapping("/recruiter-email-link/view-resume")
	public Map<String, Object> viewResume(@RequestBody Map<String, Object> body) {
		Map<String, Object> result = VisServiceRecruiterResumes.ViewResumeFromEmail.execute(body);
		return result;
	}

	@PostMapping("/recruiter-email-link/alias")
	public Map<String, Object> saveAlias(@RequestBody Map<String, Object> body) {
		Map<String, Object> result = VisServiceRecruiterResumes.SaveResumeAliasFromEmail.execute(body);
		return result;
	}

	@PostMapping("/recruiter-email-link/unsubscribe")
	public Map<String, Object> unsubscribe(@RequestBody Map<String, Object> body) {
		Map<String, Object> result = VisServiceRecruiterResumes.UnsubscribeFromResumesEmail.execute(body);
		return result;
	}

	@GetMapping("/recruiter/{email}/resumes-of-the-week")
	public Map<String, Object> getResumesOfTheWeek(@RequestBody Map<String, Object> sessionValues) {
		Map<String, Object> result = VisServiceRecruiterResumes.GetResumesOfTheWeek.execute(sessionValues);
		return result;
	}
}
