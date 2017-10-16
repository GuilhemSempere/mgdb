/**
 * ***********************************************************************
 * GIGWA - Genotype Investigator for Genome Wide Analyses
 * Copyright (C) 2016 <CIRAD>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License, version 3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * See <http://www.gnu.org/licenses/agpl.html> for details about GNU Affero
 * General Public License V3.
 * ***********************************************************************
 */
package fr.cirad.tools.security.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javassist.tools.rmi.ObjectNotFoundException;

@Component
public abstract class AbstractTokenManager {

    static private final Logger LOG = Logger.getLogger(AbstractTokenManager.class);
        
	static public final String ENTITY_PROJECT = "project";
	static public final String ROLE_READER = "READER";

    abstract public String createAndAttachToken(String username, String password) throws IllegalArgumentException, IOException;
    
    abstract public Authentication getAuthenticationFromToken(String token);
 
    abstract public boolean removeToken(String token);
    abstract public String generateToken(Authentication auth) throws IllegalArgumentException, UnsupportedEncodingException;

    abstract public boolean canUserReadProject(String token, String module, Comparable projectId);
    abstract public boolean canUserReadProject(Authentication authentication, String module, Comparable projectId);
    
    abstract public boolean canUserReadDB(String token, String module) throws ObjectNotFoundException;    
    abstract public boolean canUserReadDB(Authentication authentication, String module) throws ObjectNotFoundException;

	abstract public void cleanupTokenMap() throws ParseException;
	
    abstract public int getSessionTimeoutInSeconds();

    abstract public void setSessionTimeoutInSeconds(int sessionTimeoutInSeconds);
    
    public String readToken(HttpServletRequest request)
    {
        String token = request.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer "))
			token = token.substring(7);
		return token;
    }
}