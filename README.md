Cumulocity Clients Java
---------------

This repository contains [Cumulocity] [1] client libraries for Java. For more information on [Cumulocity] [1] visit [http://www.cumulocity.com] [1].

Access to [Cumulocity] [1] [Maven repository] [2] is required to build the code.


Building with Maven
---------------

Please add [Cumulocity] [1] [Maven repository] [2] to Your `settings.xml` like this:

    <settings>
      <activeProfiles>
        <activeProfile>cumulocity</activeProfile>
      </activeProfiles>

      <profiles>
        <profile>
          <id>cumulocity</id>
          <repositories>
            <repository>
              <id>cumulocity-maven-repo</id>
              <url>http://resources.cumulocity.com/maven/repository</url>
            </repository>
          </repositories>
          <pluginRepositories>
            <pluginRepository>
              <id>cumulocity-plugins-repo</id>
              <url>http://resources.cumulocity.com/maven/repository</url>
            </pluginRepository>
          </pluginRepositories>
        </profile>
      </profiles>
    </settings>

  [1]: http://www.cumulocity.com
  [2]: http://maven.apache.org/
  [3]: https://www.cumulocity.com/guides
  
  ------------------------------

These tools are provided as-is and without warranty or support. They do not constitute part of the Software AG product suite. Users are free to use, fork and modify them, subject to the license agreement. While Software AG welcomes contributions, we cannot guarantee to include every contribution in the master project.

