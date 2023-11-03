
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
              <url>https://resources.cumulocity.com/maven/repository</url>
            </repository>
          </repositories>
          <pluginRepositories>
            <pluginRepository>
              <id>cumulocity-plugins-repo</id>
              <url>https://resources.cumulocity.com/maven/repository</url>
            </pluginRepository>
          </pluginRepositories>
        </profile>
      </profiles>
    </settings>

  [1]: http://www.cumulocity.com
  [2]: http://maven.apache.org/
  [3]: https://www.cumulocity.com/guides

### Backporting

A github actions plugin for backporting was introduced. 
You can inspect it in `.github/workflows/auto-backport.yml`.
It will automatically prepare Pull Requests to specified branches, when the original Pull Request will be merged.

To do a backport add `auto-backport` label in your Pull Request.
Second, you need a target branches, each specified with `auto-backport-to-<branch_name>`.
For example to backport to branch `release/r0000.0.0` you have to add a label `auto-backport-to-release/r0000.0.0`.

Backporting can be also done without github actions. See documentation for the tool itself: https://github.com/sqren/backport

