package com.jno.cloud.framework.auth.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author 3hyzy
 * @data 2020-04-15
 **/
public interface AuthFilter {


    void Filter(Authentication authentication, UserDetails userDetails);
}
