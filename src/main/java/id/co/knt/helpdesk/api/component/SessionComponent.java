package id.co.knt.helpdesk.api.component;

import id.co.knt.helpdesk.api.model.Login;
import id.co.knt.helpdesk.api.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionComponent {

    private final LoginRepository loginRepository;

    @Autowired
    public SessionComponent(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Scheduled(fixedRate = 3600000)
    public void kickOutActiveUser(){
        List<Login> logins = loginRepository.findAll();
        if (!logins.isEmpty()) {
            loginRepository.deleteAll();
        }
    }
}
