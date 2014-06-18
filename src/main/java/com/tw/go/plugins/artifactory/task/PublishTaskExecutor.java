package com.tw.go.plugins.artifactory.task;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.tw.go.plugins.artifactory.task.config.PublishTaskConfig;

import static com.tw.go.plugins.artifactory.task.config.PublishTaskConfig.PATH;
import static com.tw.go.plugins.artifactory.task.config.PublishTaskConfig.URL;

public class PublishTaskExecutor implements TaskExecutor {
    @Override
    public ExecutionResult execute(TaskConfig config, TaskExecutionContext context) {
        context.console().printLine("Artifactory URL : " + config.getValue(URL));
        context.console().printLine("Path : " + config.getValue(PATH));

        return ExecutionResult.success("Finished running Artifactory plugin!");
    }
}
