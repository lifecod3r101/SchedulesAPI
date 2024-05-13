package org.example.Controllers;

import org.example.Models.SchedulesRolesModel;
import org.example.Models.SchedulesTeamModel;
import org.example.Repositories.SchedulesTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/team")
public class SchedulesTeamController {
    @Autowired
    SchedulesTeamRepository teamRepository;

    @PostMapping("/add")
    public String addTeamMember(@RequestParam("teamMemberName") String memberName, @RequestParam("teamMemberEmail") String memberEmail, @RequestParam("teamMemberBirthDate") String memberBirthDate, @RequestParam("teamMemberPhoneNumber") String memberPhoneNumber) {
        SchedulesTeamModel schedulesTeamModel = new SchedulesTeamModel();
        schedulesTeamModel.setUserName(memberName);
        schedulesTeamModel.setUserEmail(memberEmail);
        schedulesTeamModel.setUserBirthDate(memberBirthDate);
        schedulesTeamModel.setUserPhoneNumber(memberPhoneNumber);
        teamRepository.save(schedulesTeamModel);
        return "Team member saved";
    }

    @GetMapping("/find/{userId}")
    public SchedulesTeamModel getTeamMember(@PathVariable("userId") String userId) {
        return teamRepository.findById(userId).get();
    }

    @PatchMapping("/update/{userId}")
    public String updateTeamMember(@PathVariable("userId") String userId, @RequestParam(name = "userBirthDate", required = false) String userBirthDate, @RequestParam(name = "userEmail", required = false) String userEmail, @RequestParam(name = "userName", required = false) String userName, @RequestParam(name = "userPhoneNumber", required = false) String userPhoneNumber) {
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
        return "Member updated";
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
