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

    public String getPeopleListUrl() {
        return peopleListUrl;
    }



    private String peopleListUrl;
    private String peopleProfileUrl;
    private String peopleIdentifier;
	private String repoListUrl;
	private String repoProfileUrl;
	private String repoIdentifier;

	public MatchmakerENV(PropertyReader property){
		//this.property = PropertyReader.getInstance(propertiesPath);
		this.property = property;
		this.setRuleJarProfilePath(this.property.getProperty("matchmaker.rule.jar.properties.path"));
		this.setCachedProfileRepositories(this.property.getProperty("cached.profile.repositories"));
		this.setCachedProfilePerson(this.property.getProperty("cached.profile.person"));
		this.setPeopleListUrl(this.property.getProperty("people.list.rest.url"));
		this.setPeopleProfileUrl(this.property.getProperty("people.profile.rest.url"));
		this.setPeopleIdentifier(this.property.getProperty("people.identifier"));
        this.setRepoListUrl(this.property.getProperty("repo.list.rest.url"));
        this.setRepoProfileUrl(this.property.getProperty("repo.profile.rest.url"));
        this.setRepoIdentifier(this.property.getProperty("repo.identifier"));
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

    public void setPeopleListUrl(String peopleListUrl) {
        this.peopleListUrl = peopleListUrl;
    }

    public String getPeopleProfileUrl() {
        return peopleProfileUrl;
    }

    public void setPeopleProfileUrl(String peopleProfileUrl) {
        this.peopleProfileUrl = peopleProfileUrl;
    }

    public String getPeopleIdentifier() {
        return peopleIdentifier;
    }

    public void setPeopleIdentifier(String peopleIdentifier) {
        this.peopleIdentifier = peopleIdentifier;
    }

    public String getRepoListUrl() {
        return repoListUrl;
    }

    public void setRepoListUrl(String repoListUrl) {
        this.repoListUrl = repoListUrl;
    }

    public String getRepoProfileUrl() {
        return repoProfileUrl;
    }

    public void setRepoProfileUrl(String repoProfileUrl) {
        this.repoProfileUrl = repoProfileUrl;
    }

    public String getRepoIdentifier() {
        return repoIdentifier;
    }

    public void setRepoIdentifier(String repoIdentifier) {
        this.repoIdentifier = repoIdentifier;
    }
}
