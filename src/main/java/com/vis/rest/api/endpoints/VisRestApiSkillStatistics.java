package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constants.CcpOtherConstants;
import com.vis.business.statistics.VisBusinessMonthsOfTheSkill;
import com.vis.business.statistics.VisBusinessSkillsOfTheMonth;
import com.vis.rest.open.api.VisOpenApiSkillStatistics;
import com.vis.services.VisServiceSkillStatistics;

/**
 * REST controller for the statistics of the skills at path {@code /statistics/skills}: public, with no session.
 */
@CrossOrigin
@RestController
@RequestMapping("statistics/skills")
public class VisRestApiSkillStatistics implements VisOpenApiSkillStatistics {

	/**
	 * The skills of a month, region and seniority. Delegates to the matching service.
	 * @param yearMonth the {@code yearMonth} path variable
	 * @param seniority the {@code seniority} path variable
	 * @param ddd the {@code ddd} path variable
	 * @return the response body
	 */
	@GetMapping("/{yearMonth}/{seniority}/{ddd}")
	public Map<String, Object> getSkillsOfTheMonth(@PathVariable("yearMonth") String yearMonth, @PathVariable("seniority") String seniority, @PathVariable("ddd") Integer ddd) {
		var json = CcpOtherConstants.EMPTY_JSON
				.put(VisBusinessSkillsOfTheMonth.Fields.yearMonth, yearMonth)
				.put(VisBusinessSkillsOfTheMonth.Fields.seniority, seniority)
				.put(VisBusinessSkillsOfTheMonth.Fields.ddd, ddd);
		var execute = VisServiceSkillStatistics.GetSkillsOfTheMonth.execute(json.content);
		return execute;
	}

	/**
	 * The months of a skill in a region and seniority. Delegates to the matching service.
	 * @param seniority the {@code seniority} path variable
	 * @param ddd the {@code ddd} path variable
	 * @param skill the {@code skill} query parameter
	 * @return the response body
	 */
	@GetMapping("/{seniority}/{ddd}/months")
	public Map<String, Object> getMonthsOfTheSkill(@PathVariable("seniority") String seniority, @PathVariable("ddd") Integer ddd, @RequestParam("skill") String skill) {
		var json = CcpOtherConstants.EMPTY_JSON
				.put(VisBusinessMonthsOfTheSkill.Fields.seniority, seniority)
				.put(VisBusinessMonthsOfTheSkill.Fields.ddd, ddd)
				.put(VisBusinessMonthsOfTheSkill.Fields.skill, skill);
		var execute = VisServiceSkillStatistics.GetMonthsOfTheSkill.execute(json.content);
		return execute;
	}

	/**
	 * The links of the Telegram groups read by the load. Delegates to the matching service.
	 * @return the response body
	 */
	@GetMapping("/telegramGroups")
	public Map<String, Object> getTelegramGroups() {
		var execute = VisServiceSkillStatistics.GetTelegramGroups.execute(CcpOtherConstants.EMPTY_JSON.content);
		return execute;
	}
}
