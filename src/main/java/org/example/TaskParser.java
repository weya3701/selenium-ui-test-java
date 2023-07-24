package org.example;
import java.io.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public class TaskParser {
  private TestJob testJob;

  public String fileParser(String filePath) {
    String fileContent = null;
    try {
      FileReader fileReader = new FileReader(filePath);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }

      bufferedReader.close();

      fileContent = stringBuilder.toString();

    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return fileContent;
  }

  public TaskParser(String yamlFile) {
    Representer representer = new Representer(new DumperOptions());
    representer.getPropertyUtils().setSkipMissingProperties(true);

    try {
      Yaml yaml = new Yaml(representer);

      StringBuffer sb = new StringBuffer();
      String yamlOutput = fileParser(yamlFile);

      TestJob testJob = yaml.loadAs(yamlOutput, TestJob.class);
      this.testJob = testJob;
    } catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }

  public TestJob getTestJob() {
    return this.testJob;
  }
}
