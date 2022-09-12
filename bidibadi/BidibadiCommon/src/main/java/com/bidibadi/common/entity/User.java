package com.bidibadi.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
public class User {

	@Id
	// @Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", roles=" + roles + "]";
	}

	@Column(length = 40, nullable = false, unique = true)

	private String email;
	@Column(length = 64, nullable = false)

	private String password;

	@Column(name = "first_name", length = 45, nullable = false)

	private String firstname;

	@Column(name = "last_name", length = 45, nullable = false)

	private String lastname;
	@Column(length = 64)

	private String photos;

	private boolean enabled;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public void addrole(Role role) {
		this.roles.add(role);

	}

	public User() {
	}

	public User(String email, String password, String firstname, String lastname) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public User(Integer id, String email, String password, String firstname, String lastname, String photos,
			boolean enabled, Set<Role> roles) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.photos = photos;
		this.enabled = enabled;
		this.roles = roles;
	}

	@Transient
	public String getPhotosImagePath() {

		if (id == null || photos == null)
			return "/images/foto.png";

		return "/user-photos/" + this.id + "/" + this.photos;
	}

}
