package org.example.Controllers;

import org.example.Models.SchedulesTeamModel;
import org.example.Repositories.SchedulesTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete/{userId}")
    public String removeTeamMember(@PathVariable("userId")String userId){
        teamRepository.deleteById(userId);
        return "Team member removed";
    }
}
