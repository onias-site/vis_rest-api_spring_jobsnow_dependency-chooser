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
			+ "What does it do? Returns how many active resumes the region has, the averages of the CLT and PJ salary expectations "
			+ "by seniority and of all of them (counting only the resumes that informed them), and the slices of the pie charts: "
			+ "resumeType, spokenLanguage (name|level, 2 fluent and 1 intermediate), skill, pcd, travel, seniority, "
			+ "disponibility (immediate or notImmediate), temporallyJob (accepts or doesNotAccept), clt and pj (the start of each range of 1000, "
			+ "or notInformed). No login is needed."
			+ "<br/><br/><b>Path variables:</b><ul>"
			+ "<li><b>ddd</b> – Required. The region: one of the DDDs of the metropolitan regions, or 10 for home office.</li>"
			+ "</ul>"
	)
	@ApiResponses({
		@ApiResponse(content = {
			@Content(schema = @Schema(example = "{\"ddd\": 11, \"total\": 2, \"averages\": {\"pleno\": {\"resumes\": 1, \"clt\": 9000, \"pj\": 0}, \"all\": {\"resumes\": 2, \"clt\": 8500, \"pj\": 12000}}, "
					+ "\"charts\": {\"resumeType\": {\"1\": 2}, \"spokenLanguage\": {\"Inglês|2\": 1, \"Inglês|1\": 1}, \"clt\": {\"8000\": 1, \"9000\": 1}}}")) },
			responseCode = "200",
			description = "The statistics. A region with no resume has total 0, averages at 0 and empty charts."),
		@ApiResponse(responseCode = "400", description = "The ddd is not a number."),
		@ApiResponse(responseCode = "422", description = "Validation error — the ddd is not one of the regions of the resume."),
	})
	@GetMapping("/{ddd}")
	Map<String, Object> getResumeStatisticsOfTheRegion(@PathVariable("ddd") Integer ddd);
}
