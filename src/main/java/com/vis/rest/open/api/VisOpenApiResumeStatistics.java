package com.vis.rest.open.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI contract for the statistics of the resumes at path {@code /statistics/resumes}.
 */
@RequestMapping("statistics/resumes")
@Tag(name = "Resume statistics", description = "What the active resumes of each region offer: types, languages, skills, seniority and salary expectations.")
public interface VisOpenApiResumeStatistics {

	/**
	 * The statistics of the resumes of a region.
	 * @param ddd the {@code ddd} path variable
	 * @return the response body
	 */
	@Operation(
		summary = "The statistics of the resumes of a region",
		description = "When does it occur? When the user opens a region in the screen Supply X Demand. "
			+ "What does it do? Reads, in a single round trip, the groups of the region of the active resumes (resumes) and of the views of the "
			+ "resumes by the recruiters (viewedResumes), and returns for each one: how many children it has (total), the averages of the CLT and PJ salary expectations "
			+ "and of the years of experience (one decimal place) by seniority and of all of them (counting only the children that informed them), and the slices of the pie charts: "
			+ "resumeType, spokenLanguage (name|level, 2 fluent and 1 intermediate), skill, pcd, travel, seniority, "
			+ "disponibility (immediate or notImmediate), temporallyJob (accepts or doesNotAccept), skillsCount (upTo5, from6To15, from16To30, "
			+ "from31To50 or over50) and contractType (onlyClt, onlyPj, both or notInformed). No login is needed."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>ddd</b> – Required. The region: one of the DDDs of the metropolitan regions, or 10 for home office.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"ddd\": 11, \"resumes\": {\"total\": 2, \"averages\": {\"pleno\": {\"resumes\": 1, \"clt\": 9000, \"pj\": 0, \"experience\": 4.0}, \"all\": {\"resumes\": 2, \"clt\": 8500, \"pj\": 12000, \"experience\": 6.5}}, "
					+ "\"charts\": {\"resumeType\": {\"1\": 2}, \"spokenLanguage\": {\"Inglês|2\": 1, \"Inglês|1\": 1}}}, "
					+ "\"viewedResumes\": {\"total\": 1, \"averages\": {\"all\": {\"resumes\": 1, \"clt\": 0, \"pj\": 9000, \"experience\": 8.0}}, \"charts\": {\"resumeType\": {\"1\": 1}}}}")) },
			responseCode = "200",
			description = "The statistics. A source with no child has total 0, averages at 0 and empty charts."),
		@ApiResponse(responseCode = "400", description = "The ddd is not a number."),
		@ApiResponse(responseCode = "422", description = "Validation error — the ddd is not one of the regions of the resume."),
	})
	@GetMapping("/{ddd}")
	Map<String, Object> getResumeStatisticsOfTheRegion(@PathVariable("ddd") Integer ddd);

	/**
	 * The counters by ddd, by day and by month of insertion.
	 * @return the response body
	 */
	@Operation(
		summary = "The totals by region and the insertions by day and by month",
		description = "When does it occur? When the user opens the screen Supply X Demand, before opening any folder. "
			+ "What does it do? Reads, in a single round trip, three counters of the active resumes (resumes) and of the views of the resumes "
			+ "by the recruiters (viewedResumes): byRegion, how many records each ddd has (a record with several ddds counts once in each; the screen "
			+ "sums the ddds of each state for the titles of the folders); byDay (yyyyMMdd) and byMonth (yyyyMM), how many records were inserted in "
			+ "each day and month and are still active (the line chart). A key with no record is left out. No login is needed."
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"byRegion\": {\"resumes\": {\"10\": 33, \"11\": 15}, \"viewedResumes\": {\"10\": 57, \"11\": 22}}, "
					+ "\"byDay\": {\"resumes\": {\"20261114\": 2}, \"viewedResumes\": {\"20261113\": 5}}, "
					+ "\"byMonth\": {\"resumes\": {\"202610\": 40, \"202611\": 11}, \"viewedResumes\": {\"202611\": 84}}}")) },
			responseCode = "200",
			description = "The counters of each source; a source with no record has an empty json."),
	})
	@GetMapping("/totals")
	Map<String, Object> getResumeTotals();
}
