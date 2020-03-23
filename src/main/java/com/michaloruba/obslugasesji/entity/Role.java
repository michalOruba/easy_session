package com.michaloruba.obslugasesji.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotNull(message = "this field is required")
	@Size(min = 1, message = "this field is required")
	@Column(name = "name")
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH,
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REFRESH
	})
	@JoinTable(name = "users_roles",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Collection<User> users;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public Role(String name, Collection<User> users) {
		this(name);
		this.users = users;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> roles) {
		this.users = roles;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", name='" + name + '\'' +
				", users=" + users +
				'}';
	}
}
