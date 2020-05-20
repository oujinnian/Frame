package com.jno.cloud.framework.auth.config;

import com.jno.cloud.framework.auth.security.SecurityAuthenticationFailHandler;
import com.jno.cloud.framework.auth.security.SecurityAuthenticationFilter;
import com.jno.cloud.framework.auth.security.SecurityAuthenticationProvider;
import com.jno.cloud.framework.auth.security.SecurityAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import javax.annotation.Resource;

@EnableWebSecurity //开启security功能
@EnableGlobalMethodSecurity(prePostEnabled = true)//保证post之前的注解可以使用
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;
    @Autowired
    SecurityAuthenticationFailHandler securityAuthenticationFailHandler;
    @Autowired
    SecurityAuthenticationProvider securityAuthenticationProvider;

    @Qualifier("userDetailsServiceImpl") //注入选择的实现类
    @Autowired
    UserDetailsService userDetailsService;

//    @Autowired
//    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @Autowired
//    JwtUserDetailsService jwtUserDetailsService;
//
//    @Autowired
//    JwtAuthorizationTokenFilter authenticationTokenFilter;

    /**
     * 介绍
     * 配置了这个Bean以后，从前端传递过来的密码将被加密
     *  springboot2.x引入的security版本是5.x的，这个版本需要提供一个PasswordEncoder实例，不然就会报错
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

//    //先来这里认证一下
//
//    /**
//     * 登录认证
//     * @param auth
//     * @throws Exception
//     */
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(securityAuthenticationProvider);
//    }

    /**
     * 登录认证
     * 认证的入口
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //添加自定义登录认证
        auth.authenticationProvider(securityAuthenticationProvider);
    }

    //静态资源的访问，跳过filter验证
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()  //跳过验证
                .antMatchers("/**");
        //放行
//            web.ignoring().antMatchers("/getKey","/config/getConfig","/userLogin","/userLogout","/userJwt","/refreshToken","/platformIn","/nouser","/uCenterLogin");
    }

    //配置拦截
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.formLogin()    //表单登陆.
//                .loginPage("/login.html")   //指定登录页
                //登录页面提交页面， 使用UsernamePasswordAuthenticationFilter过滤器处理请求
//                .loginProcessingUrl("/authentication/form")
                .successHandler(securityAuthenticationSuccessHandler)
                .failureHandler(securityAuthenticationFailHandler)
        .and()  //
        .authorizeRequests()    //授权请求配置
                //访问此地址不需要进行身份认证，防止重定向死循环
                .antMatchers("/login.html",
                        "/swagger-ui",
                        "/swagger-ui.html",
                        "/v2/api-docs", // swagger api json
                        "/swagger-resources/configuration/ui", // 用来获取支持的动作
                        "/swagger-resources", // 用来获取api-docs的URI
                        "/swagger-resources/configuration/security", // 安全选项
                        "/swagger-resources/**"
                ).permitAll()
                //方便后面写前后端分离的时候前端过来的第一次验证请求，这样做，会减少这种请求的时间和资源使用
                .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
        .anyRequest()   //任何请求
        .authenticated()    //访问如何资源都需要身份认证
        .and()
        .csrf().disable()//关闭跨站请求伪造攻击拦截，禁用 Spring Security 自带的跨域处理，防止csdf攻击的
                //// 定制我们自己的 session 策略：调整为让 Spring Security 不创建和使用 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //jwt校验，注入两个校验器
//        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
