package com.phonebook.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author marko.trajcheski
 * 
 */

public class PhoneBook {
	
	private final String SEPPARATOR = ", ";
	private final List<String> OPERATOR_CODES = Arrays.asList("87", "88", "89");
	private final List<Character> SPECIAL_CHAR = Arrays.asList('2', '3', '4', '5', '6', '7', '8', '9');

	private Map<String, String> nameToPhone = new HashMap<>();
	private Map<String, Integer> phoneToCall = new HashMap<>();

	private static String rawFile;
	private static String parsedFile;
	

	public String getPhoneNumberByName(String name) {
		return nameToPhone.get(name);
	}

	public void removeNumber(String parsedFile, String name) {
		phoneBookDataOperatoins(parsedFile, name, null, true);
	}

	public void addNumber(String parsedFile, String name, String number) {
		phoneBookDataOperatoins(parsedFile, name, number, false);
	}
	
	public void printSortedMapByName() {
		List<String> keys = new ArrayList<>(nameToPhone.keySet());
		keys.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		for (String key : keys) {
			System.out.println(key + ", " + nameToPhone.get(key));
		}
	}
	
	public void printCallStatistic() {
		Set<Entry<String, Integer>> set = phoneToCall.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		int limit = 5;
		for (Map.Entry<String, Integer> entry : list) {
			if (limit <= 0) {
				break;
			}
			System.out.println(entry.getKey() + " : " + entry.getValue());
			limit--;
		}
	}
	
	private String validatePhone(String phone) {
		boolean p = phone.toString().startsWith("+");
		phone = phone .replaceAll("\\D+", "");
		
		if(p && phone.length() == 12 && phone.startsWith("359") && OPERATOR_CODES.contains(phone.substring(3, 5))
				&& SPECIAL_CHAR.contains(phone.charAt(5))){
			return phone;
		}
		
		if(!p && phone.length() == 10 && '0' == phone.charAt(0)
				&& OPERATOR_CODES.contains(phone.substring(1, 3)) && SPECIAL_CHAR.contains(phone.charAt(3))){
			
			return "+359"+phone.substring(1);
		}
		
		if(!p && phone.length() == 14 && phone.startsWith("00") && "359".equals(phone.substring(2, 5))
				&& OPERATOR_CODES.contains(phone.substring(5, 7)) && SPECIAL_CHAR.contains(phone.charAt(7))){
			return "+" + phone.substring(2);
		}
		
//		return (p && phone.length() == 12 && phone.startsWith("359") && OPERATOR_CODES.contains(phone.substring(3, 5))
//				&& SPECIAL_CHAR.contains(phone.charAt(5)))
//				|| (!p && phone.length() == 10 && '0' == phone.charAt(0)
//						&& OPERATOR_CODES.contains(phone.substring(1, 3)) && SPECIAL_CHAR.contains(phone.charAt(3)))
//				|| (!p && phone.length() == 14 && phone.startsWith("00") && "359".equals(phone.substring(2, 5))
//						&& OPERATOR_CODES.contains(phone.substring(5, 7)) && SPECIAL_CHAR.contains(phone.charAt(7)));
		return null;
	}
	

	public void parseRawFile(String rawFile, String parsedFile) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(new File(rawFile)));
			bw = new BufferedWriter(new FileWriter(new File(parsedFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(SEPPARATOR);
				if (split.length > 1) {
					String name = split[0];
					String phone = split[1];
					if (validatePhone(phone) != null) {
						nameToPhone.put(name, validatePhone(phone));
						phoneToCall.put(validatePhone(phone), 0);
						bw.write(name + SEPPARATOR + phone);
						bw.newLine();
						bw.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bw != null) {
				try {
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void phoneBookDataOperatoins(String parsedFile, String name, String number, boolean removeRecord) {

		BufferedReader br = null;
		BufferedWriter bw = null;
		StringBuilder sb = new StringBuilder();

		try {
			br = new BufferedReader(new FileReader(new File(parsedFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(SEPPARATOR);
				if (removeRecord && split[0].equalsIgnoreCase(name)) {
					nameToPhone.remove(name);
					phoneToCall.remove(split[1]);
					continue;
				}
				sb.append(line);
				sb.append("\r\n");
			}
			if (removeRecord == false && validatePhone(number) != null) {
				sb.append(name + SEPPARATOR + validatePhone(number));
				sb.append("\r\n");
				nameToPhone.put(name, number);
				phoneToCall.put(validatePhone(number), 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			bw = new BufferedWriter(new FileWriter(new File(parsedFile), false));
			bw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Should be called from other class which manages calls, where a method will exist which 
	 * checks the validity of the caller and the direction, then if it is outgoing call it will call this method
	 */
	public void outgoingCall(String number) {
		if (phoneToCall.containsKey(number)) {
			phoneToCall.put(number, phoneToCall.get(number) + 1);
		}
	}

	

	

	public String getRawFile() {
		return rawFile;
	}

	public void setRawFile(String rawFile) {
		this.rawFile = rawFile;
	}

	public String getParsedFile() {
		return parsedFile;
	}

	public void setParsedFile(String parsedFile) {
		this.parsedFile = parsedFile;
	}
}
