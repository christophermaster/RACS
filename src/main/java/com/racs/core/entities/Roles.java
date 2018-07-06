package com.racs.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase entidad que representa un rol de una aplicación, además de una
 * entidad que se relaciona con los usuarios.
 * 
 * @author team disca
 */

@Entity
//@Table(name = "roles_users", uniqueConstraints = @UniqueConstraint(columnNames = { "aplication_client_id", "name" }))
@Table(name = "roles")
public class Roles implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5423416466129355042L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "enabled")
	private Boolean enabled;

	@Temporal(TemporalType.DATE)
	@Column (name="creation_date")
	private Date creationDate;

	@Temporal(TemporalType.DATE)
	@Column (name="last_update_date")
	private Date lastUpdateDate;
	
	@Column(name = "creator_user" )
	private String creatorUser;
	
	@Column(name = "last_user_updater")
	private String lastUserUpdater;

	/*@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "roleApp")
	private List<FunctionalityRole> functionalityRoles = new ArrayList<>();*/

	@ManyToMany(mappedBy = "rols")
	private List<User> users = new ArrayList<>();

	// Gestion de la relacion *ToMany
	/*public void addFunctionalityRole(FunctionalityRole functionalityRole) {
		functionalityRoles.add(functionalityRole);
		functionalityRole.setRoleApp(this);
	}

	public void removeFunctionalityRole(FunctionalityRole functionalityRole) {
		functionalityRoles.remove(functionalityRole);
		functionalityRole.setRoleApp(null);
	}*/

	public void addUserSso(User user) {
		users.add(user);
		user.getRols().add(this);
	}

	public void removeUserSso(User user) {
		users.remove(user);
		user.getRols().remove(this);
	}

	// Constructores
	public Roles() {
		
	}
	
	
	public Roles(Long id, String name, String type, Boolean enabled, Date creationDate, Date lastUpdateDate,
			String creatorUser, String lastUserUpdater, List<User> users) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.enabled = enabled;
		this.creationDate = creationDate;
		this.lastUpdateDate = lastUpdateDate;
		this.creatorUser = creatorUser;
		this.lastUserUpdater = lastUserUpdater;
		this.users = users;
	}
	
	//Getter y Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(String creatorUser) {
		this.creatorUser = creatorUser;
	}

	public String getLastUserUpdater() {
		return lastUserUpdater;
	}

	public void setLastUserUpdater(String lastUserUpdater) {
		this.lastUserUpdater = lastUserUpdater;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleUser [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append(", creationDate=");
		builder.append(creationDate);
		builder.append(", lastUpdateDate=");
		builder.append(lastUpdateDate);
		builder.append(", creatorUser=");
		builder.append(creatorUser);
		builder.append(", lastUserUpdater=");
		builder.append(lastUserUpdater);
		builder.append(", users=");
		builder.append(users);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((creatorUser == null) ? 0 : creatorUser.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((lastUserUpdater == null) ? 0 : lastUserUpdater.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Roles))
			return false;
		Roles other = (Roles) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (creatorUser == null) {
			if (other.creatorUser != null)
				return false;
		} else if (!creatorUser.equals(other.creatorUser))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		if (lastUserUpdater == null) {
			if (other.lastUserUpdater != null)
				return false;
		} else if (!lastUserUpdater.equals(other.lastUserUpdater))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}
