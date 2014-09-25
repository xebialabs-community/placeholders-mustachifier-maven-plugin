package com.xebialabs.maven.mustache;


import com.xebialabs.maven.mustache.provider.DefaultValueProvider;
import com.xebialabs.maven.mustache.provider.PropertyFileValueProvider;
import com.xebialabs.maven.mustache.provider.ValueProvider;
import com.xebialabs.maven.mustache.provider.XLDeployValueProvider;

public class ValueProviderConfiguration {

    private PropertyFileValueProvider file;

    private XLDeployValueProvider xldeploy;

    public ValueProvider getValueProvider() {

        if (file == null && xldeploy == null)
            return new DefaultValueProvider();
        if (file != null && xldeploy !=null)
            throw new RuntimeException("You should configure only one value provider (file or xldeploy)");

        if (file != null )
            return file;
        if (xldeploy !=null)
            return xldeploy;

        throw new RuntimeException("Not possible to reach this statement");
    }

    public PropertyFileValueProvider getFile() {
        return file;
    }

    public void setFile(final PropertyFileValueProvider file) {
        this.file = file;
    }

    public XLDeployValueProvider getXldeploy() {
        return xldeploy;
    }

    public void setXldeploy(final XLDeployValueProvider xldeploy) {
        this.xldeploy = xldeploy;
    }
}
