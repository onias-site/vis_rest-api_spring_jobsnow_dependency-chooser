package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for job position operations at path {@code /recruiters/{email}/positions/{title}}.
 * Covers creating/updating positions, status changes, data retrieval, resume listing,
 * and skill extraction or suggestion from position text.
 */
@RequestMapping("recruiters/{email}/positions/{title}")
@Tag(name = "Positions", description = "Operations for managing job positions: CRUD, resume listing, and skill extraction.")
public interface VisOpenApiPosition {

	@Operation(
		summary = "Create or update a job position",
		description = "When does it occur? When the recruiter creates a new job position or updates an existing one. "
			+ "What does it do? Saves the position data. Both POST (create) and PATCH (update) are accepted."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>title</b> – Required. Position title.</li>"
			+ "</ul>"
			+ "No specific field validation is enforced beyond the entity requirements."
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Position saved successfully."),
	})
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH})
	Map<String, Object> save(@RequestBody String sessionValues);

	@Operation(
		summary = "Deactivate (change status of) a job position",
		description = "When does it occur? When the recruiter wants to close or deactivate a position. "
			+ "What does it do? Marks the position as inactive by moving it to the twin entity."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>title</b> – Required. Position title.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Position status changed successfully."),
	})
	@DeleteMapping("/status")
	Map<String, Object> changeStatus(@RequestBody String sessionValues);

	@Operation(
		summary = "Get job position data",
		description = "When does it occur? When the recruiter or the system needs to retrieve the full data of a position. "
			+ "What does it do? Returns the position data along with an 'activePosition' flag indicating if it is active or inactive."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>title</b> – Required. Position title.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"activePosition\": true}")) },
			responseCode = "200",
			description = "Position data returned. 'activePosition' is true if the position is currently active."),
	})
	@GetMapping
	Map<String, Object> getData(@RequestBody String sessionValues);

	@Operation(
		summary = "List resumes matching this position (paginated)",
		description = "When does it occur? When the recruiter opens the list of candidate resumes for a position. "
			+ "What does it do? Returns a paginated list of resumes that match this position."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>title</b> – Required. Position title.</li>"
			+ "<li><b>fromIndex</b> – Required. Pagination start index.</li>"
			+ "<li><b>listSize</b> – Required. Number of items to return.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Resume list returned successfully."),
	})
	@GetMapping("/resumes/fromIndex/{fromIndex}/listSize/{listSize}")
	Map<String, Object> getResumeList(@RequestBody String sessionValues,
			@PathVariable("fromIndex") String fromIndex,
			@PathVariable("listSize") String listSize,
			@PathVariable("title") String title);

	@Operation(
		summary = "Extract important skills from position text",
		description = "When does it occur? When the recruiter provides a job description text and wants to identify the key skills. "
			+ "What does it do? Parses the position text and extracts the most relevant skills."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>title</b> – Required. Position title (also used as part of the text context).</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{}")) },
			responseCode = "200",
			description = "Skills extracted from the position text."),
	})
	@PostMapping("/words")
	Map<String, Object> getImportantSkillsFromText(@RequestBody String sessionValues,
			@PathVariable("title") String title);

	@Operation(
		summary = "Suggest new skills for this position",
		description = "When does it occur? When the recruiter wants to propose additional skills for a position. "
			+ "What does it do? Registers the skill suggestion for review and approval."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>email</b> – Required. Recruiter's email address.</li>"
			+ "<li><b>title</b> – Required. Position title.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Skill suggestion registered. The skill already exists in the system."),
		@ApiResponse(responseCode = "202", description = "Skill suggestion submitted for review."),
		@ApiResponse(responseCode = "409",
			description = "Skill already exists, is pending, or has been approved."),
		@ApiResponse(responseCode = "412",
			description = "Skill was previously rejected and cannot be re-suggested."),
	})
	@PatchMapping("/words")
	Map<String, Object> suggestNewSkills(@RequestBody String sessionValues,
			@PathVariable("title") String title);
}
