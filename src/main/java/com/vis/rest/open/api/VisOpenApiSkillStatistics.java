package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for the statistics of the skills at path {@code /statistics/skills}.
 */
@RequestMapping("statistics/skills")
@Tag(name = "Skill statistics", description = "How many positions announced in Telegram ask for each skill.")
public interface VisOpenApiSkillStatistics {

	/**
	 * The skills of a month, region and seniority.
	 * @param yearMonth the {@code yearMonth} path variable
	 * @param seniority the {@code seniority} path variable
	 * @param ddd the {@code ddd} path variable
	 * @return the response body
	 */
	@Operation(
		summary = "The skills of a month, region and seniority",
		description = "When does it occur? When the user searches the screen of statistics of the skills. "
			+ "What does it do? Returns how many positions the combination has (each position counted once) and the skills they ask for, "
			+ "each one as a JSON text with its total. No login is needed."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>yearMonth</b> – Required. The month, as yyyyMM.</li>"
			+ "<li><b>seniority</b> – Required. One of estag, junior, pleno, senior, especialista or unknown.</li>"
			+ "<li><b>ddd</b> – Required. The region: a DDD, 10 for home office or 0 for no DDD.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"total\": 12, \"skills\": [\"{\\\"skill\\\":\\\"JAVA\\\",\\\"total\\\":5}\"]}")) },
			responseCode = "200",
			description = "The total and the skills. With no position, the total is 0 and the list is empty."),
		@ApiResponse(responseCode = "400", description = "The ddd is not a number."),
		@ApiResponse(responseCode = "422", description = "Validation error — the yearMonth is not a month in the format yyyyMM from 2015 to the current year, the seniority is not one of the known ones or the ddd is above 99."),
	})
	@GetMapping("/{yearMonth}/{seniority}/{ddd}")
	Map<String, Object> getSkillsOfTheMonth(@PathVariable("yearMonth") String yearMonth, @PathVariable("seniority") String seniority, @PathVariable("ddd") Integer ddd);

	/**
	 * The months of a skill in a region and seniority.
	 * @param seniority the {@code seniority} path variable
	 * @param ddd the {@code ddd} path variable
	 * @param skill the {@code skill} query parameter
	 * @return the response body
	 */
	@Operation(
		summary = "The months of a skill in a region and seniority",
		description = "When does it occur? When the user expands a skill in the screen of statistics of the skills. "
			+ "What does it do? Returns the months that have positions with the skill, each one as a JSON text with its total. No login is needed."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>seniority</b> – Required. One of estag, junior, pleno, senior, especialista or unknown.</li>"
			+ "<li><b>ddd</b> – Required. The region: a DDD, 10 for home office or 0 for no DDD.</li>"
			+ "</ul><b>Query parameters:</b><ul>"
			+ "<li><b>skill</b> – Required. The skill, which goes here because a name may have characters that a path does not accept.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"months\": [\"{\\\"total\\\":5,\\\"yearMonth\\\":\\\"202201\\\"}\"]}")) },
			responseCode = "200",
			description = "The months of the skill. With no position, the list is empty."),
		@ApiResponse(responseCode = "400", description = "The skill is missing or the ddd is not a number."),
		@ApiResponse(responseCode = "422", description = "Validation error — the skill is empty or longer than 50 characters, the seniority is not one of the known ones or the ddd is above 99."),
	})
	@GetMapping("/{seniority}/{ddd}/months")
	Map<String, Object> getMonthsOfTheSkill(@PathVariable("seniority") String seniority, @PathVariable("ddd") Integer ddd, @RequestParam("skill") String skill);

	/**
	 * The links of the Telegram groups read by the load.
	 * @return the response body
	 */
	@Operation(
		summary = "The links of the Telegram groups",
		description = "When does it occur? When the user opens the list of searched groups in the screen of statistics of the skills. "
			+ "What does it do? Returns the links of the Telegram groups whose positions feed the statistics. It takes no parameter and no login is needed."
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"groups\": [\"https://t.me/empregossp\"]}")) },
			responseCode = "200",
			description = "The links of the groups."),
	})
	@GetMapping("/telegramGroups")
	Map<String, Object> getTelegramGroups();
}
