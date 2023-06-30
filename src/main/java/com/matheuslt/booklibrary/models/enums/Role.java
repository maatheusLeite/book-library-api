package com.matheuslt.booklibrary.models.enums;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	ADMIN(
			Set.of(
					Permission.ADMIN_READ,
					Permission.ADMIN_CREATE,
					Permission.ADMIN_UPDATE,
					Permission.ADMIN_DELETE
			)
	),
	USER(
			Set.of(
					Permission.USER_READ,
					Permission.USER_CREATE,
					Permission.USER_UPDATE,
					Permission.USER_DELETE
			)
	);
	
	private Role(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	private final Set<Permission> permissions;

	public Set<Permission> getPermissions() {
		return permissions;
	}
	
	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList());
		
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return authorities;
	}
}
