package com.DeliciousRecipes.nangboo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;


public class Ingredient {
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	String name = "";
	String expirationDate = "";
	String memo = "";
	
	Date date;
	
	public void stringToDate(){
		
		try {
			date = dateFormat.parse(expirationDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
