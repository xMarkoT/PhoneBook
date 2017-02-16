package com.phonebook.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

	private String rawFilePath;
	private String parsedFilePath;

	public static void main(String[] args) {
		String rawFilePath = "C:\\Users\\marko.trajcheski\\Desktop\\PhoneBook\\imenik.txt";
		String parsedFilePath = "C:\\Users\\marko.trajcheski\\Desktop\\PhoneBook\\parsedImenik.txt";
		PhoneBook pb = new PhoneBook(rawFilePath, parsedFilePath);

		pb.parseRawFile();

		// pb.printSortedMapByName();

		pb.addPhoneRecordToFile("REC1", "0888111111");
		pb.addPhoneRecordToFile("REC2", "00359888111111");
		pb.addPhoneRecordToFile("REC3", "+359888111111");
		pb.addPhoneRecordToFile("REC4", "0881111111");
		pb.addPhoneRecordToFile("REC5", "+355888111111");
		pb.addPhoneRecordToFile("REC6", "0888111111");
		pb.addPhoneRecordToFile("REC7", "0888111111");
		pb.addPhoneRecordToFile("REC8", "0888111111");

		// pb.printSortedMapByName();

		pb.removePhoneRecord("REC8");
		pb.removePhoneRecord("REC1");

		pb.printSortedMapByName();
	}

	public PhoneBook(String rawFilePath, String parsedFilePath) {
		this.rawFilePath = rawFilePath;
		this.parsedFilePath = parsedFilePath;
	}

	public String getPhoneNumberByName(String name) {
		return getNameToPhone().get(name);
	}

	public void printSortedMapByName() {
		List<String> keys = new ArrayList<>(getNameToPhone().keySet());
		keys.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		for (String key : keys) {
			System.out.println(key + ", " + getNameToPhone().get(key));
		}
	}

	public void printCallStatistic() {
		Set<Entry<String, Integer>> set = getPhoneToCall().entrySet();
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
		phone = phone.replaceAll("\\D+", "");

		if (p && phone.length() == 12 && phone.startsWith("359") && OPERATOR_CODES.contains(phone.substring(3, 5))
				&& SPECIAL_CHAR.contains(phone.charAt(5))) {
			return "+" + phone;
		}

		if (!p && phone.length() == 10 && '0' == phone.charAt(0) && OPERATOR_CODES.contains(phone.substring(1, 3))
				&& SPECIAL_CHAR.contains(phone.charAt(3))) {

			return "+359" + phone.substring(1);
		}

		if (!p && phone.length() == 14 && phone.startsWith("00") && "359".equals(phone.substring(2, 5))
				&& OPERATOR_CODES.contains(phone.substring(5, 7)) && SPECIAL_CHAR.contains(phone.charAt(7))) {
			return "+" + phone.substring(2);
		}

		return null;
	}

	public void parseRawFile() {
		File rawfile = new File(rawFilePath);
		File parsedFile = new File(parsedFilePath);
		try (BufferedReader br = new BufferedReader(new FileReader(rawfile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(parsedFile));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(SEPPARATOR);
				if (split.length > 1) {
					String name = split[0];
					String phone = split[1];
					String phoneNumber = validatePhone(phone);
					if (phoneNumber != null) {
						getNameToPhone().put(name, phoneNumber);
						getPhoneToCall().put(phoneNumber, 0);
						bw.write(name + SEPPARATOR + phoneNumber + "\r\n");
						bw.flush();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPhoneRecordToFile(String name, String number) {
		File file = new File(parsedFilePath);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
			if (!file.exists()) {
				file.createNewFile();
			}
			String phoneNumber = validatePhone(number);
			if (phoneNumber != null) {
				getNameToPhone().put(name, phoneNumber);
				getPhoneToCall().put(phoneNumber, 0);
				bw.write(name + SEPPARATOR + phoneNumber + "\r\n");
			}
			bw.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removePhoneRecord(String name) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(parsedFilePath)))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(SEPPARATOR);
				if (split[0].equalsIgnoreCase(name)) {
					getNameToPhone().remove(name);
					getPhoneToCall().remove(split[1]);
					continue;
				}
				sb.append(line);
				sb.append("\r\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(parsedFilePath), false))) {
			bw.write(sb.toString());
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Should be called from other class which manages calls, where a method
	 * will exist which checks the validity of the caller and the direction,
	 * then if it is outgoing call it will call this method
	 */
	public void outgoingCall(String number) {
		if (getPhoneToCall().containsKey(number)) {
			getPhoneToCall().put(number, getPhoneToCall().get(number) + 1);
		}
	}

	public String getRawFile() {
		return rawFilePath;
	}

	public void setRawFile(String rawFile) {
		this.rawFilePath = rawFile;
	}

	public String getParsedFile() {
		return parsedFilePath;
	}

	public void setParsedFile(String parsedFile) {
		this.parsedFilePath = parsedFile;
	}

	public Map<String, String> getNameToPhone() {
		return nameToPhone;
	}

	public void setNameToPhone(Map<String, String> nameToPhone) {
		this.nameToPhone = nameToPhone;
	}

	public Map<String, Integer> getPhoneToCall() {
		return phoneToCall;
	}

	public void setPhoneToCall(Map<String, Integer> phoneToCall) {
		this.phoneToCall = phoneToCall;
	}
}
