### Packaging

To build from command line
```
mvn clean install microservice:package 
```

or adding to pom.xml

```
      <plugins>
            <plugin>
                <groupId>com.nsn.cumulocity.clients-java</groupId>
                <artifactId>microservice-package-maven-plugin</artifactId>

                <executions>
                    <execution>
                        <goals>
                            <goal>package</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

### Uploading
To upload microservice first configure settings.xml
```
	<server>
	    <id>microservice</id>
	    <username>management/admin</username>
	    <password>****</password>
	    <configuration>
		    <url>http://cumulocity.default.svc.cluster.local</url>
	    </configuration>
	</server>
```

Then run
```
mvn clean install microservice:package microservice:upload
```

Or if is already built
```
mvn microservice:upload
```