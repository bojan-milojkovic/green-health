package com.green.health.security.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class RoleJPA {

	@Id
	@Column(name="role_id")
	private Long roleId;
	
	@Column(name="role_name")
	private String roleName;
	
	@OneToMany(mappedBy="roleJpa")
	private List<UserHasRolesJPA> userHasRolesJpa = new ArrayList<UserHasRolesJPA>();

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<UserHasRolesJPA> getUserHasRolesJpa() {
		return userHasRolesJpa;
	}

	public void setUserHasRolesJpa(List<UserHasRolesJPA> userHasRolesJpa) {
		this.userHasRolesJpa = userHasRolesJpa;
	}
}