package com.xebialabs.maven.mustache;

import com.xebialabs.maven.mustache.transformer.MustacheToKey;


/**
 * From {{key:value}} to {{key}}
 *
 * @author Benoit Moussaud
 * @goal mustache-tokey
 * @phase compile
 * @threadSafe
 */
public class MustachifierToKeyMojo extends AbstractMustachifierMojo {

    protected String transform(final String content) {
        return new MustacheToKey(getBeforeDelimiter(), getAfterDelimiter(), getValueSeparator()).process(content);
    }


}
