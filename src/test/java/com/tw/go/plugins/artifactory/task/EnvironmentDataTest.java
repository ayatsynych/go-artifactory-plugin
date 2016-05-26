package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.ARTIFACTORY_URL;
import static com.tw.go.plugins.artifactory.task.EnvironmentData.PIPELINE_VALUESTREAM_URL;
import static com.tw.go.plugins.artifactory.testutils.MapBuilder.map;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnvironmentDataTest {
    @Test
    public void shouldReturnEnvironmentVariableValue() {
        EnvironmentVariables environmentVariables = asEnvVars(map("ARTIFACTORY_URL", "http://localhost"));

        assertThat(ARTIFACTORY_URL.from(environmentVariables)).isEqualTo("http://localhost");
    }

    @Test(expected = MissingEnvironmentDataException.class)
    public void shouldThrowExceptionIfEnvironmentVariableIsMissing() {
        EnvironmentVariables environmentVariables = asEnvVars(new HashMap<String, String>());
        ARTIFACTORY_URL.from(environmentVariables);
    }

    @Test
    public void shouldReturnValueStreamUrlForThePipeline() {
        EnvironmentVariables environmentVariables = asEnvVars(
                map("GO_SERVER_URL", "http://go.server:8153/go/")
                        .and("GO_PIPELINE_NAME", "name")
                        .and("GO_PIPELINE_COUNTER", "23")
        );

        assertThat(PIPELINE_VALUESTREAM_URL.from(environmentVariables))
                .isEqualTo("http://go.server:8153/go/pipelines/value_stream_map/name/23");

    }

    private EnvironmentVariables asEnvVars(Map<String, String> envVars) {
        EnvironmentVariables environmentVariables = mock(EnvironmentVariables.class);
        when(environmentVariables.asMap()).thenReturn(envVars);
        return environmentVariables;
    }
}