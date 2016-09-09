MatchMaker
===============
Pre-requisits
-----------------
Tomcat version 6 or above</br>
Java 1.8</br>
Maven 3</br>
Configuration and Installation (As a Web Application)
-----------------
1) Checkout the Matchmaker repository
~~~
git clone https://github.com/Data-to-Insight-Center/SA-Matchmaker.git
~~~
2) Add Research Object, Repository and People profile templates to the source 
~~~
SA-Matchmaker/src/main/resources/edu/indiana/d2i/matchmaker/profile
~~~
Sample profiles templates are already available in the source at https://github.com/Data-to-Insight-Center/SA-Matchmaker/tree/master/src/main/resources/edu/indiana/d2i/matchmaker/profile
</br>
</br>
3) Build the module
~~~
cd SA-Matchmaker
mvn clean install -DskipTests=true
~~~
4) Copy the .war file to tomcat/webapps folder
~~~
cp target/matchmaker-1.0.0.war CATALINA_HOME/webapps/mm.war
~~~
5) Unzip mm.war and add Repositories and People profiles to web application
~~~
unzip CATALINA_HOME/webapps/mm.war -d CATALINA_HOME/webapps/mm
cp repositories.json person.json CATALINA_HOME/webapps/mm/WEB-INF/classes/edu/indiana/d2i/matchmaker/profile/
~~~
6) Start tomcat

Use matchmaker - REST API
-----------------
1) Send a POST request to http://&lt;host&gt;:&lt;port&gt;/mm/mm/rest with the following request as the POST body
~~~
{
  "@context": [
    "https://w3id.org/ore/context"
  ],
  "Rights Holder": "https://sead2-beta.ncsa.illinois.edu/api/users/568acec7f26f437b16cafac4",
  "Aggregation": {
    "Uploaded By": "https://sead2-beta.ncsa.illinois.edu/api/users/568acec7f26f437b16cafac4",
    "Title": "test_dataset1 - new-cur-obj-test",
    "Creation Date": "04-03-2016",
    "similarTo": "https://sead2-beta.ncsa.illinois.edu/datasets/56966fc4e4b01d13f5ced4a5",
    "Creator": ["0000-0001-9351-557X"],
    "Abstract": ["Its a simple test"],
    "Identifier": "https://sead2-beta.ncsa.illinois.edu/spaces/curations/56da04d7e4b09d159f672258",
    "Publishing Project": "https://sead2-beta.ncsa.illinois.edu/spaces/56966fa6e4b01d13f5ced4a3",
    "@id": "https://sead2-beta.ncsa.illinois.edu/api/curations/56da04d7e4b09d159f672258/ore#aggregation"
  },
  "Preferences": {
    "Purpose": "Testing-Only",
    "Repository": null
  },
  "Aggregation Statistics": {
    "Data Mimetypes": [
      "image/png",
      "text/plain"
    ],
    "Number of Datasets": 2,
    "Number of Collections": 1,
    "Max Dataset Size": "18317",
    "Total Size": "11000000",
    "Max Collection Depth": "2"
  }
}
~~~
The result would be;
~~~
[
  {
    "orgidentifier": "sda",
    "Per Rule Scores": [
      {
        "Score": 1,
        "Message": "Total size is acceptable (<= 1000000000)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Total Size"
      },
      {
        "Score": 1,
        "Message": "Collection depth is acceptable (<= 4)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Collection Depth"
      }
    ],
    "Total": 2,
    "repositoryName": "IU SEAD Cloud",
    "Result": "Percentage of total attributes matched : 100.00% | Percentage of critical attributes matched : 100.00%"
  },
  {
    "orgidentifier": "ideals",
    "Per Rule Scores": [
      {
        "Score": 1,
        "Message": "Total size is acceptable (<= 10000000000)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Total Size"
      },
      {
        "Score": -1,
        "Message": "Collection depth is not acceptable (<= 1)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Collection Depth"
      }
    ],
    "Total": 0,
    "repositoryName": "IDEALS",
    "Result": "Percentage of total attributes matched : 50.00% | Percentage of critical attributes matched : 50.00%"
  },
  {
    "orgidentifier": "bob",
    "Per Rule Scores": [
      {
        "Score": 1,
        "Message": "Total size is acceptable (<= 10000000)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Total Size"
      },
      {
        "Score": 1,
        "Message": "Collection depth is acceptable (<= 10)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Collection Depth"
      }
    ],
    "Total": 2,
    "repositoryName": "SEAD NDS Labs Publisher (Proof-of-Concept)",
    "Result": "Percentage of total attributes matched : 100.00% | Percentage of critical attributes matched : 100.00%"
  },
  {
    "orgidentifier": "openicpsr",
    "Per Rule Scores": [
      {
        "Score": 1,
        "Message": "Total size is acceptable (<= 10000000000)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Total Size"
      },
      {
        "Score": -1,
        "Message": "Collection depth is not acceptable (<= 1)",
        "Attribute Type": "Critical",
        "Rule Name": "Maximum Collection Depth"
      }
    ],
    "Total": 0,
    "repositoryName": "Inter-university Consortium for Political and Social Research",
    "Result": "Percentage of total attributes matched : 50.00% | Percentage of critical attributes matched : 50.00%"
  }
]
~~~

Use matchmaker - Web UI
-----------------
Web UI of the Matchmaker is available at http://&lt;host&gt;:&lt;port&gt;/mm/crud-table.html. There you can view the rules, edit rules and send requests with Research Object profiles to get matched repositories. 
</br>
</br>


