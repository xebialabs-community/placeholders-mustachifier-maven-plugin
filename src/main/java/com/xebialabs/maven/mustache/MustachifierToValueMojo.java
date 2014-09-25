package com.xebialabs.maven.mustache;

import com.xebialabs.maven.mustache.transformer.MustacheToValue;


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
     * The separator format from the placeholder name and the default value
     *
     * @parameter
     */
    protected ValueProviderConfiguration valueProvider = new ValueProviderConfiguration();


    protected String transform(final String content) {
        return new MustacheToValue(getBeforeDelimiter(), getAfterDelimiter(), getValueSeparator(), valueProvider.getValueProvider()).process(content);
    }

    public ValueProviderConfiguration getValueProvider() {
        return valueProvider;
    }

    public void setValueProvider(final ValueProviderConfiguration valueProvider) {
        this.valueProvider = valueProvider;
    }
}
