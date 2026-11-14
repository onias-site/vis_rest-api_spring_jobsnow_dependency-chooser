package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for the charts of the screen Curiosities at path {@code /statistics/curiosities}.
 */
@RequestMapping("statistics/curiosities")
@Tag(name = "Curiosities", description = "Counters of the platform: new users, their goals and origins, avoided companies and skills, resumes and tickets.")
public interface VisOpenApiCuriosities {

	/**
	 * The charts of the screen Curiosities.
	 * @return the response body
	 */
	@Operation(
		summary = "The charts of the screen Curiosities",
		description = "When does it occur? When the user opens the screen Curiosities. "
			+ "What does it do? Reads, in a single round trip and only by id, the counters of jn_grouper that every cost center provides to the "
			+ "screen: newUsersByDay (yyyyMMdd) and newUsersByMonth (yyyyMM), the answers of the sign-up questionnaire inserted in each day and month; "
			+ "notAllowedCompanies, the domains of the companies the owners of the active resumes forbid; avoidedSkills, the skills they exclude; "
			+ "goals and channels of the users (sign-up questionnaire); resumeSituation (active and inactive resumes); contactUsTypes, the tickets "
			+ "ever opened by type. A chart with no record is left out. No login is needed."
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"newUsersByDay\": {\"20261114\": 11}, \"newUsersByMonth\": {\"202611\": 84}, "
					+ "\"notAllowedCompanies\": {\"empresa.com.br\": 3}, \"avoidedSkills\": {\"COBOL\": 2}, \"goals\": {\"jobs\": 72, \"recruiting\": 3}, "
					+ "\"channels\": {\"linkedin\": 31, \"telegram\": 16}, \"resumeSituation\": {\"active\": 51, \"inactive\": 4}, "
					+ "\"contactUsTypes\": {\"reportBug\": 5, \"makeCriticism\": 1}}")) },
			responseCode = "200",
			description = "The counters of each chart, {key: total}."),
	})
	@GetMapping
	Map<String, Object> getCuriosities();
}
