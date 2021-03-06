package com.apttus.sfdc.rebates.lightning.api.validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.asserts.SoftAssert;
import com.apttus.customException.ApplicationException;
import com.apttus.sfdc.rebates.lightning.api.library.CIM;
import com.apttus.sfdc.rebates.lightning.api.library.CIMAdmin;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.response.Response;

public class ResponseValidatorBase {
	protected JsonParser parser = new JsonParser();
	protected SoftAssert softassert;

	public void validateCreateSuccess(Response response) {
		softassert = new SoftAssert();
		boolean success = (parser.parse(response.getBody().asString())).getAsJsonObject().get("success").getAsBoolean();
		softassert.assertEquals(success, true, "Validate success flag");
		softassert.assertAll();
	}

	public void validateGetDataSource(Response response, String dataSourceId) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1, "Validate response size");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Id").getAsString(), dataSourceId, "Validate datasource id");
		softassert.assertAll();
	}

	public void validateDeleteSuccess(Response response) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 0, "Validate response size");
		softassert.assertAll();
	}

	public void validateGetTemplate(Response response, CIMAdmin cimAdmin) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1, "Validate response size");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Id").getAsString(), cimAdmin.getTemplateData().getTemplateId(),
				"Validate Template id");
		softassert.assertEquals(records.get("Name").getAsString(), cimAdmin.getTemplateData().getName(),
				"Validate Template Name");
		softassert.assertAll();
	}

	public void validateGetLinkTemplates(Map<String, String> testData, Response response, CIMAdmin cimAdmin) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1, "Validate response size");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Id").getAsString(), cimAdmin.linkTemplatesData.getLinkTemplateId(),
				"Validate linkTemplate id");
		softassert.assertEquals(records.get("IncentiveType__c").getAsString(), testData.get("IncentiveType__c"),
				"Validate ProgramType in linkTemplate");
		softassert.assertEquals(records.get("IncentiveSubType__c").getAsString(), testData.get("IncentiveSubType__c"),
				"Validate ProgramSub_Type in linkTemplate");
		softassert.assertAll();
	}

	public void validateLinkTemplatesStatus(Response response, CIMAdmin cimAdmin, String status) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1, "Validate response size");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Status__c").getAsString(), status, "Validate status in linkTemplate");
		softassert.assertAll();
	}

	public void validateIncentiveDetails(Map<String, String> testData, Response response, CIM cim) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1,
				"Validate response size, Response should have single record");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Apttus_Config2__EffectiveDate__c").getAsString(),
				cim.incentiveData.getApttus_Config2__EffectiveDate__c(), "Validate Incentive Start Date");
		softassert.assertEquals(records.get("Apttus_Config2__ExpirationDate__c").getAsString(),
				cim.incentiveData.getApttus_Config2__ExpirationDate__c(), "Validate Incentive End Date");
		softassert.assertEquals(records.get("BenefitLevel__c").getAsString(), testData.get("BenefitLevel__c"),
				"Validate Incentive BenefitLevel");
		softassert.assertEquals(records.get("MeasurementLevel__c").getAsString(), testData.get("MeasurementLevel__c"),
				"Validate Incentive MeasurementLevel");
		softassert.assertEquals(records.get("Apttus_Config2__RecurrenceFrequency__c").getAsString(),
				testData.get("Apttus_Config2__RecurrenceFrequency__c"), "Validate Incentive MeasurementFrequency");
		softassert.assertEquals(records.get("Id").getAsString(), cim.incentiveData.getIncentiveId(),
				"Validate Incentive Id");
		softassert.assertEquals(records.get("Name").getAsString(), cim.incentiveData.getName(),
				"Validate Incentive Name");
		softassert.assertEquals(records.get("Apttus_Config2__UseType__c").getAsString(),
				testData.get("Apttus_Config2__UseType__c"), "Validate Program UseType");
		softassert.assertEquals(records.get("IncentiveType__c").getAsString(), testData.get("IncentiveType__c"),
				"Validate Incentive Type");
		softassert.assertEquals(records.get("IncentiveSubType__c").getAsString(), testData.get("IncentiveSubType__c"),
				"Validate Incentive SubType");
		softassert.assertAll();
	}

	public void validateTemplateStatus(Response response, CIMAdmin cimAdmin, String Status) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1, "Validate response size");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Id").getAsString(), cimAdmin.getTemplateData().getTemplateId(),
				"Validate Template id");
		softassert.assertEquals(records.get("Status__c").getAsString(), Status,
				"Validate Template Status-Draft/Active/Inactive");
		softassert.assertAll();
	}

	public void validateUpdatedTemplate(Response response, CIMAdmin cimAdmin, Map<String, String> testData,
			String qnbLayoutId) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1,
				"Validate response size, Response should have single record");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Id").getAsString(), cimAdmin.getTemplateData().getTemplateId(),
				"Validate Template id");
		softassert.assertEquals(records.get("Description__c").getAsString(), testData.get("Description__c"),
				"Validate updated Template Decription");
		softassert.assertEquals(records.get("Name").getAsString(), cimAdmin.getTemplateData().getName(),
				"Validate template Name");
		softassert.assertEquals(records.get("QnBLayoutId__c").getAsString(), qnbLayoutId,
				"Validate Updated Template QnB Layout Id");
		softassert.assertAll();
	}

	public void validateFailureResponse(Response response, String errorcode, String message) {
		softassert = new SoftAssert();
		boolean messageExists = false;
		JsonArray responseBody = parser.parse(response.getBody().asString()).getAsJsonArray();
		messageExists = responseBody.get(0).getAsJsonObject().get("message").getAsString().contains(message);
		softassert.assertTrue(messageExists, "Verify failure message exists");
		softassert.assertEquals(responseBody.get(0).getAsJsonObject().get("errorCode").getAsString(), errorcode,
				"Verify failure Errorcode");
		softassert.assertAll();
	}

	public void validateParticipantsDetails(Map<String, String> testData, Response response, CIM cim)
			throws ApplicationException {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1,
				"Validate response size, Response does not have single record");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("EffectiveDate__c").getAsString(),
				cim.participantsData.getIncentiveParticipant().getEffectiveDate__c(), "Validate Participant Effective Date");
		softassert.assertEquals(records.get("ExpirationDate__c").getAsString(),
				cim.participantsData.getIncentiveParticipant().getExpirationDate__c(), "Validate Participant Expired Date");
		softassert.assertEquals(records.get("Id").getAsString(), cim.participantsData.getIncentiveParticipant().getId(),
				"Validate Incentive Participant Id");
		softassert.assertEquals(records.get("Incentive__c").getAsString(), cim.incentiveData.getIncentiveId(),
				"Validate Incentive Id");
		softassert.assertAll();
	}
	
	public void validateAvailableParticipant(List<Map<String, String>> testData, Response response, CIM cim)
			throws ApplicationException {
		softassert = new SoftAssert();
		int expectedSize = testData.size();
		JsonArray resp = parser.parse(response.getBody().asString()).getAsJsonArray();
		softassert.assertEquals(resp.size(), expectedSize, "Validate response size");
		softassert.assertAll();

		List<String> testAccount = testData.stream().map(p -> p.get("Account__c")).collect(Collectors.toList());
		List<String> testEffectiveDates = testData.stream().map(p -> p.get("EffectiveDate__c"))
				.collect(Collectors.toList());
		List<String> testExpirationDates = testData.stream().map(p -> p.get("ExpirationDate__c"))
				.collect(Collectors.toList());

		for (int j = 0; j < expectedSize; j++) {
			JsonObject records = resp.get(j).getAsJsonObject();
			if (testAccount.contains(records.get("Account__c").getAsString())) {
				int index = testAccount.indexOf(records.get("Account__c").getAsString());
				String testAcc = testAccount.get(index);
				String testEff = testEffectiveDates.get(index);
				String testExp = testExpirationDates.get(index);
				softassert.assertEquals(records.get("Account__c").getAsString(), testAcc,
						"Validate Participant Effective Date");
				softassert.assertEquals(records.get("EffectiveDate__c").getAsString(), testEff,
						"Validate Participant Effective Date");
				softassert.assertEquals(records.get("ExpirationDate__c").getAsString(), testExp,
						"Validate Participant Expiration Date");
			} else {
				throw new ApplicationException("Expected Participants are not availabe on Incentive");
			}
		}
		softassert.assertAll();
	}
	
	public void validateIncentiveStatus(String expectedStatus, Response response, String incentiveId) {
		softassert = new SoftAssert();
		JsonObject resp = parser.parse(response.getBody().asString()).getAsJsonObject();
		softassert.assertEquals(resp.get("totalSize").getAsInt(), 1,
				"Validate response size, Response should have single record");
		softassert.assertAll();
		JsonObject records = resp.getAsJsonArray("records").get(0).getAsJsonObject();
		softassert.assertEquals(records.get("Apttus_Config2__Status__c").getAsString(), expectedStatus,
				"Validate Incentive Status");
		softassert.assertEquals(records.get("Id").getAsString(), incentiveId, "Validate Incentive Id");
		softassert.assertAll();
	}

	public void validateParticipantFailureResponse(Response response, String errorFields, String errorMessage) {
		softassert = new SoftAssert();
		JsonObject responsebody = parser.parse(response.getBody().asString()).getAsJsonArray().get(0).getAsJsonObject();
		softassert.assertEquals(responsebody.get("ErrorMessages").getAsString(), errorMessage,
				"Verify failure Error Message");
		softassert.assertEquals(responsebody.get("ErrorFields").toString(), errorFields, "Verify failure Error Fields");
		softassert.assertAll();
	}
}
