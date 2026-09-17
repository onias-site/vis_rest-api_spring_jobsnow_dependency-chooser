package com.vis.rest.api.endpoints;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpJsonFieldName;
import com.vis.rest.open.api.VisOpenApiRecruiter;
import com.vis.services.VisServiceRecruiter;


import com.ccp.json.fields.validation.CcpJsonCommonsFields;
import com.vis.json.fields.validation.VisJsonCommonsFields;

/**
 * REST controller for recruiter operations in the VIS module at path {@code /recruiter/{email}}.
 * Manages sending resumes by email, querying seen resumes, listing positions,
 * and saving/toggling opinions about resumes.
 */
@CrossOrigin
@RestController
@RequestMapping("recruiter/{email}")
public class VisRestApiRecruiter implements VisOpenApiRecruiter {
	
	enum JsonFieldNames implements CcpJsonFieldName{
		resumeIds, opinionType, positionStatus
	}
	
	@PostMapping("/resumes/sending/email")
	public Map<String, Object> sendResumesToEmail(
			@RequestBody String sessionValues,
			@RequestParam("emails") List<String> emails,
			@RequestParam("resumeIds") List<String> resumeIds
			){
				CcpJsonRepresentation ccpJsonRepresentation = new CcpJsonRepresentation(sessionValues);
				CcpJsonRepresentation put = ccpJsonRepresentation
				.put(JsonFieldNames.resumeIds, resumeIds);

				CcpJsonRepresentation json = put
				.put(CcpJsonCommonsFields.emails, emails)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.SendResumesToEmail.execute(json.content);
		return execute;
	}

	@GetMapping("/resumes/seen/{opinionType}")
	public Map<String, Object> getAlreadySeenResumes(
			@RequestBody String sessionValues,
			@PathVariable("opinionType") String opinionType
			){
				CcpJsonRepresentation ccpJsonRepresentation2 = new CcpJsonRepresentation(sessionValues);

				CcpJsonRepresentation json = ccpJsonRepresentation2
				.put(JsonFieldNames.opinionType, opinionType)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.GetAlreadySeenResumes.execute(json.content);
		return execute;
	}

	@GetMapping("/positions/{positionStatus}")
	public Map<String, Object> getPositionsFromThisRecruiter(
			@RequestBody String sessionValues,
			@PathVariable("positionStatus") String positionStatus
			){
				CcpJsonRepresentation ccpJsonRepresentation3 = new CcpJsonRepresentation(sessionValues);

				CcpJsonRepresentation json = ccpJsonRepresentation3
				.put(JsonFieldNames.positionStatus, positionStatus)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.GetPositionsFromThisRecruiter.execute(json.content);
		return execute;
	}
	
	@PostMapping("/resumes/{resumeId}")
	public Map<String, Object> changeOpinionAboutThisResume(
			@RequestBody String sessionValues,
			@PathVariable("resumeId") String resumeId
			){
				CcpJsonRepresentation ccpJsonRepresentation4 = new CcpJsonRepresentation(sessionValues);

				CcpJsonRepresentation json = ccpJsonRepresentation4
				.put(VisJsonCommonsFields.resumeId, resumeId)
				;
		
		Map<String, Object> execute = VisServiceRecruiter.ChangeOpinionAboutThisResume.execute(json.content);
		return execute;
	}
	@PostMapping("/resumes/{resumeId}/opinion")
	public Map<String, Object> saveOpinionAboutThisResume(
			@RequestBody String sessionValues,
			@PathVariable("resumeId") String resumeId
			){
				CcpJsonRepresentation ccpJsonRepresentation5 = new CcpJsonRepresentation(sessionValues);

				CcpJsonRepresentation json = ccpJsonRepresentation5
				.put(VisJsonCommonsFields.resumeId, resumeId)
				;
		Map<String, Object> execute = VisServiceRecruiter.SaveOpinionAboutThisResume.execute(json.content);
		return execute;
	}

}
