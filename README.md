# Maven Plugin : Placeholders Mustachifier

## Overview

XL Deploy simplifies configuration management by removing environment specific values from configuration files.  During deployment, XL Deploy will apply configurations with values appropriate for the deployment environment.

To leverage this capability, configuration files have 'mustachified' property placeholders instead of values.  In XL Deploy, you create a dictionary that maps property placeholders to a value.  Dictionaries are assigned to environments.

For example, it is common for database connection information to vary from one environment to another.  In a configuration file set up for XL Deploy configuration management, the configuration might look like this:

```xml
<Connector id="Default JDBC Database">
    JdbcDriver org.postgresql.Driver
    JdbcUrl jdbc:postgresql://{{DB_URL}}/{{DB_NAME}}
    UserName {{DB_USERNAME}}
    Password {{DB_PASSWORD}}
</Connector>
```

A dictionary in XL Deploy would map these placeholders, DB_URL, DB_NAME, DB_USERNAME, and DB_PASSWORD to actual values.  During deployment, XL Deploy will scan the configuration file and replace the placeholder token with the correct value for the environment.

So far so good.  However, this causes a problem for developers.  Generally configuration files are managed in a source code repository along with other code.  During development, programmers will check out the code, do work, build locally and test.  However the application won't run with property placeholders in the configuration files.  That's where this plugin comes in.

## Usage

There are two ways you can use this plugin to solve the 'property placeholder development' problem.  Specify default values in the configuration or use a separate property file for local values.

### Method 1 : Default Values

This plugin can produce configuration files with either property keys or property values.  For example, you can create your configuration files to have property placeholder keys with a default value like this...

```xml
<Connector id="Default JDBC Database">
    JdbcDriver org.postgresql.Driver
    JdbcUrl jdbc:postgresql://{{DB_URL:localhost}}/{{DB_NAME:mydb}}
    UserName {{DB_USERNAME:scott}}
    Password {{DB_PASSWORD:tiger}}
</Connector>
```

When you run a build for local testing, use the 'mustache-tovalue' mode and the plugin will generate a configuration like this...

```xml
<Connector id="Default JDBC Database">
    JdbcDriver org.postgresql.Driver
    JdbcUrl jdbc:postgresql://localhost/mydb
    UserName scott
    Password tiger
</Connector>
```

When building for an XL Deploy deployment, use the 'mustache-tokey' mode and the plugin will generate a configuration like this...

```xml
<Connector id="Default JDBC Database">
    JdbcDriver org.postgresql.Driver
    JdbcUrl jdbc:postgresql://{{DB_URL}}/{{DB_NAME}}
    UserName {{DB_USERNAME}}
    Password {{DB_PASSWORD}}
</Connector>
```

*Pros*

* Developers do not have to manage a local property file.

*Cons*

* All developers would need local development environments configured the same way.

### Method 2 : Local Properties File

If your developers tend to have their own local configurations, this plugin can do a property placeholder substitution, much like XL Deploy, using a local properties file.  Taking the above example ready for XL Deploy...

```xml
<Connector id="Default JDBC Database">
    JdbcDriver org.postgresql.Driver
    JdbcUrl jdbc:postgresql://{{DB_URL}}/{{DB_NAME}}
    UserName {{DB_USERNAME}}
    Password {{DB_PASSWORD}}
</Connector>
```

The developer could create a local properties file like this...

```bash
DB_URL=localhost
DB_NAME=mydb
DB_USERNAME=scott
DB_PASSWORD=tiger
```

Running the plugin 'mustache-tovalue' goal would generate a configuration file like this...

```xml
<Connector id="Default JDBC Database">
    JdbcDriver org.postgresql.Driver
    JdbcUrl jdbc:postgresql://localhost/mydb
    UserName scott
    Password tiger
</Connector>
```

*Pros*

* Developers have full control over their local configurations.

*Cons*

* Developers need to maintain their own local properties file.


More information here: http://xebialabs-community.github.io/placeholders-mustachifier-maven-plugin/0.5-SNAPSHOT/

## Development

The unit test uses a nice tool called `Restito` https://github.com/mkotsur/restito to mock the REST calls

### Docker

The docker folder contains two images to build the plugin using java7 or java8

### Install Locally

By default jar is signed after packaging and before installation. To skip that (testing):

```bash
mvn -Dgpg.skip install
```
