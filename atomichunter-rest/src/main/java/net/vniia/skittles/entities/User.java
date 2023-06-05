package net.vniia.skittles.entities;

import net.vniia.skittles.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @Column(nullable = false, unique = true)
    public String login;
    private String password;
    private String fullName;
    @Column(name = "email")
    private String email;

    @JoinTable(
            name = "authorities",
            joinColumns = {@JoinColumn(name = "login")})
    @Column(name = "authority")
    private String role;

    public void update(UserDto userDto) {
        this.login = userDto.login;
        this.role = userDto.getRole();
        this.email = userDto.getEmail();
        this.fullName = userDto.getFullName();
    }
}
