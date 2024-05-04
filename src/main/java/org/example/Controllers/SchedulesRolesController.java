package org.example.Controllers;

import org.example.Models.SchedulesRolesModel;
import org.example.Models.SchedulesTeamRoleModel;
import org.example.Repositories.SchedulesRolesRepository;
import org.example.Repositories.SchedulesTeamRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class SchedulesRolesController {
    @Autowired
    SchedulesRolesRepository rolesRepository;
    @Autowired
    SchedulesTeamRolesRepository teamRolesRepository;

    @PostMapping("/add")
    public String addRole(@RequestParam("roleName") String roleName) {
        SchedulesRolesModel schedulesRolesModel = new SchedulesRolesModel();
        schedulesRolesModel.setRoleName(roleName);
        rolesRepository.save(schedulesRolesModel);
        return "Role added";
    }

    @GetMapping("/find/{roleId}")
    public SchedulesRolesModel findRole(@PathVariable("roleId") String roleId) {
        return rolesRepository.findById(roleId).get();
    }

    @PatchMapping("/update/{roleId}")
    public String updateRole(@PathVariable("roleId") String roleId, @RequestParam("roleName") String roleName) {
        SchedulesRolesModel rolesModel = rolesRepository.findById(roleId).get();
        rolesModel.setRoleName(roleName);
        return "Role updated";
    }

    @DeleteMapping("/delete/{roleId}")
    public String removeRole(@PathVariable("roleId") String roleId) {
        rolesRepository.deleteById(roleId);
        return "Role deleted";
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("userId") String userId, @RequestParam("roleId") String roleId) {
        SchedulesTeamRoleModel teamRoleModel = new SchedulesTeamRoleModel();
        teamRoleModel.setRoleId(roleId);
        teamRoleModel.setUserId(userId);
        teamRolesRepository.save(teamRoleModel);
        return "User assigned to role";
    }
}