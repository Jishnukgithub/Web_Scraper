package demo;

import java.io.File;

import org.testng.Assert;

public class JsonFileCreator {

    public void testJsonFile() {
    File file = new File("output/hockey-team-data.json");
    File file1 = new File("output/oscar-winner-data.json");
        Assert.assertTrue(file.exists(), "JSON file does not exist");
        Assert.assertTrue(file.length() > 0, "JSON file is empty");
    }
}
    

