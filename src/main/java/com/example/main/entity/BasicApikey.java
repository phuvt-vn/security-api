package com.example.main.entity;

import java.time.LocalDateTime;



import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BasicApikey {

	@Id
	private int apikeyId;

	private int userId;

	private String apikey;

	private LocalDateTime expiredAt;

	public int getApikeyId() {
		return apikeyId;
	}

	public void setApikeyId(int apikeyId) {
		this.apikeyId = apikeyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

}
