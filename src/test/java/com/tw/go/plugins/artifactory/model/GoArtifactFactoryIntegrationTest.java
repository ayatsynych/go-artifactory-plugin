package com.tw.go.plugins.artifactory.model;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.tw.go.plugins.artifactory.task.config.TaskConfigBuilder;
import com.tw.go.plugins.artifactory.task.executor.TaskExecutionContextBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.path;
import static org.apache.commons.lang.StringUtils.join;

public class GoArtifactFactoryIntegrationTest {
    private static GoArtifactFactory factory;
    private static TaskExecutionContext context;
    private Map<String, String> properties = ImmutableMap.<String, String>builder().put("name", "value").build();

    @BeforeClass
    public static void beforeAll() throws Exception {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("GO_REVISION", "123");
        context = new TaskExecutionContextBuilder()
                .withWorkingDir(System.getProperty("user.dir"))
                .withEnvVars(envVars)
                .build();
        factory = new GoArtifactFactory();
    }

    @Test
    public void shouldCreateGoArtifacts() {
        TaskConfig config = new TaskConfigBuilder()
                .path(path("src", "test", "resources", "artifact.txt"))
                .uri("repo/path/to/output.ext")
                .property("name", "value")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact expectedArtifact = goArtifact("src/test/resources/artifact.txt", "repo/path/to/output.ext", properties);

        assertThat(artifacts).containsExactly(expectedArtifact);
    }

    @Test
    public void shouldCreateArtifactsWithUniqueRemotePathsIfUriIsAFolder() {
        TaskConfig config = new TaskConfigBuilder()
                .path(asPath("src", "test", "resources", "**{artifact.txt,test.html}"))
                .uri("repo/path")
                .uriIsFolder(true)
                .property("name", "value")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact artifactTxt = goArtifact("src/test/resources/artifact.txt", "repo/path/artifact.txt", properties);
        GoArtifact testHtml = goArtifact("src/test/resources/view/test.html", "repo/path/test.html", properties);

        assertThat(artifacts).containsExactly(artifactTxt, testHtml);
    }

    @Test
    public void shouldCreateArtifactsWithSameRemotePathIfUriIsNotAFolder() {
        TaskConfig config = new TaskConfigBuilder()
                .path(asPath("src", "test", "resources", "**{artifact.txt,test.html}"))
                .uri("repo/path")
                .uriIsFolder(false)
                .property("name", "value")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact artifactTxt = goArtifact("src/test/resources/artifact.txt", "repo/path", properties);
        GoArtifact testHtml = goArtifact("src/test/resources/view/test.html", "repo/path", properties);

        assertThat(artifacts).containsExactly(artifactTxt, testHtml);
    }

    @Test
    public void shouldSubstituteEnvironmentVariablesIntoUri() {
        TaskConfig config = new TaskConfigBuilder()
                .path(asPath("src", "test", "resources", "**{artifact.txt,test.html}"))
                .uri("repo/path/${GO_REVISION}")
                .uriIsFolder(false)
                .property("name", "value")
                .build();

        Collection<GoArtifact> artifacts = factory.createArtifacts(config, context);

        GoArtifact artifactTxt = goArtifact("src/test/resources/artifact.txt", "repo/path/123", properties);
        GoArtifact testHtml = goArtifact("src/test/resources/view/test.html", "repo/path/123", properties);

        assertThat(artifacts).containsExactly(artifactTxt, testHtml);
    }

    private GoArtifact goArtifact(String relativePath, String uri, Map<String, String> properties) {
        String[] segments = relativePath.split("/");

        GoArtifact artifact = new GoArtifact(path(System.getProperty("user.dir"), segments), uri);
        artifact.properties(properties);
        return artifact;
    }

    private String asPath(String... segments) {
        return join(segments, File.separatorChar);
    }
}