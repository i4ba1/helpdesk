package id.co.knt.helpdesk.api.ctrl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import id.co.knt.helpdesk.api.model.Login;
import id.co.knt.helpdesk.api.model.User;
import id.co.knt.helpdesk.api.service.LoginService;
import id.co.knt.helpdesk.api.service.UserService;

/**
 * @author Muhamad Nizar Iqbal
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/userManagement")
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    /**
     * Logging In and create new token for user
     *
     * @param objects
     * @return
     */
    @RequestMapping(value = "/loggingIn/", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> login(@RequestBody User newUser) {
        Date dt = new Date();
        DateTime dateTime = new DateTime(dt);
        dateTime = dateTime.plusHours(3);
        SecureRandom rand = new SecureRandom();
        /**
         * First check if the username and password are valid
         */
        User user = userService.validateUser(newUser.getUserName(),
                Base64.getEncoder().encodeToString(newUser.getPassword().getBytes()));
        Boolean isValid = user == null ? false : true;

        if (isValid) {
            Login login = loginService.findByUser(user);
   
            if (login == null) {
                return firstLogin(dt, rand, dateTime, user);
            } else {
                return new ResponseEntity<List<Map<String, Object>>>(new ArrayList<>(), HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<List<Map<String, Object>>>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }
    
    @RequestMapping(value = "/createUser/", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createAdmin(@RequestBody User newUser) {
    	LOG.info("ResponseEntity<Boolean> createAdmin /createAdmin/");
		User adminUser = userService.findUserByUsername("admin");
		User u = null;

		try {
			if (adminUser == null) {
				newUser.setUserName("a4m1n");
				newUser.setName("admin");
				newUser.setCreatedDate(new Date());
				String pass = "aDmin123!";

				newUser.setPassword(pass);
				u = userService.registerUser(newUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return u != null ? new ResponseEntity<Boolean>(true, HttpStatus.OK)
				: new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<List<Map<String, Object>>> firstLogin(Date dt, SecureRandom rand, DateTime dateTime,
                                                                 User user) {
        Login newLogin = new Login();
        newLogin.setLoginDate(dt);
        newLogin.setToken(new BigInteger(130, rand).toString(50));
        newLogin.setTokenExpired(dateTime.getMillis());
        newLogin.setUser(user);

        Map<String, Object> mapObj = new HashMap<String, Object>();
        mapObj.put("token", newLogin.getToken());
        mapObj.put("user", user);
        mapObj.put("type", "full-version");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        data.add(mapObj);

        return loginService.saveLogin(newLogin) != null
                ? new ResponseEntity<List<Map<String, Object>>>(data, HttpStatus.OK)
                : new ResponseEntity<List<Map<String, Object>>>(data, HttpStatus.NOT_FOUND);
    }

    /**
     * Logged out user
     *
     * @param objects
     * @return
     */
    @RequestMapping(value = "/loggedOut/", method = RequestMethod.POST)
    public ResponseEntity<Void> logout(@RequestBody Login newLogin) {
        HttpHeaders headers = new HttpHeaders();

        Login login = loginService.findByToken(newLogin.getToken());
        if (login == null) {
            return new ResponseEntity<Void>(headers, HttpStatus.UNAUTHORIZED);
        }
        loginService.deleteToken(login);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
