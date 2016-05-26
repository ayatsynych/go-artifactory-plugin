package com.tw.go.plugins.artifactory.task.config;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import org.junit.Test;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.buildProperties;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuildPropertiesConfigElementTest {
    @Test
    public void shouldValidateKeyValuePairsSeparatedByNewline() {
        TaskConfig taskConfig = propertiesConfig("a=b");
        assertThat(buildProperties.validate(taskConfig)).isAbsent();

        taskConfig = propertiesConfig("a=b\nc=d");
        assertThat(buildProperties.validate(taskConfig)).isAbsent();

        taskConfig = propertiesConfig("a=b\n\n\nc=d\n\n");
        assertThat(buildProperties.validate(taskConfig)).isAbsent();
    }

    @Test
    public void shouldNotValidateIfNotCorrectlyFormatted() {
        TaskConfig taskConfig = propertiesConfig("a=b=c");

        assertThat(buildProperties.validate(taskConfig))
                .hasValue(new ValidationError("Properties", "Invalid properties format"));
    }

    @Test
    public void shouldReturnPropertiesAsMap() {
        TaskConfig taskConfig = propertiesConfig("a=b\n\nc=d \n\n\n");
        Map<String, String> propertiesMap = buildProperties.from(taskConfig);

        assertThat(propertiesMap).containsEntry("a", "b");
        assertThat(propertiesMap).containsEntry("c", "d");
    }

    private TaskConfig propertiesConfig(String value) {
        TaskConfig mock = mock(TaskConfig.class);
        when(mock.getValue("Properties")).thenReturn(value);
        return mock;
    }
}
