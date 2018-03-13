package id.co.knt.helpdesk.api.aspect;

import id.co.knt.helpdesk.api.service.LoginService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;

//@Aspect
//@Component
public class UserAuthorizationAspect {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthorizationAspect.class);

    @Autowired
    private LoginService loginRepo;

    @Around("execution(* id.co.knt.helpdesk.api.controller.ProductController.*(..)) || execution(* id.co.knt.helpdesk.api.controller.SNManagementController.*(..))")
    public Object admTeacher(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        HttpHeaders headers = new HttpHeaders();
        Object result = null;
        if (authorizing(objects) > 0) {
            result = new ResponseEntity<Void>(headers, HttpStatus.UNAUTHORIZED);
        } else {
            result = joinPoint.proceed();
        }

        return result;
    }

    private int authorizing(Object[] obj) {
        int result = 0;
        JSONArray array = null;
        String token = "";

        try {
            if (obj.length == 0) {
                array = new JSONArray(Arrays.asList(obj));
                token = array.getJSONArray(0).getJSONObject(0).get("authorization").toString();
                LOG.info("Token: " + token);

                if (token.equals("") || !loginRepo.validateToken(token, new Date().getTime())) {
                    result = 1;
                }
            } else {
                array = new JSONArray(Arrays.asList(obj[0]));
                token = array.getJSONArray(0).getJSONObject(0).get("authorization").toString();
                if (token.equals("") || !loginRepo.validateToken(token, new Date().getTime())) {
                    result = 1;
                }
            }
        } catch (JSONException e) {
            if (obj.length == 0 || obj[0].equals("")
                    || !loginRepo.validateToken(obj[0].toString(), new Date().getTime())) {
                result = 1;
            }
        }

        return result;
    }
}

