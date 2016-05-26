package com.google;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;

public class GuavaTests {
    @Test
    public void shouldSplitAStringBasedOnDelimiterAndNumberOfTimes() {
        String testString = "a/b/c.txt";
        List<String> splits = Splitter.on("/").omitEmptyStrings().limit(2).splitToList(testString);

        assertThat(splits).containsExactlyElementsIn(asList("a", "b/c.txt"));
    }

    @Test
    public void shouldStringifyIterables() {
        List<String> strings = Arrays.asList("a", "b", "c");
        assertThat(Iterables.toString(strings)).isEqualTo("[a, b, c]");
    }

    @Test
    public void shouldPerformSplitsMultipleTimes() {
        String toBeSplit = "a=b\n\nc=d\n\n\n";
        Map<String, String> split = Splitter.on("\n").omitEmptyStrings().withKeyValueSeparator("=").split(toBeSplit);

        assertThat(split.size()).isEqualTo(2);
        assertThat(split).containsEntry("a", "b");
        assertThat(split).containsEntry("c", "d");
    }
}

