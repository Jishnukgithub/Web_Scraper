package demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

// import org.checkerframework.checker.calledmethods.qual.EnsuresCalledMethods.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
// import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
// import java.util.*; 
import java.util.List;

import com.beust.ah.A;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Equivalence.Wrapper;

// import dev.failsafe.internal.util.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
public class TestCases {
    
     WebDriver driver;
    SeleniumWrapper wrapper;
    private static final String OUTPUT_DIR = "src/test/resources/output"; 
    ArrayList<HashMap<String, Object>> movieDataList;
    


@BeforeSuite(alwaysRun = true)
public void setup() {
    System.out.println("Constructor: Driver");
    WebDriverManager.chromedriver().timeout(30).setup();
   driver = new ChromeDriver();
   movieDataList = new ArrayList<>();
   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
   driver.manage().window().maximize();
   wrapper = new SeleniumWrapper(driver, Duration.ofSeconds(50));
   System.out.println("Sucessfully Created driver");
   
    }
    

@Test
public void testCase01() throws StreamWriteException, DatabindException, IOException{
    System.out.println("Start test case: Testcase1");
    wrapper.navigateToURL("https://www.scrapethissite.com/pages/");
     
     WebElement HokeyTeamClick = wrapper.wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//h3[@class='page-title'])[2]")));
     HokeyTeamClick.click();

     List<HashMap<String, String>> teamsData = new ArrayList<>();
     for (int i = 0; i < 4; i++) {
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='table']/tbody/tr"));
        for(WebElement row:rows){
            List<WebElement> columns = row.findElements(By.tagName("td"));
            if(columns.size() <4){
                System.out.println("Skipping row due to insufficient columns: " + row.getText());
                continue;

            }
           
            String winPercentageStrng = columns.get(6).getText().replace("%", "").trim();
            double winPercentage;
            
            try{
           winPercentage = Double.parseDouble(winPercentageStrng)/100.0;
            }catch (NumberFormatException e){
                System.out.println("Skipping row due to number format exception: " + row.getText());
                    continue;
            }
           
            if (winPercentage < 0.40) {
                HashMap<String, String> teamData = new HashMap<>();
                teamData.put("Team Name", columns.get(0).getText());
                teamData.put("Year", columns.get(1).getText());
                teamData.put("Win %", String.valueOf(winPercentage));
                teamsData.add(teamData);

            }
        }
        try{
        WebElement Next = wrapper.wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-label='Next']")));
        Next.click();
        }catch(Exception e){
            System.out.println("Next button is not clickable: " + e.getMessage());
            break;
        }
  }
  File outputDir = new File(OUTPUT_DIR);
  if (!outputDir.exists()) {
      outputDir.mkdirs();
  }
  ObjectMapper mapper = new ObjectMapper();
  mapper.writeValue(new File(outputDir, "hockey-team-data.json"), teamsData);
  System.out.println("End test case: Testcase1");
    }
@Test
    public void testCase02() throws StreamWriteException, DatabindException, IOException{
        System.out.println("Start test case: Testcase2");
        wrapper.navigateToURL("https://www.scrapethissite.com/pages/");
        WebElement oscarWinnerClick = wrapper.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//h3[@class='page-title'])[3]")));
        oscarWinnerClick.click();
        
        List<WebElement> years = driver.findElements(By.xpath("//a[@class='year-link']"));
        for(WebElement yearElement:years){
            String year = yearElement.getText();
            System.out.println(year);
            yearElement.click();
        
        List<WebElement> movies = driver.findElements(By.xpath("//table[@class='table']/tbody/tr/td"));
        for (int i = 0; i < Math.min(5, movies.size()); i++) {
            WebElement movie = movies.get(i);
            String title = movies.get(1).getText();
            String nomination = movies.get(2).getText();
            String awards = movies.get(3).getText();
            boolean isWinner = i == 0;
            
             HashMap<String, Object> movieData = new HashMap<>();
                movieData.put("EpochTimeOfScrape", Instant.now().getEpochSecond());
               
                movieData.put("Title", title);
                movieData.put("Nomination", nomination);
                movieData.put("Awards", awards);
                movieData.put("isWinner", isWinner);
                movieDataList.add(movieData);
            }
            driver.navigate().back();
            
        }
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outputDir, "oscar-winner-data.json"), movieDataList);
        System.out.println("End test case: Testcase2");
        
}



    }



