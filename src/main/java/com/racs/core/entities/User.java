package com.racs.core.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase entidad usuario que se requiere autenticar o validar
 * con el modulo sso. Representa el usuario que recibira acceso a aplicaciones.
 * 
 * @author team disca
 */

@Entity
@Table(name = "users")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3181104013651339465L;

	@Id
	@GeneratedValue (strategy= GenerationType.AUTO)
	private Long id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name = "firstname" )
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "enabled")
	private Boolean enabled;

	@Temporal(TemporalType.DATE)
	@Column (name="user_active_until")
	@DateTimeFormat(pattern="dd/MM/yyyy")
    private Date userActiveUntil;
	
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

	 @Column (name="token")
	private String token;

	@Column (name="token_date_validate")
	private Date tokenDateValidate;

	//Relaciones de entidades
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "users_roles",  joinColumns = @JoinColumn(name = "user_id"),
    	    inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonManagedReference
    private Set<Roles> rols = new HashSet<>();

	//Gestion de la relacion *ToMany
	public void addRoleApp(Roles roleApp) {
		rols.add(roleApp);
		roleApp.getUsers().add(this);
	}

    public void removeRoleApp(Roles roleApp) {
        rols.remove(roleApp);
        roleApp.getUsers().remove(this);
    }

    public Boolean isAdmin(){
		for (Roles role: rols) {
			if(role.getName().equals("ADMIN_SSO")){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	//CONSTRUCTORES
	public User() {
	}
	
	public User(Long id) {
		this.id = id;
	}
	
	public User(User user) {
		this.id = user.id;
		this.username = user.username;
		this.password = user.password;
		this.firstname = user.firstname;
		this.lastname = user.lastname;
		this.email = user.email;
		this.enabled = user.enabled;
		this.userActiveUntil = user.userActiveUntil;
		this.lastUpdateDate = user.lastUpdateDate;
		this.lastUserUpdater = user.lastUserUpdater;
		this.creationDate = user.creationDate;
		this.creatorUser = user.creatorUser;	
		this.rols = user.rols;
		this.token = user.token;
		this.tokenDateValidate = user.tokenDateValidate;
	}

	//Getter y Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getUserActiveUntil() {
		return userActiveUntil;
	}

	public void setUserActiveUntil(Date userActiveUntil) {
		this.userActiveUntil = userActiveUntil;
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

	public Set<Roles> getRols() {
		return rols;
	}

	public void setRols(Set<Roles> roles) {
		this.rols = roles;
	}
	
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenDateValidate() {
		return tokenDateValidate;
	}

	public void setTokenDateValidate(Date tokenDateValidate) {
		this.tokenDateValidate = tokenDateValidate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", firstname=");
		builder.append(firstname);
		builder.append(", lastname=");
		builder.append(lastname);
		builder.append(", email=");
		builder.append(email);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append(", userActiveUntil=");
		builder.append(userActiveUntil);
		builder.append(", creationDate=");
		builder.append(creationDate);
		builder.append(", lastUpdateDate=");
		builder.append(lastUpdateDate);
		builder.append(", creatorUser=");
		builder.append(creatorUser);
		builder.append(", lastUserUpdater=");
		builder.append(lastUserUpdater);
		builder.append(", roles=");
		builder.append(rols);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((creatorUser == null) ? 0 : creatorUser.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((lastUserUpdater == null) ? 0 : lastUserUpdater.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((rols == null) ? 0 : rols.hashCode());
		result = prime * result + ((userActiveUntil == null) ? 0 : userActiveUntil.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
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
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
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
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (rols == null) {
			if (other.rols != null)
				return false;
		} else if (!rols.equals(other.rols))
			return false;
		if (userActiveUntil == null) {
			if (other.userActiveUntil != null)
				return false;
		} else if (!userActiveUntil.equals(other.userActiveUntil))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	

}
