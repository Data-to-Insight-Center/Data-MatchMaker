/*
#
# Copyright 2015 The Trustees of Indiana University
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# -----------------------------------------------------------------
#
# Project: Matchmaker
# File:  AbstractENV.java
# Description: Any external abstractions, including environment variables.
#
# -----------------------------------------------------------------
# 
*/

package edu.indiana.d2i.matchmaker.util;

/**
 * @author yuanluo
 * 
 * Reserved for passing through external environment variables.
 */
public class MatchmakerENV {

	/**
	 * @param args
	 */
	private PropertyReader property = null;
	private String RuleJarProfilePath;
	private String CachedProfileRepositories;
	private String CachedProfilePerson;
    private String peopleRestUrl;
	private String repoRestUrl;

	public MatchmakerENV(PropertyReader property){
		//this.property = PropertyReader.getInstance(propertiesPath);
		this.property = property;
		this.setRuleJarProfilePath(this.property.getProperty("matchmaker.rule.jar.properties.path"));
		this.setCachedProfileRepositories(this.property.getProperty("cached.profile.repositories"));
		this.setCachedProfilePerson(this.property.getProperty("cached.profile.person"));
		this.setPeopleRestUrl(this.property.getProperty("people.rest.url"));
        this.setRepoRestUrl(this.property.getProperty("repo.rest.url"));
    }

    public void	setRuleJarProfilePath(String RuleJarProfilePath){
		this.RuleJarProfilePath=RuleJarProfilePath;
	}
	public String getRuleJarProfilePath(){
		return this.RuleJarProfilePath;
	}
	public void	setCachedProfileRepositories(String CachedProfileRepositories){
		this.CachedProfileRepositories=CachedProfileRepositories;
	}
	public String getCachedProfileRepositories(){
		return this.CachedProfileRepositories;
	}
	public void	setCachedProfilePerson(String CachedProfilePerson){
		this.CachedProfilePerson=CachedProfilePerson;
	}
	public String getCachedProfilePerson(){
		return this.CachedProfilePerson;
	}

    public String getPeopleRestUrl() {
        return peopleRestUrl;
    }

    public void setPeopleRestUrl(String peopleRestUrl) {
        this.peopleRestUrl = peopleRestUrl;
    }

    public String getRepoRestUrl() {
        return repoRestUrl;
    }

    public void setRepoRestUrl(String repoRestUrl) {
        this.repoRestUrl = repoRestUrl;
    }
}
