package com.apttus.sfdc.rebates.lightning.ui.library;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.apttus.sfdc.rebates.lightning.common.GenericPage;
import com.apttus.sfdc.rebates.lightning.generic.utils.SFDCHelper;

public class DataSourcePage extends GenericPage {

	@FindBy(xpath = "//*[text()='Save']")
	public WebElement btnSave;

	@FindBy(css = ".forceActionsText")
	public WebElement txtToastMessage;

	@FindBy(xpath = "//div/button[@title='Close']")
	public WebElement btnCloseToastMessage;

	@FindBy(xpath = "//label[text()='Name']/following-sibling::div/input")
	public WebElement txtDataSource;

	@FindBy(xpath = "//*[@data-field='fileSuffix']/div[1]")
	public WebElement txtFileSuffix;

	@FindBy(xpath = "//input[@name='Transaction Line Object']")
	public WebElement ddlSelectTransMetaData;

	@FindBy(xpath = "//input[@name='Product Field']")
	public WebElement ddlSelectProductField;

	@FindBy(xpath = "//input[@name='Incentive Account Field']")
	public WebElement ddlIncentiveAccount;

	@FindBy(xpath = "//input[@name='Calculation Date Field']")
	public WebElement ddlCalculationDate;

	@FindBy(xpath = "//*[text()='Account Location']")
	public WebElement ddlAccountLocation;

	@FindBy(xpath = "//*[text()='Creation Date']")
	public WebElement ddlCalculationDateValue;

	@FindBy(xpath = "//*[text()='Base Product']")
	public WebElement ddlProductValue;

	@FindBy(xpath = "//*[text()='Account']")
	public WebElement ddlIncentiveAccountValue;

	@FindBy(xpath = "//option[text()='txt']")
	public WebElement fileExtension;

	@FindBy(xpath = "//*[text()='Related']")
	public WebElement lblRelatedTab;
	WebDriverWait wait;
	String cmbTxt = "//span[@class='uiOutputText'][text()='OPTION']";
	String lnkTemplateId = "//*[@data-recordid='OPTION']";
	String lnkFormulaTab = "//*[@data-row-key-value='OPTION']";

	public String nameDataSource = "Rebates_Auto_DataSource_" + SFDCHelper.randomNumberGenerator();

	public DataSourcePage(WebDriver driver) {
		super(driver);
		wait = new WebDriverWait(driver, 120);
		PageFactory.initElements(driver, this);
	}

	public void clickSave() throws Exception {
		sfdcAcolyte.waitTillElementIsVisible(btnSave).waitTillElementIsClickable(btnSave).click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForTransactionLineObject() throws Exception {

		nameDataSource = "Rebates_Auto_DataSource_" + SFDCHelper.randomNumberGenerator();
		sfdcAcolyte.click(btnCloseToastMessage);
		sfdcAcolyte.waitTillElementIsVisible(txtDataSource).clickAndSendkeys(txtDataSource, nameDataSource)
				.click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForCalculationDate() throws Exception {

		sfdcAcolyte.click(btnCloseToastMessage);
		nameDataSource = "Rebates_Auto_DataSource_" + SFDCHelper.randomNumberGenerator();
		sfdcAcolyte.waitTillElementIsVisible(txtDataSource).clickAndSendkeys(txtDataSource, nameDataSource);
		sfdcAcolyte.click(ddlSelectTransMetaData).click(ddlSelectTransMetaData).click(ddlSelectTransMetaData)
				.waitTillElementIsVisible(ddlAccountLocation).waitTillElementIsClickable(ddlAccountLocation)
				.jsScrollAndClick(ddlAccountLocation).click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForProduct() throws Exception {
		sfdcAcolyte.click(btnCloseToastMessage);
		sfdcAcolyte.click(ddlCalculationDate).waitTillElementIsClickable(ddlCalculationDateValue)
				.jsScroll(ddlCalculationDateValue).click(ddlCalculationDateValue).click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForIncentiveAccount() throws Exception {
		sfdcAcolyte.click(btnCloseToastMessage);
		sfdcAcolyte.click(ddlSelectProductField).waitTillElementIsClickable(ddlProductValue).jsScroll(ddlProductValue)
				.click(ddlProductValue).click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForFileSuffixToIgnore() throws Exception {
		sfdcAcolyte.click(btnCloseToastMessage);
		sfdcAcolyte.click(ddlIncentiveAccount).waitTillElementIsClickable(ddlIncentiveAccountValue)
				.jsScroll(ddlIncentiveAccountValue).click(ddlIncentiveAccountValue).click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForFileExtension(Map<String, String> testData) throws Exception {
		sfdcAcolyte.click(btnCloseToastMessage);
		sfdcAcolyte.clickAndSendkeys(txtFileSuffix, testData.get("FileSuffixToignore__c")).click(btnSave)
				.click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}

	public void verifyValidationMessageForRecordDelimter() throws Exception {
		sfdcAcolyte.click(btnCloseToastMessage);
		sfdcAcolyte.click(fileExtension).click(btnSave).click(btnSave);
		sfdcAcolyte.waitTillElementIsVisible(txtToastMessage);
	}
}