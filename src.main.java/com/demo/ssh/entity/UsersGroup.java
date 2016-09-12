package com.demo.ssh.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "usergroup")
public class UsersGroup {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column
	private String name;

	@OneToMany(mappedBy = "usersGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Users.class)
	private List<Users> users;

	public UsersGroup(long i) {
		super();
		this.id = i;
	}

	public UsersGroup() {
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
