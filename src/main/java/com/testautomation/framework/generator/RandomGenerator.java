package com.testautomation.framework.generator;

import com.testautomation.framework.utils.DateAndTime;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Author Bharath
 * @Date 19-March-2018
 */

public class RandomGenerator 
{

	public static Random rand = new Random();
	public static DateAndTime dateAndTime = new DateAndTime();

	/*	To Generate Random Numbers of the Entered Length.*/
	public static String GenerateRandomNumber(int length)
	{
//		StringBuffer output = new StringBuffer(1000);
//		output.append("1");
//		for (int i = 1; i < length; i++)
//		{
//			output.append(0);
//		}
//		int num = Integer.parseInt(output.toString());
//		int maxnum = (num*10) - 1;
//
//		int randnum = num + rand.nextInt(maxnum) + 1;
//
//		String RandNum = new Integer(randnum).toString();
//		return RandNum;

		if (length > 18)
			throw new IllegalStateException("To many digits");
		long tLen = (long) Math.pow(10, length - 1) * 9;

		long number = (long) (Math.random() * tLen) + (long) Math.pow(10, length - 1) * 1;

		String randomValue = number + "";
		if (randomValue.length() != length) {
			throw new IllegalStateException("The random number '" + randomValue + "' is not '" + length + "' digits");
		}
		return randomValue;
	}

	/*	To Generate Random Capitalized Letters of the Entered Length.*/
	public static String GenerateRandomCapitalizedString(int length)
	{
		StringBuffer output = new StringBuffer(1000);
		output.append((char)(rand.nextInt(26) + 'A'));
		System.out.println(output.toString());

		for(int i = 1; i < 10; i++)
		{
			char c = (char)(rand.nextInt(26) + 'a');
			output.append(c);
		}
		String RandCapitalizedString = output.toString();
		return RandCapitalizedString;
	}

	/*	To Generate Random Small Letters of the Entered Length.*/
	public static String GenerateRandomSmallLetters(int length)
	{
		StringBuffer output = new StringBuffer(1000);
		output.append((char)(rand.nextInt(26) + 'a'));
		System.out.println(output.toString());

		for(int i = 1; i < 10; i++)
		{
			char c = (char)(rand.nextInt(26) + 'a');
			output.append(c);
		}
		String RandSmallLetters = output.toString();
		return RandSmallLetters;
	}
	/*	To Generate Random Capital Letters of the Entered Length.*/
	public static String GenerateRandomCapitalLetters(int length)
	{
		StringBuffer output = new StringBuffer(1000);
		output.append((char)(rand.nextInt(26) + 'A'));
		System.out.println(output.toString());

		for(int i = 1; i < 10; i++)
		{
			char c = (char)(rand.nextInt(26) + 'A');
			output.append(c);
		}
		String RandCapitalLetters = output.toString();

		return RandCapitalLetters;
	}

	/*	To Generate Random Alpha-Numeric Characters of the Entered Length.*/
	public static String GenerateRandomAlphaNumericCharacters(int length)
	{
		return RandomStringUtils.randomAlphanumeric(length).toString();
	}

	/*	To Generate Random ASCII Characters of the Entered Length.*/
	public static String GenerateRandomASCIICharacters(int length)
	{
		return RandomStringUtils.randomAscii(length).toString();
	}

	/*	To Generate Random E-Mail IDs(Auto Generate Domain Names.*/
	public static String GenerateRandomEMAILIDs() throws Exception{

		String EmailID = "autotester_"+RandomGenerator.GenerateRandomNumber(5) +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		//String EmailID = "random_" + dateAndTime.getDate() + dateAndTime.getTime();
		//String Domain = RandomStringUtils.randomAlphabetic(7).toLowerCase().toString();
		String Domain = "example";

		return EmailID + "@" + Domain + ".com";
	}

	/*	To Generate Random E-Mail IDs.*/
	public static String GenerateRandomEMAILIDs(String DomainName)
	{
		String EmailID = RandomStringUtils.randomAlphabetic(15).toString();

		return EmailID + "@" + DomainName ;
	}
}
