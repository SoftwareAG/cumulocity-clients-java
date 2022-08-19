package com.cumulocity.rest.representation;

import org.junit.jupiter.api.Test;
import org.svenson.JSONParser;

import com.cumulocity.rest.representation.measurement.MeasurementsApiRepresentation;

public class DynamicApiExtensionTest {

    public String measurementJsonExtended = "{\n" + 
            "        \"measurements\": {\n" + 
            "            \"measurements\": null,\n" + 
            "            \"self\": \"http://demos.cumulocity.com/measurement/measurements\"\n" + 
            "        },\n" + 
            "        \"measurementsForTest\": \"http://demos.cumulocity.com/measurement/measurements?dateFrom={dateFrom}&dateTo={dateTo}&someParameter={someValue}\",\n" +
            "        \"measurementsForDate\": \"http://demos.cumulocity.com/measurement/measurements?dateFrom={dateFrom}&dateTo={dateTo}\",\n" + 
            "        \"measurementsForDateAndFragmentType\": \"http://demos.cumulocity.com/measurement/measurements?dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}\",\n" + 
            "        \"measurementsForDateAndFragmentTypeAndType\": \"http://demos.cumulocity.com/measurement/measurements?dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}&type={type}\",\n" + 
            "        \"measurementsForDateAndType\": \"http://demos.cumulocity.com/measurement/measurements?dateFrom={dateFrom}&dateTo={dateTo}&type={type}\",\n" + 
            "        \"measurementsForFragmentType\": \"http://demos.cumulocity.com/measurement/measurements?fragmentType={fragmentType}\",\n" + 
            "        \"measurementsForFragmentTypeAndType\": \"http://demos.cumulocity.com/measurement/measurements?fragmentType={fragmentType}&type={type}\",\n" + 
            "        \"measurementsForSource\": \"http://demos.cumulocity.com/measurement/measurements?source={source}\",\n" + 
            "        \"measurementsForSourceAndDate\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&dateFrom={dateFrom}&dateTo={dateTo}\",\n" + 
            "        \"measurementsForSourceAndDateAndFragmentType\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}\",\n" + 
            "        \"measurementsForSourceAndDateAndFragmentTypeAndType\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&dateFrom={dateFrom}&dateTo={dateTo}&fragmentType={fragmentType}&type={type}\",\n" + 
            "        \"measurementsForSourceAndDateAndType\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&dateFrom={dateFrom}&dateTo={dateTo}&type={type}\",\n" + 
            "        \"measurementsForSourceAndFragmentType\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&fragmentType={fragmentType}\",\n" + 
            "        \"measurementsForSourceAndFragmentTypeAndType\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&fragmentType={fragmentType}&type={type}\",\n" + 
            "        \"measurementsForSourceAndType\": \"http://demos.cumulocity.com/measurement/measurements?source={source}&type={type}\",\n" + 
            "        \"measurementsForType\": \"http://demos.cumulocity.com/measurement/measurements?type={type}\",\n" + 
            "        \"self\": \"http://demos.cumulocity.com/measurement\"\n" + 
            "    },\n" + 
            "    \"self\": \"http://demos.cumulocity.com/platform\",\n" + 
            "    \"user\": {\n" + 
            "        \"currentUser\": \"http://demos.cumulocity.com/user/currentUser\",\n" + 
            "        \"groupByName\": \"http://demos.cumulocity.com/user/{realm}/groupByName/{groupName}\",\n" + 
            "        \"groups\": \"http://demos.cumulocity.com/user/{realm}/groups\",\n" + 
            "        \"roles\": \"http://demos.cumulocity.com/user/roles\",\n" + 
            "        \"self\": \"http://demos.cumulocity.com/user\",\n" + 
            "        \"userByName\": \"http://demos.cumulocity.com/user/{realm}/userByName/{userName}\",\n" + 
            "        \"users\": \"http://demos.cumulocity.com/user/{realm}/users\"\n" + 
            "    }";

    @Test
    public void extendedApiShouldBeParsable() throws Exception {

        JSONParser.defaultJSONParser().parse(MeasurementsApiRepresentation.class, measurementJsonExtended);

    }

}
