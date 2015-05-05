Current build status: [![Build Status](https://travis-ci.org/as-ideas/ideas-dash.svg?branch=master)](https://travis-ci.org/as-ideas/ideas-dash)

An extendable Dashboard for development teams

How it works?

- Quickstart
Create a Spring-Boot-Application like the one you can find in the dash-example-module

- Terms
-- CheckResults can be grouped in the UI. Thats what Groups are for
-- Teams are used as filters. This way many teams can share (no team specified) checks/results or have checks executed that only concern them and not interfere with other teams

- Supported Checks
-- Jenkins
Jenkins can be queried for build results if you provide JenkinsChecks via a CheckProvider. JenkinsJobListCheckProvider may come in handy for you as it parses the complete list of jenkins jobs for specified patterns and creates JenkinsChecks automatically.
No more manual configuration for these.
-- Jira
Jira can be queried for issues (Bugs, Stories, Whatever). You may like to take a look at JiraQueryBuilder as a starting point for creating JiraChecks.
-- Fabric
FabricCheck
-- DataDog
DataDog can be queried for monitors. Just provide DataDogChecks via a CheckProvider.
-- Art-Tests
Art is supported as well in theory. This was not used in a while and may prove unstable.


```xml
<dependencies>
    <dependency>
        <groupId>de.axelspringer.ideas.tools</groupId>
        <artifactId>ideas-de.axelspringer.ideas.tools.dash</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>ideas-de.axelspringer.ideas.tools.dash</id>
        <url>https://raw.github.com/as-ideas/ideas-de.axelspringer.ideas.tools.dash/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```
