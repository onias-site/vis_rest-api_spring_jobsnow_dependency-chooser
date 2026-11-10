package com.vis.rest.api;


import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.gcp.memorystore.CcpGcpMemoryStore;
import com.ccp.implementations.db.bulk.elasticsearch.CcpElasticSerchDbBulk;
import com.ccp.implementations.db.crud.elasticsearch.CcpElasticSearchCrud;
import com.ccp.implementations.db.query.elasticsearch.CcpElasticSearchQueryExecutor;
import com.ccp.implementations.db.utils.elasticsearch.CcpElasticSearchDbRequest;
import com.ccp.implementations.email.sendgrid.CcpSendGridEmailProvider;
import com.ccp.implementations.file.bucket.gcp.CcpGcpFileBucket;
import com.ccp.implementations.http.apache.mime.CcpApacheMimeHttp;
import com.ccp.implementations.instant.messenger.telegram.CcpTelegramInstantMessenger;
import com.ccp.implementations.json.gson.CcpGsonJsonHandler;
import com.ccp.implementations.main.authentication.gcp.oauth.CcpGcpMainAuthentication;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.CcpGcpPubSubMensageriaSender;
import com.ccp.implementations.password.mindrot.CcpMindrotPasswordHandler;
import com.ccp.local.testings.implementations.CcpLocalInstances;
import com.ccp.local.testings.implementations.cache.CcpLocalCacheInstances;
import com.ccp.rest.api.spring.exceptions.handler.CcpRestApiExceptionHandlerSpring;
import com.ccp.rest.api.spring.response.CcpRestApiSessionValuesRemoverSpring;
import com.ccp.rest.api.spring.openapi.CcpMissingPathParamsCustomizer;
import com.ccp.rest.api.spring.servlet.filters.CcpCorsAndMethodsFilter;
import com.ccp.rest.api.spring.servlet.filters.CcpDbCallsLogFilter;
import com.ccp.rest.api.spring.servlet.filters.CcpPutSessionValuesAndExecuteTaskFilter;
import org.springframework.core.Ordered;
import com.ccp.rest.api.spring.servlet.filters.CcpValidEmailFilter;
import com.ccp.rest.api.utils.CcpRestApiUtils;
import com.jn.business.errors.JnBusinessSaveApiError;
import com.jn.json.fields.validation.JnApiOrigins;
import com.jn.business.session.JnBusinessValidateSession;
import com.vis.rest.api.endpoints.VisRestApiResume;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Entry point of the REST API of the vis cost center. Wires the dependencies (local implementations when
 * {@code localEnvironment} is set, GCP and Elasticsearch otherwise), records unhandled errors and protects
 * {@code /resume/*}, {@code /recruiter/*} and {@code /recruiters/*} with the e-mail syntax filter and the
 * session validation.
 */
@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		VisRestApiResume.class,
		CcpRestApiExceptionHandlerSpring.class,
		CcpRestApiSessionValuesRemoverSpring.class,
})
@SpringBootApplication
public class VisRestApiSpringStarter {
	
	/**
	 * Wires the dependencies and starts Spring.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		CcpGsonJsonHandler ccpGsonJsonHandler = CcpGsonJsonHandler.INSTANCE;
		CcpDependencyInjection.loadAllDependencies(ccpGsonJsonHandler);
		
		boolean localEnvironment = CcpRestApiUtils.isLocalEnvironment();	
		CcpTelegramInstantMessenger ccpTelegramInstantMessenger = CcpTelegramInstantMessenger.INSTANCE;
		CcpElasticSearchDbRequest ccpElasticSearchDbRequest = CcpElasticSearchDbRequest.INSTANCE;
		CcpMindrotPasswordHandler ccpMindrotPasswordHandler = CcpMindrotPasswordHandler.INSTANCE;
		CcpGcpMainAuthentication ccpGcpMainAuthentication = CcpGcpMainAuthentication.INSTANCE;
		CcpElasticSerchDbBulk ccpElasticSerchDbBulk = CcpElasticSerchDbBulk.INSTANCE;
		CcpElasticSearchCrud ccpElasticSearchCrud = CcpElasticSearchCrud.INSTANCE;
		CcpApacheMimeHttp ccpApacheMimeHttp = CcpApacheMimeHttp.INSTANCE;
		// the queries (e.g. the purge of a versionable record's history, the orphan items of a withdrawn
		// skill hierarchy fix request) need it
		CcpElasticSearchQueryExecutor ccpElasticSearchQueryExecutor = CcpElasticSearchQueryExecutor.INSTANCE;
		CcpDependencyInjection.loadAllDependencies
		(
				localEnvironment ? CcpLocalInstances.asyncMensageriaListener : CcpGcpPubSubMensageriaSender.INSTANCE,
				localEnvironment ? CcpLocalCacheInstances.map : CcpGcpMemoryStore.INSTANCE,
				localEnvironment ? CcpLocalInstances.bucket : CcpGcpFileBucket.INSTANCE,
				localEnvironment ? CcpLocalInstances.email : CcpSendGridEmailProvider.INSTANCE,
				// local: writes to c:\logs\telegram instead of sending a real message to the support group
				localEnvironment ? CcpLocalInstances.instantMessenger : ccpTelegramInstantMessenger,
				ccpElasticSearchDbRequest,
				ccpMindrotPasswordHandler
				,
				ccpGcpMainAuthentication,
				ccpElasticSerchDbBulk,
				ccpElasticSearchCrud,ccpElasticSearchQueryExecutor,ccpApacheMimeHttp
		);

		CcpRestApiExceptionHandlerSpring.genericExceptionHandler = new JnBusinessSaveApiError(JnApiOrigins.vis);
		SpringApplication.run(VisRestApiSpringStarter.class, args);
	}
	
	/**
	 * Adds to the OpenAPI document the path variables that an operation uses in its path but does not declare.
	 * @return the customizer
	 */
	@Bean
	public GlobalOpenApiCustomizer missingPathParamsCustomizer() {
		return CcpMissingPathParamsCustomizer.INSTANCE;
	}

	/**
	 * Describes the API in the OpenAPI document.
	 * @return the description
	 */
	@Bean
	public OpenAPI visOpenAPI() {
		OpenAPI openAPI = new OpenAPI();
		Info info2 = new Info();
		Info title = info2
						.title("JobsNow VIS API");
						Info description = title
						.description("REST API for the VIS module: resume management, recruiters, companies and skills.");
						Info version = description
						.version("1.0");
						OpenAPI info = openAPI
						.info(version);
						return info;
	}

	/**
	 * Serves the static resources of the Swagger UI.
	 * @return the configurer
	 */
	@Bean
	public WebMvcConfigurer swaggerResourceHandler() {
		var webMvcConfigurer = new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				ResourceHandlerRegistration addResourceHandler = registry.addResourceHandler("/webjars/**");
				addResourceHandler
						.addResourceLocations("classpath:/META-INF/resources/webjars/");
						ResourceHandlerRegistration addResourceHandler2 = registry.addResourceHandler("/swagger-ui/**");
						addResourceHandler2
						.addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
			}
		};
		return webMvcConfigurer;
	}

	/**
	 * Measures the calls to the database of every request and writes them in one line of the standard output, read by
	 * the local panel of the requests ({@code documentation/deploy/27-requisicoes-do-cloud-run.py}). Runs before every
	 * other filter, so the session validation is measured too.
	 * @return the filter registration
	 */
	@Bean
	public FilterRegistrationBean<CcpDbCallsLogFilter> dbCallsLogFilter() {
		FilterRegistrationBean<CcpDbCallsLogFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(CcpDbCallsLogFilter.INSTANCE);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegistration;
	}

	/**
	 * The CORS of the API (only the origins of {@code ccp.cors.allowed-origins}, in {@code application.properties}) and
	 * the refusal of {@code TRACE}; runs right after the measurements, before the filters of e-mail and session, so their
	 * errors also reach the front end with the CORS headers.
	 * @param allowedOrigins the origins allowed, separated by commas
	 * @return the filter registration
	 */
	@Bean
	public FilterRegistrationBean<CcpCorsAndMethodsFilter> corsAndMethodsFilter(@Value("${ccp.cors.allowed-origins:}") String allowedOrigins) {
		FilterRegistrationBean<CcpCorsAndMethodsFilter> filterRegistration = new FilterRegistrationBean<>();
		CcpCorsAndMethodsFilter filter = new CcpCorsAndMethodsFilter(allowedOrigins);
		filterRegistration.setFilter(filter);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return filterRegistration;
	}

	/**
	 * Rejects an invalid e-mail in the path of {@code /resume/*}, {@code /recruiter/*} and {@code /recruiters/*}.
	 * @return the filter registration
	 */
	@Bean
	public FilterRegistrationBean<CcpValidEmailFilter> emailFilter() {
		FilterRegistrationBean<CcpValidEmailFilter> filterRegistration = new FilterRegistrationBean<>();
		CcpValidEmailFilter emailSyntaxFilter = CcpValidEmailFilter.getEmailSyntaxFilter("resume/", "recruiter/", "recruiters/");
		filterRegistration.setFilter(emailSyntaxFilter);
		filterRegistration.addUrlPatterns("/resume/*", "/recruiter/*", "/recruiters/*");
		
		return filterRegistration;
	}
	
	/**
	 * Validates the session ({@code JnBusinessValidateSession}, which runs the validator seeded by the login) on {@code /resume/*}, {@code /recruiter/*} and {@code /recruiters/*}.
	 * @return the filter registration
	 */
	@Bean
	public FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> validateSessionFilter() {
		FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> filterRegistration = new FilterRegistrationBean<>();
		CcpPutSessionValuesAndExecuteTaskFilter filter = new CcpPutSessionValuesAndExecuteTaskFilter(JnBusinessValidateSession.INSTANCE);
		filterRegistration.setFilter(filter);
		filterRegistration.addUrlPatterns("/resume/*", "/recruiter/*", "/recruiters/*");
		return filterRegistration;
	}
}
