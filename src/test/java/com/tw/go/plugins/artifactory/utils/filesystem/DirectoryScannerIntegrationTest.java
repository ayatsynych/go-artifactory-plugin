package com.tw.go.plugins.artifactory.utils.filesystem;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;

public class DirectoryScannerIntegrationTest {
    private Path rootDir;
    private Path subDir;
    private DirectoryScanner directoryScanner;

    @Before
    public void beforeEach() throws IOException {
        rootDir = Files.createTempDirectory(null);
        subDir = Files.createTempDirectory(rootDir, null);

        directoryScanner = new DirectoryScanner(rootDir.toString());
    }

    @Test
    public void shouldReturnFileGivenItsName() throws IOException {
        Path testFile = Files.createTempFile(rootDir, "Test", ".java");
        String testFileName = testFile.getFileName().toString();

        Collection<File> files = directoryScanner.scan(testFileName);

        assertThat(files).containsExactly(testFile.toFile());
    }

    @Test
    public void shouldReturnMultipleFiles() throws IOException {
        Path temp1 = Files.createTempFile(rootDir, null, ".java");
        Path temp2 = Files.createTempFile(rootDir, null, ".java");
        Path temp3 = Files.createTempFile(subDir, null, ".class");

        Collection<File> files = directoryScanner.scan("**");

        assertThat(files).containsExactly(temp1.toFile(), temp2.toFile(), temp3.toFile());
    }

    @Test
    public void shouldReturnFilesMatchingGlob() throws IOException {
        Files.createTempFile(rootDir, null, ".java");
        Files.createTempFile(rootDir, null, ".java");
        Path temp = Files.createTempFile(subDir, null, ".class");

        Collection<File> files = directoryScanner.scan("**/*.class");

        assertThat(files).containsExactly(temp.toFile());
    }

    @After
    public void afterEach() throws IOException {
        FileUtils.deleteDirectory(rootDir.toFile());
    }
}