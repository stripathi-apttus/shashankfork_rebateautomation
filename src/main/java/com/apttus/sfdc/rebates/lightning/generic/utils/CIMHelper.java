package com.apttus.sfdc.rebates.lightning.generic.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.apttus.customException.ApplicationException;
import com.apttus.helpers.Efficacies;
import com.apttus.sfdc.rebates.lightning.api.library.BenefitProductQnB;
import com.apttus.sfdc.rebates.lightning.api.library.CIM;
import com.apttus.sfdc.rebates.lightning.api.library.CIMAdmin;
import com.apttus.sfdc.rebates.lightning.api.validator.BenefitProductValidator;
import com.jayway.restassured.response.Response;

public class CIMHelper {

	public Efficacies efficacies;
	public Map<String, String> jsonData;
	public String stepCalcFormulaIdBenefit;
	public String stepCalcFormulaIdQualification;
	public String nonStepCalcFormulaIdBenefit;
	public String nonStepCalcFormulaIdQualification;
	public Response response;
	public BenefitProductValidator responseValidator;
	public String fieldExpressionId;
	public List<Map<String, String>> jsonArrayData;
	public Map<String, String> formulaMap;

	public CIMHelper() throws Exception {
		efficacies = new Efficacies();
		responseValidator = new BenefitProductValidator();
		jsonArrayData = new ArrayList<Map<String, String>>();
		formulaMap = new HashMap<String, String>();
	}
	
	public void deleteDataSourceForIncentive(CIMAdmin cimAdmin) throws Exception {
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewDataSourceOrderLineItem");
		String id = cimAdmin.getDataSourceForTransactionLineObjectName(jsonData);
		if (id != null) {
			cimAdmin.deleteDataSource(id);
			response = cimAdmin.getDataSourceForId(id);
			responseValidator.validateDeleteSuccess(response);
		}
	}

	public void createDataSourceAndFormulasForIncentives(CIMAdmin cimAdmin) throws Exception {
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewDataSourceOrderLineItem");
		DataHelper.setIncentiveDataSourceId(cimAdmin.createDataSource(jsonData));
		
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createFieldExpressionId");
		fieldExpressionId = cimAdmin.getFieldExpressionId(jsonData);
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdBenefit");
		stepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		formulaMap.put("stepCalcFormulaIdBenefit", stepCalcFormulaIdBenefit);

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdBenefit");
		nonStepCalcFormulaIdBenefit = cimAdmin.getCalcFormulaId(jsonData);
		formulaMap.put("nonStepCalcFormulaIdBenefit", nonStepCalcFormulaIdBenefit);

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createStepCalcFormulaIdQualification");
		stepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		formulaMap.put("stepCalcFormulaIdQualification", stepCalcFormulaIdQualification);

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNonStepCalcFormulaIdQualification");
		nonStepCalcFormulaIdQualification = cimAdmin.getCalcFormulaId(jsonData);
		formulaMap.put("nonStepCalcFormulaIdQualification", nonStepCalcFormulaIdQualification);
		
		DataHelper.setFormulaDataMap(formulaMap);

		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "linkCalcFormulaToExpressionId");
		cimAdmin.linkCalcFormulaToExpression(jsonData, DataHelper.getFormulaDataMap().get("stepCalcFormulaIdBenefit"), fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdBenefit"), fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, DataHelper.getFormulaDataMap().get("stepCalcFormulaIdQualification"), fieldExpressionId);
		cimAdmin.linkCalcFormulaToExpression(jsonData, DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdQualification"), fieldExpressionId);

		cimAdmin.linkDatasourceToCalcFormula(DataHelper.getFormulaDataMap().get("stepCalcFormulaIdBenefit"));
		cimAdmin.linkDatasourceToCalcFormula(DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdBenefit"));
		cimAdmin.linkDatasourceToCalcFormula(DataHelper.getFormulaDataMap().get("stepCalcFormulaIdQualification"));
		cimAdmin.linkDatasourceToCalcFormula(DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdQualification"));
	}

	public Response createAndValidateTemplate(CIMAdmin cimAdmin, String qnbLayoutId) throws Exception {
		jsonData = efficacies.readJsonElement("CIMAdminTemplateData.json", "createNewTemplateAPI");
		response = cimAdmin.createTemplate(jsonData, qnbLayoutId);
		responseValidator.validateCreateSuccess(response);
		response = cimAdmin.getTemplate();
		responseValidator.validateGetTemplate(response, cimAdmin);
		responseValidator.validateTemplateStatus(response, cimAdmin, RebatesConstants.draft);
		return response;
	}

	public void mapDataSourceAndFormulaToTemplateTiered(CIMAdmin cimAdmin) throws ApplicationException {
		jsonData.put("FormulaId__c", DataHelper.getFormulaDataMap().get("stepCalcFormulaIdBenefit"));
		jsonData.put("DataSourceId__c", DataHelper.getIncentiveDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", DataHelper.getFormulaDataMap().get("stepCalcFormulaIdQualification"));
		jsonData.put("DataSourceId__c", DataHelper.getIncentiveDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdBenefit"));
		jsonData.put("DataSourceId__c", DataHelper.getIncentiveDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdQualification"));
		jsonData.put("DataSourceId__c", DataHelper.getIncentiveDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
	}
	
	public void mapDataSourceAndFormulaToTemplateDiscrete(CIMAdmin cimAdmin) throws ApplicationException {
		jsonData.put("FormulaId__c", DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdBenefit"));
		jsonData.put("DataSourceId__c", DataHelper.getIncentiveDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
		jsonData.put("FormulaId__c", DataHelper.getFormulaDataMap().get("nonStepCalcFormulaIdQualification"));
		jsonData.put("DataSourceId__c", DataHelper.getIncentiveDataSourceId());
		cimAdmin.mapProgramTemplateDataSource(jsonData);
	}
	
	public void activateTemplateAndSetIdForIncentive(CIMAdmin cimAdmin) throws ApplicationException {
		cimAdmin.activateTemplate(RebatesConstants.responseNocontent);
		response = cimAdmin.getTemplate();
		responseValidator.validateTemplateStatus(response, cimAdmin, RebatesConstants.activate);
	}

	public void addAndValidateIncentive(Map<String, String> jsonDataMap, String incentiveTemplateId, CIM cim)
			throws Exception {
		jsonData = jsonDataMap;
		jsonData.put("ProgramTemplateId__c", incentiveTemplateId);
		cim.createNewIncentive(jsonData);
		response = cim.getIncentiveDetails();
		responseValidator.validateIncentiveDetails(jsonData, response, cim);
	}

	public Response addAndValidateQnBOnIncentive(BenefitProductQnB benefitProductQnB, String addQnBJsonArray)
			throws Exception {
		jsonArrayData = SFDCHelper.readJsonArray("CIMIncentiveQnBData.json", addQnBJsonArray);
		benefitProductQnB.addIncentiveQnB(jsonArrayData);
		response = benefitProductQnB.getIncentiveQnB();
		responseValidator.validateIncentiveQnB(benefitProductQnB.getRequestValue("addQnBRequest"), response);
		return response;
	}

	public void addAndValidateParticipant(CIM benefitProductQnB, String addParticipantsJson) throws Exception {
		jsonData = efficacies.readJsonElement("CIMTemplateData.json", addParticipantsJson);
		benefitProductQnB.addParticipants(jsonData);
		response = benefitProductQnB.getParticipantsDetails();
		responseValidator.validateParticipantsDetails(jsonData, response, benefitProductQnB);
	}

	public void activateAndValidateIncentive(BenefitProductQnB benefitProductQnB) throws ApplicationException {
		benefitProductQnB.activateIncentive();
		response = benefitProductQnB.getIncentiveDetails();
		responseValidator.validateIncentiveStatus(RebatesConstants.statusActivated, response,
				benefitProductQnB.getIncentiveData().incentiveId);
	}

	public void deleteQnBAndValidateOnIncenive(BenefitProductQnB benefitProductQnB, String addQnBJsonArray)
			throws Exception {
		jsonData = efficacies.readJsonElement("CIMIncentiveQnBData.json", "deleteBenefitLine");
		benefitProductQnB.deleteQnBBenefitLine(jsonData.get("SectionId"));
		response = benefitProductQnB.getIncentiveQnB();
		responseValidator.validateDeleteQnBBenefitLine(response, jsonData.get("SectionId"));
	}
}
