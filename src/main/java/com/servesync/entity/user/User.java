package com.servesync.entity.user;

import java.util.*;
import java.util.stream.Collectors;

import com.servesync.entity.address.Address;
import com.servesync.entity.base.BaseEntity;
import com.servesync.entity.role.Role;
import com.servesync.enums.RoleName;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "password_hash", nullable = false, length = 97)
	private String password;

	@Column(name = "profile_pic", length = 500)
	private String profilePic;

	// Bidirectional one-to-many relationship with Address
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }


	// Bidirectional many-to-many relationship with Role
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	// Helper method to add a role
	public void addRole(Role role) {
		if (role != null) {
			this.roles.add(role);
			role.getUsers().add(this); // Ensure bidirectional relationship
		}
	}

	// Helper method to remove a role
	public void removeRole(Role role) {
		if (role != null) {
			this.roles.remove(role);
			role.getUsers().remove(this); // Ensure bidirectional relationship
		}
	}


	// Helper method to clear roles
	public void clearRoles() {
		for (Role role : new HashSet<>(this.roles)) {
			removeRole(role);
		}
	}

	// Helper method to set roles
	public void setRoles(Set<Role> roles) {
		clearRoles(); // Clear existing roles
		if (roles != null) {
			for (Role role : roles) {
				addRole(role); // Add new roles
			}
		}
	}

	// get role names as a list
	public Set<String> getRoleNames() {
		return roles.stream()
        .map(role -> role.getRoleName().name())
        .collect(Collectors.toSet());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

	public User(long l, String string, String string2, Set<Role> roles2) {
		this.id = l;
		this.firstName = string;
		this.lastName = string2;
		this.roles = roles2;
	}

}
