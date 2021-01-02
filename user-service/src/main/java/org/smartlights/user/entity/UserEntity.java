package org.smartlights.user.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.PasswordType;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@UserDefinition
@NamedQueries({
        @NamedQuery(name = "getUserByUsername", query = "select user from UserEntity user where user.username=:username")
})
public class UserEntity extends PanacheEntity {

    @Username
    @Column(nullable = false, unique = true)
    private String username;

    @Password(PasswordType.MCF)
    @Column(nullable = false)
    private String password;

    @Email
    @Column
    private String email;

    @Roles
    @Column(nullable = false)
    private String role;

    @Version
    private int version;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String role) {
        this.username = username;
        setPassword(password);
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = BcryptUtil.bcryptHash(password);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean equals(Object object) {
        if (!(object instanceof UserEntity))
            return false;
        if (this == object)
            return true;

        UserEntity user = (UserEntity) object;
        return id.equals(user.id)
                && email.equals(user.email)
                && password.equals(user.password)
                && role.equals(user.role);
    }

    public int hashCode() {
        return Objects.hash(id, username, email, password, role);
    }
}
