package org.example.Controllers;

import jakarta.validation.Valid;
import org.example.Models.SchedulesRolesModel;
import org.example.Repositories.SchedulesRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/add")
    public ResponseEntity<SchedulesRolesModel> addRole(@Valid @RequestParam("roleName") String roleName, @ModelAttribute SchedulesRolesModel rolesModel, BindingResult bindingResult) {
        SchedulesRolesModel schedulesRolesModel = new SchedulesRolesModel();
        schedulesRolesModel.setRoleName(roleName);
        rolesRepository.save(schedulesRolesModel);
        return ResponseEntity.status(HttpStatus.OK).body(schedulesRolesModel);
    }

    @GetMapping("/find/{roleId}")
    public SchedulesRolesModel findRole(@PathVariable("roleId") String roleId) {
        return rolesRepository.findById(roleId).get();
    }

    @PatchMapping("/update/{roleId}")
    public ResponseEntity<SchedulesRolesModel> updateRole(@PathVariable("roleId") String roleId, @RequestParam("roleName") String roleName) {
        SchedulesRolesModel rolesModel = rolesRepository.findById(roleId).get();
        rolesModel.setRoleName(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(rolesModel);
    }

    @DeleteMapping("/delete/{roleId}")
    public String removeRole(@PathVariable("roleId") String roleId) {
        rolesRepository.deleteById(roleId);
        return "Role deleted";
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
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = ((FieldError) error).getRejectedValue().toString();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}