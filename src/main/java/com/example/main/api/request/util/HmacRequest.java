package com.example.main.api.request.util;

public class HmacRequest {

	private String fullName;

	private String city;

	private String gender;

	private int amount;

	public int getAmount() {
		return amount;
	}

	public String getCity() {
		return city;
	}

	public String getFullName() {
		return fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "HmacRequest [fullName=" + fullName + ", city=" + city + ", gender=" + gender + ", amount=" + amount
				+ "]";
	}

}
