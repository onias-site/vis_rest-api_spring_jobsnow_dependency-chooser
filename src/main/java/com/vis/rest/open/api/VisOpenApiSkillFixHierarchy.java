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
 * OpenAPI contract for skill hierarchy fix suggestions at path {@code /resume/{email}/skills/hierarchy}.
 * Covers the candidate asking to add a resume skill to, or remove it from, an implicit knowledge.
 */
@RequestMapping("/resume/{email}/skills/hierarchy")
@Tag(name = "Skill Hierarchy Fix", description = "Suggestions from candidates to add or remove skills from the implicit knowledge they depend on.")
public interface VisOpenApiSkillFixHierarchy {

	@Operation(
		summary = "Suggest a skill hierarchy fix",
		description = "When does it occur? When the candidate, reviewing the implicit knowledge inferred from their resume, "
			+ "believes a skill should be added to an implicit knowledge (it depends on it but is not listed) "
			+ "or removed from it (it is listed but does not depend on it). "
			+ "What does it do? Records the suggestion as pending review. Support and the candidate are notified, "
			+ "and the suggestion is later moved to approved or rejected. "
			+ "The suggestion is identified by the candidate's email, the parent and the type: "
			+ "a new suggestion from the same candidate for the same parent and type replaces the previous one."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email address (format: user@domain.ext), min 7, max 100 characters.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>parent</b> – Required. The implicit knowledge shown as the accordion title. Min 2, max 50 characters.</li>"
			+ "<li><b>skill</b> – Required array. The resume skills to add to or remove from the parent. Min 2, max 50 characters each.</li>"
			+ "<li><b>type</b> – Required. Accepted values: 'add', 'remove'.</li>"
			+ "<li><b>description</b> – Required. The candidate's explanation for the suggestion. Min 10, max 500 characters.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"parent\": \"SQL\","
				+ "\"skill\": [\"ORACLE\", \"MYSQL\"],"
				+ "\"type\": \"add\","
				+ "\"description\": \"Oracle is a relational database queried with SQL.\""
				+ "}")) },
			responseCode = "200",
			description = "Suggestion saved as pending review."),
		@ApiResponse(responseCode = "422",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping
	Map<String, Object> saveHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Withdraw a pending skill hierarchy fix suggestion",
		description = "When does it occur? When the candidate gives up a suggestion that is still pending review. "
			+ "What does it do? Deletes the candidate's pending suggestion for that parent and type. "
			+ "Approved and rejected suggestions are the review history and are not affected."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email address (format: user@domain.ext), min 7, max 100 characters.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>parent</b> – Required. The implicit knowledge shown as the accordion title. Min 2, max 50 characters.</li>"
			+ "<li><b>type</b> – Required. Accepted values: 'add', 'remove'.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Pending suggestion withdrawn."),
		@ApiResponse(responseCode = "404", description = "The candidate has no pending suggestion for this parent and type (it may have been reviewed meanwhile)."),
		@ApiResponse(responseCode = "422",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@DeleteMapping
	Map<String, Object> deleteHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Get the candidate's skill hierarchy fix suggestion",
		description = "When does it occur? When the candidate opens the add or remove modal of an implicit knowledge. "
			+ "What does it do? Returns the candidate's suggestion for that parent and type, looking first in pending, "
			+ "then in approved, then in rejected, and adds a <b>status</b> field telling where it was found. "
			+ "Approved and rejected suggestions also carry the reviewer's <b>explanation</b>. "
			+ "Returns an empty object when the candidate has no suggestion for that parent and type. "
			+ "It is a POST, not a GET, because the session filter only validates the login when the request has a body."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Candidate's email address. Valid email address (format: user@domain.ext), min 7, max 100 characters.</li>"
			+ "</ul>"
			+ "<b>Request body fields (JSON):</b><ul>"
			+ "<li><b>parent</b> – Required. The implicit knowledge shown as the accordion title. Min 2, max 50 characters.</li>"
			+ "<li><b>type</b> – Required. Accepted values: 'add', 'remove'.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"email\": \"candidate@domain.com\","
				+ "\"parent\": \"SQL\","
				+ "\"skill\": [\"ORACLE\", \"MYSQL\"],"
				+ "\"type\": \"add\","
				+ "\"description\": \"Oracle and MySQL are relational databases queried with SQL.\","
				+ "\"explanation\": \"Both depend on SQL.\","
				+ "\"status\": \"approved\""
				+ "}")) },
			responseCode = "200",
			description = "The suggestion with its status ('pending', 'approved' or 'rejected'), or an empty object when there is none."),
		@ApiResponse(responseCode = "422",
			description = "Validation error — one or more required fields are missing or contain invalid values."),
	})
	@PostMapping("/search")
	Map<String, Object> getHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues);
}
