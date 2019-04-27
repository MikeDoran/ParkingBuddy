package ProjectTwoClient;

import java.io.*;
import java.nio.file.Files;

public class Test {
    public static void main(String [] args) throws IOException {
        String inputPath = "D:\\Cygwin\\home\\Mike\\emacsProjects\\CSC445\\ProjectTwoClient\\src\\main\\resources\\collin.png";
        String outputPath = "D:\\Cygwin\\home\\Mike\\emacsProjects\\CSC445\\ProjectTwoClient\\src\\main\\resources\\collin2.png";

        File input = new File(inputPath);
        File output = new File(outputPath);

        byte [] inputbytes = Files.readAllBytes(input.toPath());
        Files.write(output.toPath(),inputbytes);
    }
}
