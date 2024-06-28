package org.example.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schedulesroles")
public class SchedulesRolesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roleId;

    @Column(name = "rolename")
    @NotBlank(message = "Sorry. This is required")
    @NotNull(message = "Sorry. This is required")
    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedulesteammembersroles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<SchedulesTeamModel> team = new HashSet<>();

    public Set<SchedulesTeamModel> getTeam() {
        return team;
    }

    public void setTeam(Set<SchedulesTeamModel> team) {
        this.team = team;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
