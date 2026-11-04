package com.vis.rest.api.endpoints;

import java.util.Map;

import org.springframework.web.servlet.HandlerMapping;

import com.ccp.decorators.CcpJsonFieldName;
import com.ccp.decorators.CcpTimeDecorator;

import jakarta.servlet.http.HttpServletRequest;

/**
 * The {@code Cache-Control} of each route of {@link VisRestApiSkillStatistics}, one item per method of the controller
 * (Enum as Decisor). Read by the CDN in front of these routes ({@code documentation/deploy/plano-cdn-estatisticas.md}):
 * what changes only with a reload of the positions stays 1 year in the CDN (the reload invalidates it); what still
 * changes, the current month and the months of a skill (they include the current month), stays 1 hour.
 */
public enum VisSkillStatisticsCacheControl {

	/** A past month never changes until a reload; the current month (America/Sao_Paulo) and later ones still do. */
	getSkillsOfTheMonth {
		@SuppressWarnings("unchecked")
		String getHeaderValue(HttpServletRequest request) {
			String pathVariablesAttribute = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
			Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(pathVariablesAttribute);
			String yearMonthName = Fields.yearMonth.name();
			String yearMonth = pathVariables.get(yearMonthName);
			CcpTimeDecorator now = new CcpTimeDecorator();
			String yearMonthPattern = Fields.YEAR_MONTH_PATTERN.getValue();
			String currentMonth = now.getFormattedDateTime(yearMonthPattern);
			boolean pastMonth = yearMonth.compareTo(currentMonth) < 0;
			return pastMonth ? Fields.LONG.getValue() : Fields.SHORT.getValue();
		}
	},

	/** The months of a skill include the current month. */
	getMonthsOfTheSkill {
		String getHeaderValue(HttpServletRequest request) {
			return Fields.SHORT.getValue();
		}
	},

	/** The groups change only with a reload of the positions. */
	getTelegramGroups {
		String getHeaderValue(HttpServletRequest request) {
			return Fields.LONG.getValue();
		}
	},
	;

	/**
	 * The {@code Cache-Control} of a successful response of the route.
	 * @param request the request
	 * @return the value of the header
	 */
	abstract String getHeaderValue(HttpServletRequest request);

	enum Fields implements CcpJsonFieldName {
		/** 5 minutes in the browser, 1 year (the limit of Cloud CDN) in the CDN. */
		LONG {
			public String getValue() {
				return "public, max-age=300, s-maxage=31536000";
			}
		},
		/** 1 minute in the browser, 1 hour in the CDN. */
		SHORT {
			public String getValue() {
				return "public, max-age=60, s-maxage=3600";
			}
		},
		YEAR_MONTH_PATTERN {
			public String getValue() {
				return "yyyyMM";
			}
		},
		yearMonth,
	}
}
