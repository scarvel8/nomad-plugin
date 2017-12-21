package org.jenkinsci.plugins.nomad;

import hudson.model.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Yegor Andreenko
 */
public class NomadApiTest {

    private NomadApi nomadApi = new NomadApi("http://localhost");
    private List<NomadConstraintTemplate> constraintTest = new ArrayList<NomadConstraintTemplate>();
    private NomadSlaveTemplate slaveTemplate = new NomadSlaveTemplate("namespace-test", 
        "09652e67-e6e9-8dd1-eec9-999e49ac3e5f",
            "300", "256", "100",
            null, constraintTest, "remoteFs", "3","1", Node.Mode.NORMAL,
            "ams", "0", "image", "dc01", "", "", false, "bridge",""
    );

    private NomadCloud nomadCloud = new NomadCloud(
            "nomad",
            "nomadUrl",
            "jenkinsUrl",
            "slaveUrl",
            Collections.singletonList(slaveTemplate));

    @Before
    public void setup() {
        slaveTemplate.setCloud(nomadCloud);
    }

    @Test
    public void testStartSlave() {
        String job = nomadApi.buildSlaveJob("slave-1","secret", slaveTemplate);

        assertTrue(job.contains("\"Region\":\"ams\""));
        assertTrue(job.contains("\"CPU\":300"));
        assertTrue(job.contains("\"MemoryMB\":256"));
        assertTrue(job.contains("\"SizeMB\":100"));
        assertTrue(job.contains("\"GetterSource\":\"slaveUrl\""));
        assertTrue(job.contains("\"privileged\":false"));
        assertTrue(job.contains("\"network_mode\":\"bridge\""));
    }

}
