package de.hohenheim.sopranos.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Comparator;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


@MappedSuperclass
public abstract class DateClass {
	
	@CreatedDate
    private Timestamp createDate;
	@LastModifiedDate
    private Timestamp editDate;
    @ManyToOne
    private SopraUser editUser;

    public SopraUser getEditUser() {
		return editUser;
	}
	public void setEditUser(SopraUser editUser) {
		this.editUser = editUser;
	}
	public String getPostDateString(){
    	return DateFormat.getInstance().format(getCreateDate());
    }
	public Timestamp getEditDate() {
		return editDate;
	}
	public String getEditDateString(){
    	return DateFormat.getInstance().format(getEditDate());
    }
	public void setEditDate() {
		this.editDate = new Timestamp(System.currentTimeMillis());
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate() {
		this.createDate = new Timestamp(System.currentTimeMillis());
	}
	
}
