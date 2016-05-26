package com.tw.go.plugins.artifactory.task.config;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UriConfigTest {
    @Test
    public void shouldRemoveTrailingSlashesFromUri() {
        UriConfig config = new UriConfig("ending/in/slash/", false);
        assertThat(config.uri()).isEqualTo("ending/in/slash");
    }
}