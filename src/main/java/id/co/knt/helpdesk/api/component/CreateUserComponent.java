package id.co.knt.helpdesk.api.component;

import id.co.knt.helpdesk.api.model.User;
import id.co.knt.helpdesk.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class CreateUserComponent {

    @Autowired
    private UserService userService;

    @Value("${userName}")
    String userName;

    @Value("${pass}")
    String pass;

    @PostConstruct
    public void createUser(){
        User user =userService.findUserByUsername("a4m1n");

        if (user == null) {
            User newUser = new User();
            newUser.setUserName(userName);
            newUser.setName("admin");
            newUser.setCreatedDate(new Date());

            newUser.setPassword(pass);
            userService.registerUser(newUser);
        }
    }
}
