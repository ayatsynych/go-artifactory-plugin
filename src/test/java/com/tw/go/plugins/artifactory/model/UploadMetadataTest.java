package com.tw.go.plugins.artifactory.model;

import com.tw.go.plugins.artifactory.client.ArtifactoryUploadResponseBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfrog.build.client.ArtifactoryUploadResponse;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.path;
import static com.tw.go.plugins.artifactory.testutils.FilesystemUtils.read;
import static java.util.Arrays.asList;

public class UploadMetadataTest {
    @Test
    public void shouldReturnDataInJsonFormat() throws IOException {
        ArtifactoryUploadResponse response = new ArtifactoryUploadResponseBuilder().build();

        UploadMetadata uploadMetadata = new UploadMetadata(asList(response));

        String expectedContent = read(new File(path("src", "test", "resources", "uploadMetadata.json")));
        assertThat(uploadMetadata.content()).isEqualTo(expectedContent);
    }

    @Test
    public void shouldNotThrowAnErrorIfAFieldIsNull() throws IOException {
        ArtifactoryUploadResponse response = new ArtifactoryUploadResponseBuilder().withErrors(null).build();

        String uploadMetadata = new UploadMetadata(asList(response)).content();
        JsonNode jsonNode = new ObjectMapper().readTree(uploadMetadata);

        assertThat(jsonNode.findValue("errors")).isNull();
    }
}