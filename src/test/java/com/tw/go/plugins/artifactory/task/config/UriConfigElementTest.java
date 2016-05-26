package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.uriConfig;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UriConfigElementTest {
    @Test
    public void shouldInvalidateAnEmptyUri() {
        Optional<ValidationError> error = uriConfig.validate(config("", true));
        assertThat(error).hasValue(new ValidationError("URI", "Uri is mandatory"));
    }

    @Test
    public void shouldInvalidateUriStartingWithSlash() {
        Optional<ValidationError> error = uriConfig.validate(config("/a/b", true));
        assertThat(error).hasValue(new ValidationError("URI", "Relative uri should not start with a '/'"));
    }

    @Test
    public void shouldReturnUriWithTrailingSlashesRemoved() {
        TaskConfig taskConfig = config("google.com", true);
        assertThat(uriConfig.from(taskConfig).uri()).isEqualTo("google.com");
    }

    @Test
    public void shouldReturnWhetherUriDenotesFolder() {
        TaskConfig taskConfig = config("/path/to/artifact.ext", false);
        assertThat(uriConfig.from(taskConfig).isFolder()).isFalse();
    }

    private TaskConfig config(String value, boolean isFolder) {
        TaskConfig mock = mock(TaskConfig.class);

        when(mock.getValue("URI")).thenReturn(value);
        when(mock.getValue("uriIsFolder")).thenReturn(String.valueOf(isFolder));

        return mock;
    }
}