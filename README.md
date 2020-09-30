# dropwizard-micrometer

[Dropwizard](https://www.dropwizard.io/en/latest/) bundle that enables your dropwizard 
application for exposition of [micrometer-like](http://micrometer.io/) metrics (system, jvm and http requests) as a
prometheus endpoint.

## Usage

### Add dependency into your `pom.xml`
If you use `maven`, you can simply reference it in the `<dependenccies>` block as below. 
The latest version can be found on in the maven 
[repository](https://mvnrepository.com/artifact/io.github.maksymdolgykh.dropwizard)

```xml
    <dependencies>
        ...
        ...
        <dependency>
            <groupId>io.github.maksymdolgykh.dropwizard</groupId>
            <artifactId>dropwizard-micrometer</artifactId>
            <version>1.0.0</version>
        </dependency>
        ...
        ...
    </dependencies>

```
### Import DropwizardMicrometer classes in your Application class
```java
import io.github.maksymdolgykh.dropwizard.micrometer.MicrometerBundle;
import io.github.maksymdolgykh.dropwizard.micrometer.MicrometerHttpFilter;
import javax.servlet.FilterRegistration;
import javax.servlet.DispatcherType;
import java.util.EnumSet;
```


### Add the bundle to your application

Add `MicrometerBundle` class to the bootstrapping phase of your Application class

```java
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    //...
    //...

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        //...
        //...
        bootstrap.addBundle(new MicrometerBundle());
        //...
        //...
    }
    //...
    //...
}
```

### Assign servlet filter to the environment

To leverage latency metrics per http endpoint you need to assign servlet filter to the environment 
in your Application class within `run` method
```java
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    //...
    //...
    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) {
        //...
        //...
        FilterRegistration.Dynamic micrometerFilter = environment.servlets().addFilter("MicrometerHttpFilter", new MicrometerHttpFilter());
        micrometerFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
```
