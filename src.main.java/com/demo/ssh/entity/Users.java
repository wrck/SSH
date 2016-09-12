package com.demo.ssh.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "users")
public class Users extends BaseModel {

	@Column
	private Integer age;

	@Column
	private String realName;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "usersGroup_id")
	private UsersGroup usersGroup;

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the usersGroup
	 */
	public UsersGroup getUsersGroup() {
		return usersGroup;
	}

	/**
	 * @param usersGroup the usersGroup to set
	 */
	public void setUsersGroup(UsersGroup usersGroup) {
		this.usersGroup = usersGroup;
	}

}
