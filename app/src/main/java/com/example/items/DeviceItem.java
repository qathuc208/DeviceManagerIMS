package com.example.items;

public class DeviceItem {

    public String imei;
    public String name_of_model;
    public String owner;
    public String avatar_of_owner;
    public String day_of_owner;
    public String is_borrow;
    public String borrower;
    public String avatar_of_borrower;
    public String day_of_borrow;
    public String is_lost;
    public String day_of_lost;
    
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }  
    
    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public String getName_of_model() {
        return name_of_model;
    }
    public void setName_of_model(String name_of_model) {
        this.name_of_model = name_of_model;
    }   
    public String getAvatar_of_owner() {
        return avatar_of_owner;
    }
    public void setAvatar_of_owner(String avatar_of_owner) {
        this.avatar_of_owner = avatar_of_owner;
    }
    public String getDay_of_owner() {
        return day_of_owner;
    }
    public void setDay_of_owner(String day_of_owner) {
        this.day_of_owner = day_of_owner;
    }
    public String getIs_borrow() {
        return is_borrow;
    }
    public void setIs_borrow(String is_borrow) {
        this.is_borrow = is_borrow;
    }
    
    public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getAvatar_of_borrower() {
        return avatar_of_borrower;
    }
    public void setAvatar_of_borrower(String avatar_of_borrower) {
        this.avatar_of_borrower = avatar_of_borrower;
    }
    public String getIs_lost() {
        return is_lost;
    }
    public void setIs_lost(String is_lost) {
        this.is_lost = is_lost;
    }
    public String getDay_of_lost() {
        return day_of_lost;
    }
    public void setDay_of_lost(String day_of_lost) {
        this.day_of_lost = day_of_lost;
    }
    public String getDay_of_borrow() {
        return day_of_borrow;
    }
    public void setDay_of_borrow(String day_of_borrow) {
        this.day_of_borrow = day_of_borrow;
    }
	public DeviceItem(String imei, String name_of_model, String owner,
			String avatar_of_owner, String day_of_owner, String is_borrow,
			String borrower, String avatar_of_borrower, String day_of_borrow,
			String is_lost, String day_of_lost) {
		super();
		this.imei = imei;
		this.name_of_model = name_of_model;
		this.owner = owner;
		this.avatar_of_owner = avatar_of_owner;
		this.day_of_owner = day_of_owner;
		this.is_borrow = is_borrow;
		this.borrower = borrower;
		this.avatar_of_borrower = avatar_of_borrower;
		this.day_of_borrow = day_of_borrow;
		this.is_lost = is_lost;
		this.day_of_lost = day_of_lost;
	}
   
    
    
    
  
    
    
    
}
