package com.apttus.sfdc.rebates.lightning.ui.admin;

import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.apttus.helpers.Efficacies;
import com.apttus.selenium.WebDriverUtils;
import com.apttus.sfdc.rebates.lightning.api.library.CIMAdmin;
import com.apttus.sfdc.rebates.lightning.api.validator.ResponseValidatorBase;
import com.apttus.sfdc.rebates.lightning.common.GenericPage;
import com.apttus.sfdc.rebates.lightning.generic.utils.RebatesConstants;
import com.apttus.sfdc.rebates.lightning.generic.utils.SFDCHelper;
import com.apttus.sfdc.rebates.lightning.main.UnifiedFramework;
import com.apttus.sfdc.rebates.lightning.ui.library.HomePage;
import com.apttus.sfdc.rebates.lightning.ui.library.LoginPage;
import com.apttus.sfdc.rebates.lightning.ui.library.TemplatePage;
import com.apttus.sfdc.rudiments.utils.SFDCRestUtils;
import com.jayway.restassured.response.Response;

public class TestTemplateUI extends UnifiedFramework {

	WebDriver driver;
	LoginPage loginPage;
	public Map<String, String> testData;
	public TemplatePage templatepage;
	public HomePage homepage;
	public Properties configProperty;
	Efficacies efficacies;
	private CIMAdmin cimAdmin;
	private Map<String, String> jsonData;
	private SFDCRestUtils sfdcRestUtils;
	private String instanceURL;
	public String baseURL;
	SoftAssert softassert;
	private ResponseValidatorBase responseValidator;
	private Response response;
	public GenericPage genericPage;

	@BeforeClass(alwaysRun = true)
	@Parameters({ "runParallel", "environment", "browser", "hubURL" })
	public void setUp(String runParallel, String environment, String browser, String hubURL) throws Exception {

		WebDriverUtils utils = new WebDriverUtils();
		utils.initializeDriver(browser, hubURL);
		driver = utils.getDriver();
		efficacies = new Efficacies();
		loginPage = new LoginPage(driver);
		configProperty = efficacies.loadPropertyFile(environment);
		loginPage = loginPage.navigateToLoginPage(configProperty.getProperty("LoginURL"));
		loginPage.waitForLoginPageLoad().loginToApp(configProperty.getProperty("LoginUser"),
				configProperty.getProperty("LoginPassword"));
		homepage = new HomePage(driver, configProperty);
		homepage.navigateToCIMAdmin();
		sfdcRestUtils = new SFDCRestUtils();
		SFDCHelper.setMasterProperty(configProperty);
		instanceURL = SFDCHelper.setAccessToken(sfdcRestUtils);
		cimAdmin = new CIMAdmin(instanceURL, sfdcRestUtils);
		softassert = new SoftAssert();

	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() throws Exception {
		responseValidator = new ResponseValidatorBase();
		genericPage = new GenericPage(driver);
	}

	@Test(description = "TC-463 Verify  Qualification formulas on the Benefit only templates", groups = { "Regression",
			"Medium", "UI" })
	public void verifyTemplateQualificationOnDiscrete() throws Exception {

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createFieldExpressionId");
		String fieldExpressionId = cimAdmin.getFieldExpressionId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdBenefit");
		String stepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdBenefit");
		String nonStepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdQualification");
		String stepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdQualification");
		String nonStepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "linkCalcFormulaToExpressionId");
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, nonStepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdQualification, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, nonStepCalcFormulaIdQualification, fieldExpressionId);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewDataSourceAPI");
		cimAdmin.createDataSource(jsonData);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(nonStepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdQualification);
		cimAdmin.linkDatasourceToCalcFormula(nonStepCalcFormulaIdQualification);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "benefitOnlyTieredQnBLayoutAPI");
		String qnbLayoutId = cimAdmin.getQnBLayoutId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewTemplateAPI");
		response = cimAdmin.createTemplate(jsonData, qnbLayoutId);
		responseValidator.validateCreateSuccess(response);
		response = cimAdmin.getTemplate();
		responseValidator.validateGetTemplate(response, cimAdmin);
		jsonData.put("FormulaId__c", stepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", stepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", nonStepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", nonStepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		responseValidator.validateTemplateStatus(response, cimAdmin, RebatesConstants.draft);
		templatepage = homepage.navigateToEditTemplate(cimAdmin.templateData.getTemplateId());
		templatepage.moveToEditTemplate(cimAdmin.getTemplateData().getTemplateId());

		templatepage.addQualificationOnTiered(cimAdmin);
		softassert.assertEquals(false, templatepage.chkQualificationValue.isEmpty());
		softassert.assertEquals(false, templatepage.chkBenefitFormulaValue.isEmpty());

		templatepage.qnbLayoutDefinition(templatepage.ddlQBselect, templatepage.ddlTierSelect);
		softassert.assertEquals(false, templatepage.chkBenefitFormulaValue.isEmpty());
		softassert.assertAll();
	}

	@Test(description = "TC 555-Validate activation of new Benefit formula Discrete template and its navigation via Template Name", groups = {
			"Regression", "UI", "High" })
	public void verifyTemplateActivationAndNavigationViaTemplateName() throws Exception {

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createFieldExpressionId");
		String fieldExpressionId = cimAdmin.getFieldExpressionId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdBenefit");
		String stepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdQualification");
		String stepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "linkCalcFormulaToExpressionId");
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdQualification, fieldExpressionId);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewDataSourceAPI");
		cimAdmin.createDataSource(jsonData);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdQualification);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "benefitOnlyTieredQnBLayoutAPI");
		String qnbLayoutId = cimAdmin.getQnBLayoutId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewTemplateAPI");
		response = cimAdmin.createTemplate(jsonData, qnbLayoutId);
		responseValidator.validateCreateSuccess(response);
		response = cimAdmin.getTemplate();
		responseValidator.validateGetTemplate(response, cimAdmin);
		jsonData.put("FormulaId__c", stepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", stepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		cimAdmin.activateTemplate(RebatesConstants.responseNocontent);

		templatepage = homepage.navigateToTemplates();
		templatepage.moveToTemplateViaIdClick(cimAdmin.getTemplateData().getTemplateId());
		softassert.assertTrue(templatepage.templateEditURL, "Verify the URL of Template Edit page");
		softassert.assertAll();
	}

	@Test(description = "TC-562 Verify creation and Activation of QnBt and Tier template Via Details View page",groups = {
			"Regression", "Medium", "UI" })
	public void verifyTemplateTierMultipleQualification() throws Exception {

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createFieldExpressionId");
		String fieldExpressionId = cimAdmin.getFieldExpressionId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdBenefit");
		String stepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdBenefit");
		String nonStepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdQualification");
		String stepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdQualification");
		String nonStepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "linkCalcFormulaToExpressionId");
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, nonStepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdQualification, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, nonStepCalcFormulaIdQualification, fieldExpressionId);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewDataSourceAPI");
		cimAdmin.createDataSource(jsonData);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(nonStepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdQualification);
		cimAdmin.linkDatasourceToCalcFormula(nonStepCalcFormulaIdQualification);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json",
				"qualificationAndBenefitTieredQnBLayoutAPI");
		String qnbLayoutId = cimAdmin.getQnBLayoutId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewTemplateAPI");
		response = cimAdmin.createTemplate(jsonData, qnbLayoutId);
		responseValidator.validateCreateSuccess(response);
		response = cimAdmin.getTemplate();
		responseValidator.validateGetTemplate(response, cimAdmin);
		jsonData.put("FormulaId__c", stepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", stepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", nonStepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", nonStepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		responseValidator.validateTemplateStatus(response, cimAdmin, RebatesConstants.draft);
		templatepage = homepage.navigateToEditTemplateView(cimAdmin.templateData.getTemplateId());
		genericPage.clickButtonAndWait(templatepage.btnActive, genericPage.txtToastMessage);
		softassert.assertEquals(RebatesConstants.messageActivateSuccessfully, genericPage.txtToastMessage.getText());
		softassert.assertAll();

	}

	@Test(description = "TC 566 Verify Activation of Multiple Benefit Product and Discrete Template via Detail View Page", groups = { "Regression",
			"Medium", "UI" })
	public void verifyTemplateTierMultipleQualificationActivationViaDetailView() throws Exception {

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createFieldExpressionId");
		String fieldExpressionId = cimAdmin.getFieldExpressionId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdBenefit");
		String stepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdBenefit");
		String nonStepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdQualification");
		String stepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdQualification");
		String nonStepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "linkCalcFormulaToExpressionId");
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, nonStepCalcFormulaIdBenefit, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, stepCalcFormulaIdQualification, fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, nonStepCalcFormulaIdQualification, fieldExpressionId);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewDataSourceAPI");
		cimAdmin.createDataSource(jsonData);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(nonStepCalcFormulaIdBenefit);
		cimAdmin.linkDatasourceToCalcFormula(stepCalcFormulaIdQualification);
		cimAdmin.linkDatasourceToCalcFormula(nonStepCalcFormulaIdQualification);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json",
				"multipleQualificationAndBenefitTieredQnBLayoutAPI");
		String qnbLayoutId = cimAdmin.getQnBLayoutId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewTemplateAPI");
		response = cimAdmin.createTemplate(jsonData, qnbLayoutId);
		responseValidator.validateCreateSuccess(response);
		response = cimAdmin.getTemplate();
		responseValidator.validateGetTemplate(response, cimAdmin);
		jsonData.put("FormulaId__c", stepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", stepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", nonStepCalcFormulaIdBenefit);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", nonStepCalcFormulaIdQualification);
		jsonData.put("DataSourceId__c", cimAdmin.getDataSourceData().getDataSourceId());
		responseValidator.validateTemplateStatus(response, cimAdmin, RebatesConstants.draft);
		templatepage = homepage.navigateToEditTemplateView(cimAdmin.templateData.getTemplateId());
		genericPage.clickButtonAndWait(templatepage.btnActive, genericPage.txtToastMessage);
		softassert.assertEquals(RebatesConstants.messageActivateSuccessfully, genericPage.txtToastMessage.getText());
		softassert.assertAll();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}
}
