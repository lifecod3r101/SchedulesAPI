package org.example.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "schedulesroles")
public class SchedulesRolesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roleId;

    @Column(name = "rolename")
    @NotBlank
    private String roleName;

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
