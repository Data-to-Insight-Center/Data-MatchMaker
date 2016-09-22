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
# Project: Matchmaker Service
# File:  MetaDriver.java
# Description:  Preparing for input and initiate matchmaking.
#
# -----------------------------------------------------------------
# 
*/
package edu.indiana.d2i.matchmaker.drivers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import edu.indiana.d2i.matchmaker.core.MatchMaker;
import edu.indiana.d2i.matchmaker.core.MatchMakingList;
import edu.indiana.d2i.matchmaker.core.POJOGenerator;
import edu.indiana.d2i.matchmaker.util.MatchmakerENV;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author yuanluo
 *
 */
//TODO: query PDT (when available) to get profiles, instead of using local copies.

public class MetaDriver {
	MatchMakingList candidateList = null;
	String responseID=null;
	private Logger log;
	private MatchmakerENV env=null;
	private String message =null;
	
	public MetaDriver(MatchmakerENV env, String message, String responseID){
		log = Logger.getLogger(MetaDriver.class);
		this.env=env;
		this.message=message;
		this.responseID=responseID;
		ObjectMapper mapper = new ObjectMapper();
		POJOGenerator pojogen = new POJOGenerator();
		pojogen.fromPath(env.getRuleJarProfilePath());
		ArrayList<File> ruleFileList = new ArrayList<File>();
		ArrayList<String> classNameList = new ArrayList<String>();
		JsonNode ruleJars = pojogen.getJsonTree();
		String mainClassName = ruleJars.get("MatchMakingListClass").asText();
		JsonNode jars = ruleJars.get("jars");
		JsonNode pojoClasses = ruleJars.get("POJOClasses");
		if (jars != null) {
			if (jars.isArray()) {
				ArrayNode jarray = (ArrayNode) jars;
				for (int i = 0; i < jarray.size(); i++) {
					JsonNode jar = jarray.get(i);
					ruleFileList.add(new File(jar.get("jarLocation").asText()));
					ArrayNode classes = (ArrayNode) jar.get("classNames");
					if (classes.isArray()) {
						for (int j = 0; j < classes.size(); j++) {
							classNameList.add(classes.get(j).asText());
						}
					} else {
						classNameList.add(classes.asText());
					}
				}
			} else {
				JsonNode jar = jars;
				ruleFileList.add(new File(jar.get("jarLocation").asText()));
				ArrayNode classes = (ArrayNode) jar.get("classNames");
				if (classes.isArray()) {
					for (int j = 0; j < classes.size(); j++) {
						classNameList.add(classes.get(j).asText());
					}
				} else {
					classNameList.add(classes.asText());
				}
			}
		}
		
		log.info("mainClassName: "+mainClassName);
		
		Object[] repositories;
		Object person;
		Object researchObject;
		File[] ruleFiles;
		String[] classNames;
		try {
			//Generate POJO objects
			POJOGenerator reposGen = null,personGen = null,researchObjectGen = null;
			reposGen = new POJOGenerator(pojoClasses.get("Repository").asText());
			personGen = new POJOGenerator(pojoClasses.get("Person").asText());
			researchObjectGen=new POJOGenerator(pojoClasses.get("ResearchObject").asText());
			
			reposGen.fromString(getRepositories());
			repositories = (Object[]) reposGen.generate();
			researchObjectGen.fromString(message);
			researchObject= (Object) researchObjectGen.generate();
			personGen.fromString(getPerson(researchObject));
			person = (Object) personGen.generate();
			
			ruleFiles = new File[ruleFileList.size()];
			for(int i=0;i<ruleFileList.size();i++){
				ruleFiles[i] = ruleFileList.get(i);
				log.info("RuleFile: "+ruleFiles[i].getPath());
			}
			
			candidateList=(MatchMakingList) Class.forName(mainClassName).getConstructor(ArrayNode.class).newInstance((ArrayNode)reposGen.getJsonTree());
			
			classNames = new String[classNameList.size()];
			for(int i=0;i<classNameList.size();i++){
				classNames[i] = classNameList.get(i);
				log.info("ClassName: "+classNames[i]);
			}
			//log.info("ResearchObject: "+message);
			new MatchMaker().basicGo(System.out, ruleFiles, classNames, candidateList, repositories, person, researchObject);
			
		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getRepositories() throws ClassNotFoundException{
        POJOGenerator reposGen = new POJOGenerator();
        String cachedProfileRepositories = this.env.getCachedProfileRepositories();
        String repoRestUrl = this.env.getRepoListUrl();
        String repoProfileUrl = this.env.getRepoProfileUrl();
        String repoIdentifier = this.env.getRepoIdentifier() != null ? this.env.getRepoIdentifier() : "orgidentifier";
        if (cachedProfileRepositories != null && !cachedProfileRepositories.equals("")) {
            reposGen.fromPath(cachedProfileRepositories);
            //log.info("RepoList: "+reposGen.getJsonTree().toString());
            return reposGen.getJsonTree().toString();
        } else if (repoRestUrl != null && !repoRestUrl.equals("") && repoProfileUrl != null && !repoProfileUrl.equals("")) {
            WebResource WebService = Client.create().resource(repoRestUrl);
            ClientResponse response = WebService
                    .accept("application/json")
                    .type("application/json")
                    .get(ClientResponse.class);
            JSONArray repoObjectList = null;
            try {
                JSONArray finalRepoArray = new JSONArray();
                repoObjectList = new JSONArray(response.getEntity(new GenericType<String>() {}));
                ArrayList<String> repos = new ArrayList<>();
                for(int i= 0 ; i < repoObjectList.length() ; i++) {
                    repos.add(URLEncoder.encode(((JSONObject) repoObjectList.get(i)).getString(repoIdentifier)));
                }
                WebResource WebServiceProfile = Client.create().resource(repoRestUrl);
                for(String repoId : repos) {
                    response = WebServiceProfile.path(repoId)
                            .accept("application/json")
                            .type("application/json")
                            .get(ClientResponse.class);
                    finalRepoArray.put(new JSONObject(response.getEntity(new GenericType<String>() {})));
                }
                reposGen.fromString(finalRepoArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reposGen.getJsonTree().toString();
        } else {
            return null;
        }
    }
	
	public String getPerson(Object researchObject) throws ClassNotFoundException{
        POJOGenerator personGen = new POJOGenerator();
        String cachedProfilePerson = this.env.getCachedProfilePerson();
        String peopleListUrl = this.env.getPeopleListUrl();
        String peopleProfileUrl = this.env.getPeopleProfileUrl();
        String peopleIdentifier = this.env.getPeopleIdentifier() != null ? this.env.getPeopleIdentifier() : "@id";
        if (cachedProfilePerson != null && !cachedProfilePerson.equals("")) {
            personGen.fromPath(cachedProfilePerson);
            //log.info("Person: "+personGen.getJsonTree().get(0).toString());
            return personGen.getJsonTree().get(0).toString(); //return the first person, just for test purpose
        } else if (peopleListUrl != null && !peopleListUrl.equals("") && peopleProfileUrl != null && !peopleProfileUrl.equals("")) {
            WebResource WebService = Client.create().resource(peopleListUrl);
            ClientResponse response = WebService
                    .accept("application/json")
                    .type("application/json")
                    .get(ClientResponse.class);
            JSONArray peopleObjectList = null;
            try {
                JSONArray finalPeopleArray = new JSONArray();
                peopleObjectList = new JSONArray(response.getEntity(new GenericType<String>() {}));
                ArrayList<String> people = new ArrayList<>();
                for(int i= 0 ; i < peopleObjectList.length() ; i++) {
                    people.add(URLEncoder.encode(((JSONObject) peopleObjectList.get(i)).getString(peopleIdentifier)));
                }
                WebResource WebServiceProfile = Client.create().resource(peopleProfileUrl);
                for(String peopleId : people) {
                    response = WebServiceProfile.path(peopleId)
                            .accept("application/json")
                            .type("application/json")
                            .get(ClientResponse.class);
                    finalPeopleArray.put(new JSONObject(response.getEntity(new GenericType<String>() {
                    })));
                }
                personGen.fromString(finalPeopleArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return personGen.getJsonTree().get(0).toString();
        } else {
            return null;
        }
    }

	public MatchmakerENV getENV() {
		// TODO Auto-generated method stub
		return this.env;
	}
	
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.message;
	}
	
	public String exec() {
		// TODO Auto-generated method stub
		return null;
	}

    public String getResults() {
        // TODO Auto-generated method stub
        return null;
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}




}
