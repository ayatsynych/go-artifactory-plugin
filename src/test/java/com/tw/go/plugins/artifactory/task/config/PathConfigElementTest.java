package com.tw.go.plugins.artifactory.task.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.truth.Truth.assertThat;
import static com.tw.go.plugins.artifactory.task.config.ConfigElement.path;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathConfigElementTest {
    public static final Iterable<Path> ROOT_DIRECTORIES = FileSystems.getDefault().getRootDirectories();

    @Test
    public void shouldBeARelativePath() {
        Optional<ValidationError> error = path.validate(pathConfig("a/b"));
        assertThat(error).isAbsent();

        error = path.validate(pathConfig("../a/b"));
        assertThat(error).isAbsent();
    }

    @Test
    public void shouldNotValidateAnAbsolutePath() {
        String root = getLast(ROOT_DIRECTORIES).toString();
        Optional<ValidationError> error = path.validate(pathConfig(root));
        assertThat(error).hasValue(new ValidationError("Path", "Path should be relative to workspace"));
    }

    @Test
    public void shouldNotValidateEmptyPath() {
        Optional<ValidationError> error = path.validate(pathConfig(""));
        assertThat(error).hasValue(new ValidationError("Path", "Path is mandatory"));
    }

    @Test
    public void shouldReturnPathFromTaskConfig() {
        TaskConfig taskConfig = pathConfig("a/b");
        assertThat(path.from(taskConfig)).isEqualTo("a/b");
    }

    private TaskConfig pathConfig(String value) {
        TaskConfig mock = mock(TaskConfig.class);
        when(mock.getValue("Path")).thenReturn(value);
        return mock;
    }
}
