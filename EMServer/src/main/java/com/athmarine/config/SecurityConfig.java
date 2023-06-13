package com.athmarine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
				"/configuration/security", "/swagger-ui.html", "/webjars/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.cors().and().csrf().disable()
		.authorizeRequests().antMatchers("/authenticate").permitAll()
		// all other requests need to be authenticated
		.antMatchers("/user/add","/user/email","/user/deleteUserByEmail/**").permitAll()
		.antMatchers("/vendor/add", "/vendor/updateBuisnessInformation").permitAll()
		.antMatchers("/vendor/createVendorEngineer").permitAll().antMatchers("/service/createService").permitAll()
		.antMatchers("/vendor/createVendorBidder").permitAll()
		.antMatchers("/vendor/createVendorApprover", "/vendor/getAllVendorBidder/**",
				"/vendor/getAllVendorBidderByApproverId/**", "/vendor/getVendorBidder/**",
				"/vendor/createVendorAdmin", "/vendor/getVendorAdmin/**")
		.permitAll().antMatchers("/finance/get/**").permitAll()
		.antMatchers("/master/createMasterPort", "/service/createService","/masterPromotion/getAllMasterPromotion","/masterPromotion/updateMasterPromotion").permitAll()
		.antMatchers("/vendor/createVendorCompanyHead").permitAll().antMatchers("/module/add").permitAll()
		.antMatchers("/departmentName/add", "/departmentName/get/**").permitAll()
		.antMatchers("/equipment/category/add").permitAll().antMatchers("/role/add").permitAll()
		.antMatchers("/master/getStateListId/**").permitAll().antMatchers("/master/getCityListId/**")
		.permitAll().antMatchers("/user/updateEmail").permitAll().antMatchers("/user/updatePhoneNumber")
		.permitAll().antMatchers("/equipment/getAllEquipmentByCategory/**").permitAll()
		.antMatchers("/equipment/getAllEquipmentByCategory/**").permitAll()
		.antMatchers("/equipment/getAllEquipmentName").permitAll().antMatchers("/equipment/getEquipmentName/**")
		.permitAll()
		.antMatchers("/emailSubscription/createEmailSubscription").permitAll()
		.antMatchers("/phoneSubscription/createPhoneSubscription").permitAll()
		.antMatchers("/contact/addContactUs","/user/emailVerification").permitAll()
		.antMatchers("/finance/add").permitAll().antMatchers("/vendor/createVendorAdmin").permitAll()
		.antMatchers("/vendor/createVendorAdmin", "/manufacturer/getAllEquipmentManufacturer/**").permitAll()
		.antMatchers("/user/validateEmailAndPhone").permitAll()
		.antMatchers("/finance/add").permitAll().antMatchers("/vendor/createVendorAdmin").permitAll()
		.antMatchers("/master/getCountryDetails").permitAll()
		.antMatchers("/equipment/category/getAllEquipmentCategory/**", "/equipment/category/add",
				"/master/getPortsListCountryId/**", "/master/getPortsListByStateId/**",
				"/master/VerifiedMasterPort", "/availableOn/add", "/availableOn/get/**",
				"/master/getCityListByCountryId/**","/chages/getEngineerCharges/**",
				"/master/getCountryCurrencyList/**")
		.permitAll().antMatchers("/role/get").permitAll().antMatchers("/customer/createCustomerAdmin")
		.permitAll().antMatchers("/customer/add").permitAll().antMatchers("/customer/update").permitAll()
		.antMatchers("/upload").permitAll().antMatchers("/customer/addRequester").permitAll()
		.antMatchers("/customer/getAllRequester/**").permitAll().antMatchers("/master/getCountryList")
		.permitAll()
		.antMatchers("/customer/approver/add","/customer/approver/getByCompanyId/**").permitAll()
		.antMatchers("/customer/getAllApprover/**").permitAll().antMatchers("/customer/addHead","/resetPassword","/updatePassword").permitAll()
		.antMatchers("/customer/addFinance","/customer/getByCompanyId/**","/user/getUserByEncodedUrl/**").permitAll().antMatchers("/OTP/getEmailOTP/**").permitAll()
		.antMatchers("/OTP/getSmsOTP/**").permitAll().antMatchers("/OTP/validateOTPEmail").permitAll()
		.antMatchers("/equipments/createEngineer").permitAll().antMatchers("/OTP/validateOTPSms").permitAll()
		.antMatchers("/make/getAllMake").permitAll()
		.antMatchers("/vendor/validateReferralCode/**").permitAll()
		.antMatchers("/payment/webhook").permitAll()
				.antMatchers("/vendor/get/**").permitAll()
		.anyRequest().authenticated().and()
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}