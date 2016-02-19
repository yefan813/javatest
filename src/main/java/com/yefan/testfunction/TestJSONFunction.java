package com.yefan.testfunction;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.yefan.testfunction.domain.Persion;

public class TestJSONFunction {
	public static void main(String[] args) {
		String testJSON = "{\"age\":25,\"marry\":false,\"name\":\"yefan\",\"sex\":1,\"work\":\"engineer\"}";
		
		Persion getPersion = getPersionObject(testJSON);
		System.out.println(getPersion);
		
		
//		System.out.println(getPersionJson());
		
		
	}
	
	/**
	 * @return
	 */
	private static String getPersionJson(){
		Persion persion = new Persion();
		persion.setName("yefan");
		persion.setAge(25);
		persion.setSex(1);
		persion.setWork("engineer");
		persion.setMarry(true);
		
		return JSON.toJSONString(persion);
	}
	
	/**
	 * @param json
	 * @return
	 */
	private static Persion getPersionObject(String json){
		Persion persion = null;
		if(!StringUtils.isEmpty(json)){
			persion = JSON.parseObject(json,Persion.class);
		}else{
			System.out.println("´«ÈëjsonÎª¿Õ");
		}
		return persion;
	}
}
