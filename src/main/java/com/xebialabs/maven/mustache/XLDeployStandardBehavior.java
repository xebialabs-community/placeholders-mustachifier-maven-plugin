package com.xebialabs.maven.mustache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xebialabs.restito.semantics.Stub;
import com.xebialabs.restito.support.behavior.Behavior;

import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Condition.get;

public class XLDeployStandardBehavior implements Behavior {
    @Override
    public List<Stub> getStubs() {
        return new ArrayList<Stub>(Arrays.asList(
                new Stub(get("/deployit/deployment/dictionary"),
                        resourceContent("restito/xldeploy/deployment/dictionary/dev.xml"))));

    }
}
