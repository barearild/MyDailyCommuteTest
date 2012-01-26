package com.barearild.commute.test.service.trafikanten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.test.InstrumentationTestCase;

import com.barearild.commute.service.trafikanten.JSONConnector;
import com.barearild.commute.service.trafikanten.places.Area;
import com.barearild.commute.service.trafikanten.places.Place;
import com.barearild.commute.service.trafikanten.places.Stop;
import com.barearild.commute.service.trafikanten.places.StopType;
import com.barearild.commute.test.R;

public class JSONConnectorTest extends InstrumentationTestCase {

	public void testShouldGetResultFromTrafikanten() throws JSONException {

		String request = "http://api-test.trafikanten.no/Place/FindMatchesOfType/jernbanetorget?placeType=Stop";

		JSONArray result = JSONConnector.getJsonResult(request);

		assertNotNull(result);
		assertTrue(result.length() > 0);

		System.out.println(result.toString(1));

		JSONObject object = result.getJSONObject(0);

		Place place = Place.parseJsonObject(object);

		assertTrue(place instanceof Stop);

	}

	public void testShouldParseStopJsonObjectsCorrectly() throws JSONException {

		JSONArray result = readLocalJSonFile(R.raw.stop_test);
		
		Stop stop = (Stop)Place.parseJsonObject(result.getJSONObject(0));
		
		assertNotNull(stop);
		assertEquals(0, stop.getWalkingDistance().intValue());
		assertNull(stop.getArrivalTime());
		assertFalse(stop.isAlightingAllowed());
		assertNull(stop.getDepartureTime());
		assertFalse(stop.isBoardingAllowed());
		assertTrue(stop.isRealTimeStop());
		assertEquals(2, stop.getRank().intValue());
		assertEquals("1", stop.getZone());
		assertEquals(597864, stop.getLocation().getLatitudeE6());
		assertEquals(6642854, stop.getLocation().getLongitudeE6());
		assertEquals("Jernbanetorget (foran Oslo S)", stop.getName());
		assertEquals("Oslo", stop.getDistrict());
		assertSame(StopType.Stop, stop.getType());
		assertEquals("JERB", stop.getShortName());
	}
	
	public void testShouldParseAreaJsonObjectCorrectly() throws JSONException {
		JSONArray result = readLocalJSonFile(R.raw.area_test);
		
		Area area = (Area)Place.parseJsonObject(result.getJSONObject(0));
		
		assertNotNull(area);
		assertEquals(10, area.getStops().size());
	}

	private JSONArray readLocalJSonFile(int resourceId) {
		JSONArray result = null;
		StringBuilder responseString = new StringBuilder();
		Resources res = getInstrumentation().getContext().getResources();

		InputStream instream = res.openRawResource(resourceId);

		BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				responseString.append(line);
			}
			result = new JSONArray(responseString.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;

	}

}
