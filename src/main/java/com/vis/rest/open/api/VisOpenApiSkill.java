package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
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
 * OpenAPI contract for skill operations at path {@code /skills}.
 * Covers extracting skills from free text, suggesting hierarchy corrections,
 * and requesting creation of new skills.
 */
@RequestMapping("skills")
@Tag(name = "Skills", description = "Operations for managing professional skills: extraction from text, hierarchy adjustments, and creation requests.")
public interface VisOpenApiSkill {

	@Operation(
		summary = "Extract skills from free text",
		description = "When does it occur? When the frontend needs to identify relevant skills from a block of text "
			+ "(e.g., a job description or resume summary). "
			+ "What does it do? Parses the text and returns a list of recognized skills along with discarded matches."
			+ "<br/><br/><b>Request body fields (JSON):</b><ul>"
			+ "<li><b>text</b> – Required. The text to extract skills from. Allows empty string. Max 5,000,000 characters.</li>"
			+ "<li><b>excludedSkill</b> – Optional array. Skills to exclude from the result. Each item must contain:<ul>"
			+ "  <li><b>skill</b> – Required. Skill identifier. Min 2, max 50 characters.</li>"
			+ "  <li><b>word</b> – Required. Word as found in text. Min 2, max 50 characters.</li>"
			+ "</ul></li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{"
				+ "\"skill\": [{\"skill\": \"Java\", \"word\": \"Java\", \"label\": \"Java\", \"parent\": []}],"
				+ "\"discardedSkills\": {},"
				+ "\"excludedSkill\": []"
				+ "}")) },
			responseCode = "200",
			description = "Skills extracted successfully. Returns matched skills, discarded matches, and the excluded list."),
	})
	@PostMapping("/fromText")
	Map<String, Object> getSkillsFromText(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Suggest skill hierarchy correction",
		description = "When does it occur? When a user identifies that a skill is incorrectly classified in the hierarchy. "
			+ "What does it do? Records the hierarchy correction suggestion for review."
			+ "<br/><br/>No specific field validation is enforced. The request body should contain the skill and hierarchy context."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Hierarchy correction suggestion saved successfully."),
	})
	@PostMapping("/hierarchy/readjustment")
	Map<String, Object> saveHierarchyFixSuggestion(@RequestBody Map<String, Object> sessionValues);

	@Operation(
		summary = "Request creation of a new skill",
		description = "When does it occur? When the user identifies a skill that does not yet exist in the system. "
			+ "What does it do? Registers the skill creation request for analysis. "
			+ "The skill name is provided as a path variable."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>skill</b> – Required. The name of the skill to be created.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "202",
			description = "Request submitted for analysis. The skill will be reviewed before being added."),
		@ApiResponse(responseCode = "409",
			description = "Skill already exists, is pending approval, or has already been approved."),
		@ApiResponse(responseCode = "412",
			description = "Skill was previously rejected and cannot be re-requested."),
	})
	@PostMapping("/{skill}")
	Map<String, Object> requestToCreateNewSkill(@PathVariable("skill") String skill,
			@RequestBody Map<String, Object> sessionValues);
}
