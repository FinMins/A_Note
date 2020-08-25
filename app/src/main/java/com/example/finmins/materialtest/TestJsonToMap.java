package com.example.finmins.materialtest;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestJsonToMap {
	public static Map<String, Object> JsonToMap(String s) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String[] strs = s.trim().split(":");
		for(int i=0;i<strs.length-1;i++) {
			resultMap.put(strs[i], strs[i+1]);
		}
		return resultMap;
	}

}
