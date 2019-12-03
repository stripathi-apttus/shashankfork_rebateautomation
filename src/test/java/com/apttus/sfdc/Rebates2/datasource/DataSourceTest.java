package com.apttus.sfdc.Rebates2.datasource;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apttus.customException.CustomException;
import com.apttus.helpers.Efficacies;
import com.apttus.helpers.JavaHelpers;
import com.apttus.selenium.WebDriverUtils;
import com.apttus.sfdc.Rebates2.common.LoginPage;
import com.apttus.sfdc.Rebates2.common.StartUpPage;
import com.apttus.sfdc.Rebates2.library.DataSourcePage;
import com.apttus.sfdc.Rebates2.library.Rebatesinit;
import com.apttus.sfdc.Rebates2.lightning.HomePage;
import com.apttus.sfdc.rudiments.utils.GeneralHelper;

public class DataSourceTest {
	
	WebDriver driver;
	public  Rebatesinit rebatesinit;
	LoginPage loginPage;
	public Map<String, String> testData;
	public DataSourcePage datasourcePage;
	public HomePage homepage;
	Properties configProperty;
	StartUpPage startUpPage;
	
	@BeforeClass(alwaysRun = true)
	@Parameters({ "runParallel", "environment", "browser", "hubURL" })
	public void setUp(String runParallel, String environment, String browser, String hubURL) throws Exception {
		
		WebDriverUtils utils = new WebDriverUtils();
		utils.initializeDriver(browser, hubURL);
		driver = utils.getDriver();

		startUpPage = new StartUpPage(driver);
		configProperty = GeneralHelper.loadPropertyFile(environment);
	
		loginPage = startUpPage.navigateToLoginPage(configProperty.getProperty("sfloginURL"));
		loginPage.waitForLoginPageLoad().loginToApp(configProperty.getProperty("username"),configProperty.getProperty("password"));
		rebatesinit= new Rebatesinit(driver);
		homepage = rebatesinit.landOnHomepage();
			
	}
		
	@Test(description = "Verify the Data Source Save Feature",groups = {"Regression"})
	
	public void CreateNewDataSource() throws Exception {
		try {
			datasourcePage=homepage.navigateToDataSource();
			testData = new Efficacies().readJsonFile("datasource.json");
			long ln = JavaHelpers.generateRandomNumber();
			datasourcePage.CreateSaveNewDataSource(testData.get("DataSourceName")+ln, testData.get("TransMetaData"), testData.get("CalculationDate"),
					                               testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileSuffix"),testData.get("FileExtenstion1"), 
					                               testData.get("FileExtenstion2"),testData.get("Delimiter"));
			assertEquals(datasourcePage.success, datasourcePage.successresponse.getText());
			datasourcePage.CloseToastMessage();
			datasourcePage=homepage.navigateToDataSource();
			datasourcePage.DataSdourceFilter(testData.get("ColumnName"),testData.get("ColumnOperator"),testData.get("DataSourceName")+ln);
			datasourcePage.DataSourceRefresh();
			datasourcePage.DeleteSFDCFilter();
			datasourcePage.RemoveFilter();
			    
		} catch (Exception e) {
			throw new CustomException(e,driver);
		}
	}
		
	@Test(description = "Verify the Data Source Validation",groups = {"Regression"})
	public void DuplicateDataSource() throws Exception {
		try {
			datasourcePage=homepage.navigateToDataSource();
			testData = new Efficacies().readJsonFile("datasource.json");
			long ln = JavaHelpers.generateRandomNumber();
			datasourcePage.DataSourceRefresh();
			datasourcePage.DuplicateSaveNewDataSource(testData.get("DupDataSourceName")+ln, testData.get("TransMetaData"), testData.get("CalculationDate"),
                           testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileSuffix"),testData.get("FileExtenstion1"), 
                           testData.get("FileExtenstion2"),testData.get("Delimiter"));
			
		    String DuplicateDataSource=testData.get("DupDataSourceName")+ln;
		    datasourcePage=homepage.navigateToDataSource();	
		    datasourcePage.DataSourceRefresh();
			datasourcePage.Verifyduplicate(DuplicateDataSource, testData.get("TransMetaData"), testData.get("CalculationDate"),
                           testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileSuffix"),testData.get("FileExtenstion1"), 
                           testData.get("FileExtenstion2"),testData.get("Delimiter"));
			assertEquals(datasourcePage.Duplicate, datasourcePage.duplicateRecord);
		    datasourcePage.CancelDatasource();
			datasourcePage.DuplicateSdourceFilter("Name","contains",DuplicateDataSource);
			datasourcePage.CloseToastMessage();
			datasourcePage.DeleteSFDCFilter();
			datasourcePage.deleteFilter();
			
		} catch (Exception e) {
			throw new CustomException(e,driver);
		}
	}
	@Test(description = "Verify mandatory & Field Validation-Data Source File Ingestion attribute",groups = {"Regression"})
	public void VerifyValidationMsz() throws Exception {
		try {
		datasourcePage=homepage.navigateToDataSource();
		testData = new Efficacies().readJsonFile("datasource.json");
		datasourcePage.DataSourceRefresh();
		datasourcePage.VeriyValidationDelimiter(testData.get("DataSourceName"), testData.get("TransMetaData"), testData.get("CalculationDate"),
                                               testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileSuffix"),testData.get("FileExtenstion1"));
	    assertEquals(datasourcePage.ResponseDelimiter, datasourcePage.getDelimiterResponse);
		
		datasourcePage.VeriyValidationFileExtension("ValidationAutomation", "Order Line Item", "Ready for Activation Date","Base Product","Bill To","_AutoSuffix","commas");
		assertEquals(datasourcePage.ResponseFileExt, datasourcePage.getFileExtensionResponse);
		
		datasourcePage.DataSourceRefresh();
		datasourcePage.VeriyValidationSuffix("ValidationAutomation", "Order Line Item", "Ready for Activation Date","Base Product","Bill To");
		assertEquals(datasourcePage.ResponseSuffix, datasourcePage.getSuffixResponse);
		
		datasourcePage=homepage.navigateToDataSource();
		datasourcePage.DataSourceRefresh();
		datasourcePage.VerifyValidation_DataSourceName();
		assertEquals(datasourcePage.ResponseDataSrc, datasourcePage.getdatasrcResponse);
		
		datasourcePage=homepage.navigateToDataSource();
		datasourcePage.VerifyValidationTransactionMetaData("ValidationAutomation");
		assertEquals(datasourcePage.ResponseMetaData, datasourcePage.getMetadataResponse);
		
		datasourcePage.VerifyValidation_CalculationDate("ValidationAutomation", "Order Line Item");
		assertEquals(datasourcePage.ResponseCalcDate, datasourcePage.getCaldateResponse);
		datasourcePage.DataSourceRefresh();
		datasourcePage.VeriyValidation_ProgramAccount("ValidationAutomation","Order Line Item","Ready for Activation Date");
		assertEquals(datasourcePage.ResponsePrgmAccount, datasourcePage.getProgram);
		
		datasourcePage.VeriyValidation_Product(testData.get("ProgramAccount"));
		assertEquals(datasourcePage.ResponseProduct, datasourcePage.ProductResponse.getText());
			
	}catch (Exception e) {
		throw new CustomException(e,driver);
	}}	
	
	 @Test(description = "Verify the Data Source with multiplecombination",groups = {"Regression"})
     public void VerifyDelimiterSuffix() throws Exception {
		 try {
    	datasourcePage=homepage.navigateToDataSource();
		testData = new Efficacies().readJsonFile("datasource.json");
		long ln = JavaHelpers.generateRandomNumber();
		datasourcePage.DataSourceRefresh();
		datasourcePage.VerifydiffrentsetofDelimterSuffixB(testData.get("DataSourceName")+"B"+ln, testData.get("TransMetaData"),testData.get("CalculationDate"),testData.get("SuffixB"),
				       testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileExtenstion1"), 
				       testData.get("FileExtenstion2"),testData.get("DelimiterB"));
		datasourcePage=homepage.navigateToDataSource();
		assertEquals(datasourcePage.success, datasourcePage.successresponse.getText());
		datasourcePage.CloseToastMessage();
		datasourcePage.DataSourceRefresh();
		datasourcePage.VerifydiffrentsetofDelimterSuffixC(testData.get("DataSourceName")+"C"+ln, testData.get("TransMetaData"),testData.get("CalculationDate"),testData.get("SuffixC"),
				       testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileExtenstion1"), 
			           testData.get("FileExtenstion2"),testData.get("DelimiterC"));
		assertEquals(datasourcePage.success, datasourcePage.successresponse.getText());
		datasourcePage.CloseToastMessage();
		datasourcePage=homepage.navigateToDataSource();	  
		datasourcePage.DataSourceRefresh();
		datasourcePage.VerifydiffrentsetofDelimterSuffixD(testData.get("DataSourceName")+"D"+ln, testData.get("TransMetaData"),testData.get("CalculationDate"),testData.get("SuffixD"),
				       testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileExtenstion1"), 
			           testData.get("FileExtenstion2"),testData.get("DelimiterD"));
		assertEquals(datasourcePage.success, datasourcePage.successresponse.getText());
		datasourcePage.CloseToastMessage();
		datasourcePage=homepage.navigateToDataSource();
		datasourcePage.DataSourceRefresh();
		datasourcePage.VerifydiffrentsetofDelimterSuffixE(testData.get("DataSourceName")+"E"+ln, testData.get("TransMetaData"),testData.get("CalculationDate"),testData.get("SuffixE"),
				       testData.get("Product"),testData.get("ProgramAccount"),testData.get("FileExtenstion1"), 
			           testData.get("FileExtenstion2"),testData.get("DelimiterE"));
		assertEquals(datasourcePage.success, datasourcePage.successresponse.getText());
		datasourcePage.CloseToastMessage();
    	
    }catch (Exception e) {
		throw new CustomException(e,driver);
	}}
    
	@Test(description = "Verify Save- Search filter to filter and view related Rebate records in List view",groups = {"Regression"})
	public void FilterDataSource() throws Exception {
		try {
			datasourcePage=homepage.navigateToDataSource();
			testData = new Efficacies().readJsonFile("datasource.json");
			datasourcePage.DataSourceRefresh();
			datasourcePage.DataSdourceFilter(testData.get("ColumnName"),testData.get("ColumnOperator"),testData.get("FilterValue"));
			datasourcePage.deleteFilter();
			assertEquals(datasourcePage.nwResponseFilter, datasourcePage.ResponseFilter);
		   
		} catch (Exception e) {
			throw new CustomException(e,driver);
		}
	}
	
	@AfterMethod(alwaysRun = true)
	public void cleanUp() throws Exception {
		
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		startUpPage.killDriver();
	}
}




