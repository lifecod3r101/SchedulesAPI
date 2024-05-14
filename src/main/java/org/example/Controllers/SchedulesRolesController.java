package org.example.Controllers;

import jakarta.validation.ConstraintViolationException;
import org.example.Models.SchedulesRolesModel;
import org.example.Models.SchedulesTeamRoleModel;
import org.example.Repositories.SchedulesRolesRepository;
import org.example.Repositories.SchedulesTeamRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class SchedulesRolesController {
    @Autowired
    SchedulesRolesRepository rolesRepository;
    @Autowired
    SchedulesTeamRolesRepository teamRolesRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addRole(@RequestParam("roleName") String roleName) {
        SchedulesRolesModel schedulesRolesModel = new SchedulesRolesModel();
        schedulesRolesModel.setRoleName(roleName);
        List<String> errors = new ArrayList<String>();
        try {
            rolesRepository.save(schedulesRolesModel);
            return ResponseEntity.ok("Role added");
        } catch (ConstraintViolationException e) {
            System.out.println(e.getConstraintViolations());
            return ResponseEntity.ok("Something went wrong. Try again later");
        }
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

    @GetMapping("/getAll")
    public List<SchedulesRolesModel> getAllRoles() {
        List<SchedulesRolesModel> rolesModelsList = new ArrayList<>();
        for (SchedulesRolesModel model : rolesRepository.findAll()) {
            rolesModelsList.add(model);
        }
        return rolesModelsList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}