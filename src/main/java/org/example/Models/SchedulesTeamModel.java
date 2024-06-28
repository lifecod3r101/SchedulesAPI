package org.example.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schedulesteammembers")
public class SchedulesTeamModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(name = "username")
    @NotBlank
    private String userName;
    @Column(name = "useremail")
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$", message = "Email must be in the format of example@example.com")
    private String userEmail;
    @Column(name = "userbirthdate")
    @NotBlank
    private String userBirthDate;
    @Column(name = "userphonenumber")
    @NotBlank
    private String userPhoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedulesteammembersmessages",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "messageid"))
    List<SchedulesMessageModel> messageModelList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedulesteammembersroles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<SchedulesRolesModel> roles = new HashSet<>();

    public Set<SchedulesRolesModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<SchedulesRolesModel> roles) {
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
