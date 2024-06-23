package Scheduler;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Scheduler {
    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Usage Instructions: Scheduler <input(FilePath)> <processorsNumber(Integer)>");
            return;
        }

        String inputFilePath = args[0];
        Integer processorsNumber = Integer.parseInt(args[1]);

        if (processorsNumber < 1) {
            System.out.println("Number of processors must be greater than 0.");
            return;
        }

        System.out.println("Input File Path: ".concat(inputFilePath));
        System.out.println("Number of Processors: ".concat(processorsNumber.toString()));

        Map tasksMap = getFileTasks(inputFilePath);

        if (tasksMap.isEmpty()) {
            System.out.println("No tasks found in the file.");
            return;
        }

        // Convert map to list of entries and sort by value to suport SJF
        List<Map.Entry<String, Integer>> taskList = new ArrayList<>(tasksMap.entrySet());
        taskList.sort(Map.Entry.comparingByValue());

        System.out.println("Sorted Task List: ".concat(taskList.toString()));

        // create a number o processors on arguments
        List<Processor> processorList = new ArrayList<>();

        for (int i = 0; i < processorsNumber; i++) {
            processorList.add(new Processor());
        }

        String processedTasks = processSJFPolicy(processorList, taskList);

        if (processedTasks.isEmpty()) {
            System.out.println("No tasks processed.");
        }

        System.out.println("\nProcessed Tasks: \n");
        System.out.println(processedTasks);

        //create a file with processedTasks content
        generateOutputFile(processedTasks);

    }

    private static Map getFileTasks (String filePath ) throws Exception {
        File inputFile = new File(filePath);
        Map taskMap = new HashMap<>();

        if (!inputFile.exists()) {
            throw new Exception("File not found.");
        }

        Scanner inputFileReader = new Scanner(inputFile);

        while (inputFileReader.hasNextLine()) {
                String data = inputFileReader.nextLine();
                taskMap.put(data.split(" ")[0], Integer.parseInt(data.split(" ")[1]));
        }

        System.out.println("Tasks Map: ".concat(taskMap.toString()));
        return taskMap;
    }

    private static String processSJFPolicy (List<Processor> processorList, List<Map.Entry<String, Integer>> taskList) {
        String processedTasks = "";
        int actualProcessor = 0;
        int processorsSize = processorList.size();

        if (processorList.isEmpty() || taskList.isEmpty()) {
            System.out.println("No processors or tasks to process.");
            return processedTasks;
        }

        for (Map.Entry<String, Integer> task : taskList) {
            Processor processor = processorList.get(actualProcessor);
            processor.addTask(task.getKey(), task.getValue());

            if (actualProcessor < processorsSize - 1) {
                actualProcessor++;
            } else {
                actualProcessor = 0;
            }
        }

        StringBuilder processorTasks = new StringBuilder();

        for (Processor processor : processorList) {
            processorTasks.append("Processador_".concat(processorList.indexOf(processor) + 1 + "\n"));
            processorTasks.append(processor.getProcessorTasks().concat("\n"));
        }

        processedTasks = processorTasks.toString();
        return processedTasks;
    }

    private static void generateOutputFile(String content) {

        String path = Scheduler.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File jarFile = new File(path);
        path = jarFile.getParent();

        System.out.println("Path to main class: " + path);

        File outputFile = new File(path + "/output.txt");
        // if file exists append a number on output file name
        if (outputFile.exists()) {
            int i = 1;
            while (outputFile.exists()) {
                outputFile = new File(path + "/output" + i + ".txt");
                i++;
            }
        }

        try {
            outputFile.createNewFile();
        } catch (Exception e) {
            System.out.println("Error creating output file.");
        }

        // write content to output file
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(content);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Error writing to output file.");
        }
    }
}
