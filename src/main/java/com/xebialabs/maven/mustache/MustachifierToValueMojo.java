package com.xebialabs.maven.mustache;

import com.xebialabs.maven.mustache.transformer.MustacheToValue;
import org.apache.maven.plugin.MojoFailureException;


/**
 * From {{key:value}} to value
 *
 * @author Benoit Moussaud
 * @goal mustache-tovalue
 * @phase compile
 * @threadSafe
 */
public class MustachifierToValueMojo extends AbstractMustachifierMojo {


    /**
     * The input for values (property file or XLDeploy instance
     *
     * @parameter
     */
    protected ValueProviderConfiguration valueProvider = new ValueProviderConfiguration();

    /**
     * True if the goal should rise an error if not finding a value, false by default otherwise
     *
     * @parameter
     */
    protected Boolean mandatoryValues = false;

    protected String transform(final String content) throws MojoFailureException {
        return new MustacheToValue(getBeforeDelimiter(), getAfterDelimiter(), getValueSeparator(), valueProvider.getValueProvider(), mandatoryValues).process(content);
    }

    public ValueProviderConfiguration getValueProvider() {
        return valueProvider;
    }

    public void setValueProvider(final ValueProviderConfiguration valueProvider) {
        this.valueProvider = valueProvider;
    }

    public void setMandatoryValues(final Boolean mandatoryValues) {
        this.mandatoryValues = mandatoryValues;
    }

    public Boolean isMandatoryValues() {
        return mandatoryValues;
    }
}
