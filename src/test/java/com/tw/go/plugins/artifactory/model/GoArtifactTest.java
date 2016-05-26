package com.tw.go.plugins.artifactory.model;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class GoArtifactTest {
    private GoArtifact artifact = new GoArtifact("/full/path/to/artifact", "repo/path/to/artifact.ext");

    @Test
    public void shouldSplitUriIntoRepoAndArtifactPath() {
        assertThat(artifact.repository()).isEqualTo("repo");
        assertThat(artifact.remotePath()).isEqualTo("path/to/artifact.ext");
    }

    @Test
    public void shouldReturnTheLocalPath() {
        assertThat(artifact.localPath()).isEqualTo("/full/path/to/artifact");
    }

    @Test
    public void shouldReturnTheRemoteArtifactName() {
        assertThat(artifact.remoteName()).isEqualTo("artifact.ext");
    }
}
