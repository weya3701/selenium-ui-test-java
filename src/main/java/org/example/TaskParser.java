package org.example;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;

public class TaskParser {

    private TestJob testJob;

    public TaskParser(String yamlFile) {
        System.out.println(yamlFile);

        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);

        try {
            Yaml yaml = new Yaml(representer);

            StringBuffer sb=new StringBuffer();
            File file=new File("/Users/mirage/Desktop/yuantaDemo.yaml");
            System.out.println(file.exists());
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader isr=new InputStreamReader(inputStream);
            BufferedReader br=new BufferedReader(isr, 409600);
            try {
                while (true) {
                    String line = null;
                    try {
                        line = br.readLine();
                        if(line==null) break;
                        System.out.println(line);
                    } catch (IOException e) {
                        break;
                    }
                    if(line.indexOf("#")!=-1) continue;
                    sb.append(line).append("\n");
                }
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            finally {
                try { br.close(); }catch(Exception exx) {}
                try { isr.close(); }catch(Exception exx) {}
                try { inputStream.close(); }catch(Exception exx) {}
            }
            TestJob testJob = yaml.loadAs(sb.toString(), TestJob.class);
            this.testJob = testJob;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public TestJob getTestJob() {
        return this.testJob;
    }
}
