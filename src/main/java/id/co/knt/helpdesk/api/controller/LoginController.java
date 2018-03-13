package id.co.knt.helpdesk.api.controller;

import id.co.knt.helpdesk.api.model.Login;
import id.co.knt.helpdesk.api.model.User;
import id.co.knt.helpdesk.api.service.LoginService;
import id.co.knt.helpdesk.api.service.UserService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

/**
 * @author Muhamad Nizar Iqbal
 */
@RestController
@RequestMapping(value = "/userManagement")
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Value("${userName}")
    String userName;

    @Value("${pass}")
    String pass;


    /**
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/loggingIn/", method = RequestMethod.POST)
    public ResponseEntity<Login> login(@RequestParam String userName, @RequestParam String password) {
        Date dt = new Date();
        DateTime dateTime = new DateTime(dt);
        dateTime = dateTime.plusHours(3);
        SecureRandom rand = new SecureRandom();

        /**
         * First check if the username and password are valid
         */
        User user = userService.validateUser(userName, password);
        Boolean isValid = user == null ? false : true;
        Login login = null;

        if (isValid) {
            login = loginService.findByUser(user);
   
            if (login == null) {
                return firstLogin(dt, rand, dateTime, user);
            } else {
                return new ResponseEntity<Login>(login, HttpStatus.OK);
            }
        }else{
        	return new ResponseEntity<Login>(login, HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value = "/createUser/", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createAdmin(@RequestBody User newUser) {
    	LOG.info("ResponseEntity<Boolean> createAdmin /createAdmin/");
		User adminUser = userService.findUserByUsername("a4m1n");
		User u = null;

		try {
			if (adminUser == null) {
				newUser.setUserName(userName);
				newUser.setName("admin");
				newUser.setCreatedDate(new Date());

				newUser.setPassword(pass);
				u = userService.registerUser(newUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return u != null ? new ResponseEntity<Boolean>(true, HttpStatus.OK)
				: new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Login> firstLogin(Date dt, SecureRandom rand, DateTime dateTime,
                                                                 User user) {
        Login newLogin = new Login();
        newLogin.setLoginDate(dt);
        newLogin.setToken(new BigInteger(130, rand).toString(50));
        newLogin.setTokenExpired(dateTime.getMillis());
        newLogin.setUser(user);

        return loginService.saveLogin(newLogin) != null
                ? new ResponseEntity<Login>(newLogin, HttpStatus.OK)
                : new ResponseEntity<Login>(newLogin, HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/loggedOut/", method = RequestMethod.POST)
    public ResponseEntity<Void> logout(@RequestParam String token) {
        HttpHeaders headers = new HttpHeaders();

        Login login = loginService.findByToken(token);
        if (login == null) {
            return new ResponseEntity<Void>(headers, HttpStatus.UNAUTHORIZED);
        }
        loginService.deleteToken(login);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
