package com.xebialabs.maven.mustache;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingleFileTest {

    @Test
    public void should_contains_default_value() {
        SingleFile file = new SingleFile();
        Assert.assertEquals(SingleFile.UTF_8, file.getModelEncoding());
        Assert.assertEquals(false, file.isFilterOnlyMustacheProperties());
    }

}