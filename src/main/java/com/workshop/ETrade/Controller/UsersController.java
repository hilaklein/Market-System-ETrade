package com.workshop.ETrade.Controller;

import com.workshop.ETrade.Controller.Forms.AppointForm;
import com.workshop.ETrade.Controller.Forms.BidForm;
import com.workshop.ETrade.Controller.Forms.LoginForm;
import com.workshop.ETrade.Controller.Forms.SignUpForm;
import com.workshop.ETrade.Domain.Notifications.Notification;
import com.workshop.ETrade.Service.ResultPackge.Result;
import com.workshop.ETrade.Service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersController {

    @Autowired
    private ServiceInterface systemService;
    @Autowired
    private SimpMessagingTemplate smt;

//    @GetMapping("/onlinemembers")
//    public newResult<String> getOnlineMembers(@RequestHeader("Authorization") String userName) {
//        return systemService.getOnlineMembers(systemService.getOnline());
//    }

//    @GetMapping("/offlinemembers")
//    public newResult<String> getOfflineMembers(@RequestHeader("Authorization") String userName) {
//        return systemService.getOfflineMembers(userName);
//    }

//    public newResult<Boolean> hasAdmin(){
//        return systemService.hasAdmin();
//    }

    @PostMapping("/remove")
    public Result<Boolean> removeMember(@RequestHeader("Authorization") String userName, @RequestBody AppointForm form) {
        return systemService.removeMember(userName, form.appointee);
    }

    @GetMapping("/entersystem")
    public Result<String> enterSystem() {
        return systemService.enterSystem();
    }

    @GetMapping("/addsysmanager/{member}")
    public Result<Boolean> addSystemManager(String userName, @PathVariable("member") String managerToAdd) {
        return systemService.addSystemManager(userName, managerToAdd);
    }

    @GetMapping("/removesysmanager/{member}")
    public Result<Boolean> removeSystemManager(String userName, @PathVariable("member") String managerToRemove) {
        return systemService.removeSystemManager(userName, managerToRemove);
    }

    @GetMapping("/exitsystem")
    public Result<Boolean> exitSystem(String userName) {
        return systemService.exitSystem(userName);
    }

    @PostMapping("/signup")
    public Result<Boolean> signUp(@RequestHeader("Authorization") String username, @RequestBody SignUpForm form) {
        return systemService.signUp(username, form.email, form.password, form.firstName, form.lastName);
    }

    @PostMapping("/login")
    public Result<Boolean> login(@RequestHeader("Authorization") String userName, @RequestBody LoginForm form) {
        return systemService.login(userName, form.email, form.password);
    }

    @GetMapping("/mybids")
    public Result<List<BidForm>> userBids(@RequestHeader("Authorization") String userName){
        return systemService.userBids(userName);
    }

    @GetMapping("/messages")
    public Result<List<Notification>> getMessages(@RequestHeader("Authorization") String userName) {
        return systemService.getMessages(userName);
    }

    @GetMapping("/isadmin")
    public Result<Boolean> isAdmin(@RequestHeader("Authorization") String userName) {
        return systemService.isAdmin(userName);
    }

    @GetMapping("/onlinemembers")
    public Result<List<String>> onlineMembers(@RequestHeader("Authorization") String userName) {
        return systemService.getOnlineMembers(userName);
    }

    @GetMapping("/offlinemembers")
    public Result<List<String>> offlineMembers(@RequestHeader("Authorization") String userName) {
        return systemService.getOfflineMembers(userName);
    }

    @GetMapping("/logout")
    public Result<String> logOut(@RequestHeader("Authorization") String userName) {
        return systemService.logOut(userName);
    }

//    @GetMapping("/terminate/{user}")
//    public newResult<Boolean> adminTerminateUser(String adminName, @PathVariable("user") String userToTerminate) {
//        return systemService.adminTerminateUser(adminName, userToTerminate);
//    }

}
