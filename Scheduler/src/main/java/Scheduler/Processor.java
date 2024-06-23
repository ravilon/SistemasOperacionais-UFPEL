package Scheduler;

import java.util.ArrayList;
import java.util.List;

public class Processor {
    Integer actualClock;
    List<String> task;
    List<Integer> taskSize;
    List<Integer> taskClock;


    Processor() {
        actualClock = 0;
        task = new ArrayList<>();
        taskClock = new ArrayList<>();
        taskSize = new ArrayList<>();
    }

    public void addTask(String taskName, Integer taskDuration) {
        task.add(taskName);
        taskSize.add(taskDuration);
        taskClock.add(actualClock);
        actualClock += taskDuration;
    }

    public String getProcessorTasks() {
        StringBuilder processorTasks = new StringBuilder();
        for (int i = 0; i < task.size(); i++) {
            processorTasks.append(task.get(i)).append(";");
            processorTasks.append(taskSize.get(i)).append(";");
            processorTasks.append(taskClock.get(i)).append("\n");

        }
        return processorTasks.toString();
    }
}
