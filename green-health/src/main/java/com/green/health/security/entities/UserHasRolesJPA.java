package com.green.health.security.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_has_roles")
public class UserHasRolesJPA {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserSecurityJPA userSecurityJpa;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	@JoinColumn(name="role_id")
	private RoleJPA roleJpa;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSecurityJPA getUserSecurityJpa() {
		return userSecurityJpa;
	}

	public void setUserSecurityJpa(UserSecurityJPA userSecurityJpa) {
		this.userSecurityJpa = userSecurityJpa;
	}

	public RoleJPA getRoleJpa() {
		return roleJpa;
	}

	public void setRoleJpa(RoleJPA roleJpa) {
		this.roleJpa = roleJpa;
	}
	
	
}