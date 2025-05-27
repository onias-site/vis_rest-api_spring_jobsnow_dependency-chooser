package com.ccp.vis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.http.CcpHttpMethods;
import com.ccp.implementations.cache.gcp.memcache.CcpGcpMemCache;
import com.ccp.implementations.db.crud.elasticsearch.CcpElasticSearchCrud;
import com.ccp.implementations.db.utils.elasticsearch.CcpElasticSearchDbRequest;
import com.ccp.implementations.file.bucket.gcp.CcpGcpFileBucket;
import com.ccp.implementations.http.apache.mime.CcpApacheMimeHttp;
import com.ccp.implementations.json.gson.CcpGsonJsonHandler;
import com.ccp.implementations.main.authentication.gcp.oauth.CcpGcpMainAuthentication;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.CcpGcpPubSubMensageriaSender;
import com.ccp.implementations.password.mindrot.CcpMindrotPasswordHandler;
import com.ccp.implementations.text.extractor.apache.tika.CcpApacheTikaTextExtractor;
import com.ccp.local.testings.implementations.CcpLocalInstances;
import com.ccp.local.testings.implementations.cache.CcpLocalCacheInstances;
import com.ccp.rest.api.spring.exceptions.handler.CcpSyncExceptionHandler;
import com.ccp.rest.api.spring.servlet.filters.CcpPutSessionValuesAndExecuteTaskFilter;
import com.ccp.rest.api.spring.servlet.filters.CcpValidEmailFilter;
import com.ccp.rest.api.spring.servlet.filters.CcpValidJsonFilter;
import com.ccp.rest.api.utils.CcpRestApiUtils;
import com.ccp.vis.controller.ControllerVisResume;
import com.jn.business.commons.JnBusinessNotifyError;
import com.jn.business.login.JnBusinessValidateSession;
import com.jn.mensageria.JnMensageriaSender;
import com.vis.commons.json.validations.VisJsonValidationResume;


@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		ControllerVisResume.class, 
		CcpSyncExceptionHandler.class,
})
@SpringBootApplication
public class ApplicationStarterVisSyncSpring {
	
	public static void main(String[] args) {
		boolean localEnvironment = CcpRestApiUtils.isLocalEnvironment();	
		CcpDependencyInjection.loadAllDependencies
		(
				localEnvironment ? CcpLocalInstances.mensageriaSender.getLocalImplementation() : new CcpGcpPubSubMensageriaSender(),
				localEnvironment ? CcpLocalCacheInstances.map.getLocalImplementation() : new CcpGcpMemCache(),
				localEnvironment ? CcpLocalInstances.bucket.getLocalImplementation() : new CcpGcpFileBucket(),
				new CcpApacheTikaTextExtractor(),
				new CcpElasticSearchDbRequest(),
				new CcpMindrotPasswordHandler()
				,new CcpGcpMainAuthentication()
				,new CcpElasticSearchCrud()
				,new CcpGsonJsonHandler()
				,new CcpApacheMimeHttp() 
		);

		CcpSyncExceptionHandler.genericExceptionHandler = new JnMensageriaSender(JnBusinessNotifyError.INSTANCE);
		SpringApplication.run(ApplicationStarterVisSyncSpring.class, args);
	}
	@Bean
	public FilterRegistrationBean<CcpValidEmailFilter> emailFilter() {
		FilterRegistrationBean<CcpValidEmailFilter> filtro = new FilterRegistrationBean<>();
		CcpValidEmailFilter emailSyntaxFilter = CcpValidEmailFilter.getEmailSyntaxFilter("resume/");
		filtro.setFilter(emailSyntaxFilter);
		filtro.addUrlPatterns("/resume/*", "/position/*");
		return filtro;
	}
	
	@Bean
	public FilterRegistrationBean<CcpValidJsonFilter> validateResumeJsonFilter() {
		FilterRegistrationBean<CcpValidJsonFilter> filtro = new FilterRegistrationBean<>();
		CcpValidJsonFilter filter = new CcpValidJsonFilter(VisJsonValidationResume.class, CcpHttpMethods.PATCH, CcpHttpMethods.POST);
		filtro.setFilter(filter);
		filtro.addUrlPatterns("/resume/*");
		return filtro;
	}

	
	@Bean
	public FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> putSessionValuesFilter() {
		FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> filtro = new FilterRegistrationBean<>();
		filtro.setFilter(CcpPutSessionValuesAndExecuteTaskFilter.TASKLESS);
		filtro.addUrlPatterns("/resume/*", "/position/*");
		return filtro;
	}

	@Bean
	public FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> validateSessionFilter() {
		FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> filtro = new FilterRegistrationBean<>();
		CcpPutSessionValuesAndExecuteTaskFilter filter = new CcpPutSessionValuesAndExecuteTaskFilter(JnBusinessValidateSession.INSTANCE);
		filtro.setFilter(filter);
		filtro.addUrlPatterns("/resume/*", "/position/*");
		return filtro;
	}
/*
*/

}
