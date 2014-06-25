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

    protected String transform(final String content) {
        return new MustacheToValue(getBeforeDelimiter(), getAfterDelimiter(), getValueSeparator()).process(content);
    }


}
