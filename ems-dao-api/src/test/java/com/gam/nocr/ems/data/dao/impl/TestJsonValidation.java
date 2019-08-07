package com.gam.nocr.ems.data.dao.impl;

import com.gam.nocr.ems.data.domain.ws.ClientWorkstationInfo;
import com.gam.nocr.ems.util.Utils;
import org.json.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestJsonValidation {
    public static void main(String[] args) {
        String json = "[{dt=2019-05-29, value=177.38}, {dt=2019-05-30, value=178.3}, {dt=2019-05-31, value=175.07}, {dt=2019-06-03, value=173.3}, {dt=2019-06-04, value=179.64}, {dt=2019-06-05, value=182.54}, {dt=2019-06-06, value=185.22}, {dt=2019-06-07, value=190.15}, {dt=2019-06-10, value=192.58}, {dt=2019-06-11, value=194.81}, {dt=2019-06-12, value=194.19}, {dt=2019-06-13, value=194.15}, {dt=2019-06-14, value=192.74}, {dt=2019-06-17, value=193.89}, {dt=2019-06-18, value=198.45}, {dt=2019-06-19, value=197.87}, {dt=2019-06-20, value=199.46}, {dt=2019-06-21, value=198.78}, {dt=2019-06-24, value=198.58}, {dt=2019-06-25, value=195.57}, {dt=2019-06-26, value=199.8}, {dt=2019-06-27, value=199.74}, {dt=2019-06-28, value=197.92}]";
        System.out.println(TestJsonValidation.isJSONValid(json));
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
