package com.tw.go.plugins.artifactory.utils.filesystem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

public class PathMatchingVisitorTest {
    @Mock
    private Path tempPath;
    @Mock
    private PathMatcher matcher;
    @InjectMocks
    private PathMatchingVisitor visitor;

    @Before
    public void beforeEach() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnMatchedFiles() throws IOException {
        when(matcher.matches(tempPath)).thenReturn(true);

        FileVisitResult result = visitor.visitFile(tempPath, null);

        assertThat(result).isEqualTo(FileVisitResult.CONTINUE);
        assertThat(visitor.matched()).containsExactly(tempPath);
    }

    @Test
    public void shouldReturnUniqueFiles() throws IOException {
        when(matcher.matches(tempPath)).thenReturn(true);

        visitor.visitFile(tempPath, null);
        visitor.visitFile(tempPath, null);

        assertThat(visitor.matched()).containsExactly(tempPath);
    }

    @Test
    public void shouldReturnEmptyListWhenNoFilesMatched() throws IOException {
        when(matcher.matches(tempPath)).thenReturn(false);

        visitor.visitFile(tempPath, null);

        assertThat(visitor.matched()).isEmpty();
    }

    @Test(expected = IOException.class)
    public void shouldAbortOnVisitFailures() throws IOException {
        visitor.visitFileFailed(tempPath, new IOException("io error thrown as part of test - no worries!"));
    }
}