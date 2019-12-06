package com.apttus.sfdc.rebates.lightning;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.apttus.sfdc.rebates.common.StartUpPage;
import com.apttus.sfdc.rebates.ui.library.DataSourcePage;


public class HomePage extends StartUpPage {
	
	
	@FindBy(css=".logout ")
	public WebElement logOutLink;
	
	@FindBy(css="[title='Close this window']")
	public WebElement closeWindow;
	
	@FindBy(css=".oneUserProfileCardTrigger span .uiImage")
	public WebElement userProfileIcon;
	
	@FindBy(xpath="//span[text()='Data Sources']")
	public WebElement dataSourcelnk;
	
	@FindBy(xpath="//*[@data-aura-class='uiOutputText forceBreadCrumbItem'][text()='Link Templates']")
	public WebElement lnkTemplatepagelabel;
	
	
	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		sfdcAcolyte.setWaitTime(60);
	}
	 	
	public HomePage logout() throws Exception {
		sfdcAcolyte.waitTillElementIsClickable(closeWindow)
		.jsClick(closeWindow);
		sfdcAcolyte.waitTillElementIsClickable(userProfileIcon)
		.jsClick(userProfileIcon);
		sfdcAcolyte.waitTillElementIsClickable(logOutLink)
		.jsClick(logOutLink);
		return PageFactory.initElements(driver, HomePage.class);
	}
	
	public DataSourcePage navigateToDataSource() throws Exception {
		
		
		sfdcAcolyte.waitTillElementIsClickable(dataSourcelnk).
                    jsClick(dataSourcelnk);
			
		return PageFactory.initElements(driver, DataSourcePage.class);
		
	}
		
		
}

