package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for skill suggestions at path {@code /resume/{email}/skills/suggestion}.
 * Covers the candidate suggesting a skill that is in the text of their resume and that was not listed.
 */
@RequestMapping("/resume/{email}/skills/suggestion")
@Tag(name = "Skill Suggestion", description = "Suggestions from candidates of skills that are in their resumes and that the system does not know yet.")
public interface VisOpenApiSkillSuggestion {

	/**
	 * Suggest a skill.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@Operation(
		summary = "Suggest a skill",
		description = "When does it occur? When the candidate, reviewing the skills read from their resume, "
			+ "notices that a skill written in the resume was not listed. "
			+ "What does it do? Records the suggestion as pending review. Support and the candidate are notified, "
			+ "and, once support reviews it, the suggestion is moved to approved (the skill starts being recognized "
			+ "in the resumes) or to rejected, always with support's explanation. "
			+ "The suggestion is identified by the candidate's email and the skill."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email address (format: user@domain.ext), min 7, max 100 characters.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>skill</b> – Required. The suggested skill. Min 2, max 50 characters.</li>"
			+ "<li><b>synonym</b> – Optional array. Other names of the same skill. Min 2, max 50 characters each.</li>"
			+ "<li><b>description</b> – Required. The candidate's explanation for the suggestion. Min 10, max 500 characters.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"skill\": \"REACT NATIVE\","
				+ "\"synonym\": [\"REACTNATIVE\", \"RN\"],"
				+ "\"description\": \"I built two mobile apps with React Native, as written in my experience section.\""
				+ "}")) },
			responseCode = "200",
			description = "Suggestion saved as pending review."),
		@ApiResponse(responseCode = "403",
			description = "userNotAllowed — support chose to ignore this candidate's skill suggestions. "
				+ "The suggestion is not saved and nobody is notified."),
		@ApiResponse(responseCode = "409", description = "The candidate already has a pending suggestion of this skill."),
		@ApiResponse(responseCode = "410", description = "alreadyRejected — support already rejected this skill suggested by this candidate; the rejection is final."),
		@ApiResponse(responseCode = "412", description = "skillAlreadyExists — the system already knows this skill, by its name or as a synonym of another skill."),
		@ApiResponse(responseCode = "422",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping
	Map<String, Object> saveSkillSuggestion(@RequestBody Map<String, Object> sessionValues);

	/**
	 * Withdraw a pending skill suggestion.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@Operation(
		summary = "Withdraw a pending skill suggestion",
		description = "When does it occur? When the candidate gives up a suggestion that is still pending review. "
			+ "What does it do? Deletes the candidate's pending suggestion of that skill. "
			+ "Reviewed suggestions (approved or rejected) are the review history and are not affected."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email address (format: user@domain.ext), min 7, max 100 characters.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>skill</b> – Required. The suggested skill. Min 2, max 50 characters.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Pending suggestion withdrawn."),
		@ApiResponse(responseCode = "404", description = "The candidate has no pending suggestion of this skill (it may have been reviewed meanwhile)."),
		@ApiResponse(responseCode = "422",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@DeleteMapping
	Map<String, Object> deleteSkillSuggestion(@RequestBody Map<String, Object> sessionValues);

	/**
	 * Get the candidate's suggestion of a skill.
	 * @param sessionValues the request body
	 * @return the response body
	 */
	@Operation(
		summary = "Get the candidate's suggestion of a skill",
		description = "When does it occur? When the candidate types, in the suggestion modal, a skill they may have suggested before. "
			+ "What does it do? Returns the candidate's suggestion of that skill, looking first in pending, "
			+ "then in approved and in rejected, and adds a <b>status</b> field telling where it was found. "
			+ "Reviewed suggestions also carry support's <b>explanation</b>. "
			+ "Returns an empty object when the candidate has no suggestion of that skill. "
			+ "It is a POST, not a GET, because the session filter only validates the login when the request has a body."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email address (format: user@domain.ext), min 7, max 100 characters.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>skill</b> – Required. The suggested skill. Min 2, max 50 characters.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"email\": \"candidate@domain.com\","
				+ "\"skill\": \"REACT NATIVE\","
				+ "\"synonym\": [\"REACTNATIVE\", \"RN\"],"
				+ "\"description\": \"I built two mobile apps with React Native, as written in my experience section.\","
				+ "\"explanation\": \"React Native is a widely used mobile framework.\","
				+ "\"status\": \"approved\""
				+ "}")) },
			responseCode = "200",
			description = "The suggestion with its status ('pending', 'approved' or 'rejected'), or an empty object when there is none."),
		@ApiResponse(responseCode = "422",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/search")
	Map<String, Object> getSkillSuggestion(@RequestBody Map<String, Object> sessionValues);
}
