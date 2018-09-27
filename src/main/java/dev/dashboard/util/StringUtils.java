package dev.dashboard.util;

import com.github.javafaker.Faker;

public class StringUtils {

	
	public static String GenerateRandomName() {
		
 		return new Faker().name().firstName();
	}
	
}
