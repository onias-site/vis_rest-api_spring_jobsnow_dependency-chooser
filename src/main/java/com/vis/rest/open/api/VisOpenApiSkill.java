package com.vis.rest.open.api;

import java.util.Map;

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
 * Covers extracting skills from free text.
 * Hierarchy corrections are documented in {@link VisOpenApiSkillFixHierarchy} and skill suggestions in
 * {@link VisOpenApiSkillSuggestion}.
 */
@RequestMapping("skills")
@Tag(name = "Skills", description = "Operations for managing professional skills: extraction from text and creation requests.")
public interface VisOpenApiSkill {

	/**
	 * Extract skills from free text.
	 * @param sessionValues the request body
	 * @return the response body
	 */
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
}
