package org.example.Controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.example.Models.SchedulesRolesModel;
import org.example.Models.SchedulesTeamModel;
import org.example.Repositories.SchedulesRolesRepository;
import org.example.Repositories.SchedulesTeamRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/team")
public class SchedulesTeamController {
    @Autowired
    SchedulesTeamRepository teamRepository;
    @Autowired
    SchedulesRolesRepository rolesRepository;

    @PostMapping("/add")
    public ResponseEntity<SchedulesTeamModel> addTeamMember(@Valid @RequestParam("teamMemberName") String memberName, @RequestParam("teamMemberEmail") String memberEmail, @RequestParam("teamMemberBirthDate") String memberBirthDate, @RequestParam("teamMemberPhoneNumber") String memberPhoneNumber, @RequestParam("teamMemberRoles") String[] memberRoles, @ModelAttribute SchedulesTeamModel teamModel, BindingResult bindingResult) {
        SchedulesTeamModel schedulesTeamModel = new SchedulesTeamModel();
        schedulesTeamModel.setUserName(memberName);
        schedulesTeamModel.setUserEmail(memberEmail);
        schedulesTeamModel.setUserBirthDate(memberBirthDate);
        schedulesTeamModel.setUserPhoneNumber(memberPhoneNumber);
        for (String memberRoleEntry : memberRoles) {
            if(rolesRepository.findById(memberRoleEntry).isPresent()) {
                SchedulesRolesModel schedulesRolesModel = rolesRepository.findById(memberRoleEntry).get();
                schedulesTeamModel.getRoles().add(schedulesRolesModel);
            }
        }
        teamRepository.save(schedulesTeamModel);
        return ResponseEntity.status(HttpStatus.OK).body(schedulesTeamModel);
    }

    @GetMapping("/find/{userId}")
    public SchedulesTeamModel getTeamMember(@PathVariable("userId") String userId) {
        return teamRepository.findById(userId).get();
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<SchedulesTeamModel> updateTeamMember(@PathVariable("userId") String userId, @RequestParam(name = "userBirthDate", required = false) String userBirthDate, @RequestParam(name = "userEmail", required = false) String userEmail, @RequestParam(name = "userName", required = false) String userName, @RequestParam(name = "userPhoneNumber", required = false) String userPhoneNumber) {
        SchedulesTeamModel teamModel = teamRepository.findById(userId).get();
        if (userBirthDate != null) {
            teamModel.setUserBirthDate(userBirthDate);
        }
        if (userEmail != null) {
            teamModel.setUserEmail(userEmail);
        }
        if (userName != null) {
            teamModel.setUserName(userName);
        }
        if (userPhoneNumber != null) {
            teamModel.setUserPhoneNumber(userPhoneNumber);
        }
        teamRepository.save(teamModel);
        return ResponseEntity.status(HttpStatus.OK).body(teamModel);
    }

    @DeleteMapping("/delete/{userId}")
    public String removeTeamMember(@PathVariable("userId") String userId) {
        teamRepository.deleteById(userId);
        return "Team member removed";
    }

    @GetMapping("/getAll")
    public List<SchedulesTeamModel> getAllTeamMembers() {
        List<SchedulesTeamModel> teamMemberModelsList = new ArrayList<>();
        for (SchedulesTeamModel model : teamRepository.findAll()) {
            teamMemberModelsList.add(model);
        }
        return teamMemberModelsList;
    }

}
