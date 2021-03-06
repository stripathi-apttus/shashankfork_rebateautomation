package com.apttus.sfdc.rebates.lightning.api.pojo;

import java.util.Map;
import com.apttus.sfdc.rebates.lightning.api.library.CIMAdmin;
import com.apttus.sfdc.rebates.lightning.generic.utils.SFDCHelper;
import com.google.gson.Gson;

public class CreateTemplatePojo {

	private String Description__c;
	private String Status__c;
	private String QnBLayoutId__c;
	private String Name;
	public String templateId;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDescription__c() {
		return Description__c;
	}

	public void setDescription__c(String description__c) {
		this.Description__c = description__c;
	}

	public String getStatus__c() {
		return Status__c;
	}

	public void setStatus__c(String status__c) {
		this.Status__c = status__c;
	}

	public String getQnBLayoutId__c() {
		return QnBLayoutId__c;
	}

	public void setQnBLayoutId__c(String qnBLayoutId__c) {
		this.QnBLayoutId__c = qnBLayoutId__c;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String createTemplateRequest(Map<String, String> testData, CIMAdmin cimAdmin, String qnbLayoutId) {

		CreateTemplatePojo createTemplatePojo = new CreateTemplatePojo();
		createTemplatePojo.setName(testData.get("Name"));
		if (testData.get("Name").equalsIgnoreCase("{RANDOM}")) {
			createTemplatePojo.setName("Rebates_Auto_Template_" + SFDCHelper.randomNumberGenerator());
		}
		createTemplatePojo.setDescription__c(testData.get("Description__c"));
		createTemplatePojo.setQnBLayoutId__c(qnbLayoutId);
		createTemplatePojo.setStatus__c(testData.get("Status__c"));
		cimAdmin.setTemplateData(createTemplatePojo);
		return new Gson().toJson(createTemplatePojo);
	}
}
/*------------------- Create Template Request ------------------
 {
  "Description__c": "Shashank T3", "Name": "Test",
  "QnB_Layout_Id__c": "a593i000000LC0tAAG", "Status__c": "Draft"
  }
 */