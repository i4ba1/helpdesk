package id.co.knt.smartbee.controller;

import id.co.knt.smartbee.dao.WorkbenchDao;
import id.co.knt.smartbee.entity.WorkBench;
import id.web.pos.integra.gawl.Gawl;
import id.web.pos.integra.gawl.Gawl.UnknownCharacterException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping({"/l"})
public class Checker
{
  public static String CHECK_SUCCESS = "0";
  public static String CHECK_FAILED_REGISTRATION = "1";
  public static String CHECK_FAILED_ACTIVATION = "2";
  public static String REGISTER_SUCCESS = "0";
  public static String REGISTER_DUPLICATE_MODULE = "1";
  public static String REGISTER_INVALID_SERIAL = "2";
  public static String ACTIVATION_SUCCESS = "0";
  public static String ACTIVATION_FAILED = "1";
  public static char SEPARATOR = ':';
  private Gawl gawl;
  private WorkbenchDao workbenchDao;
  
  public Checker() {}
  
  @Inject
  public void setGawl(Gawl gawl) { this.gawl = gawl; }
  
  @Inject
  public void setWorkbenchDao(WorkbenchDao workbenchDao)
  {
    this.workbenchDao = workbenchDao;
  }
  
  @RequestMapping({"/hello"})
  @ResponseBody
  public String hello() {
    return "hello";
  }
  





  @RequestMapping(value={"/c/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public String check(@PathVariable int id)
  {
    String result = CHECK_SUCCESS;
    try {
      WorkBench w = workbenchDao.findByModule(Integer.valueOf(id));
      if (gawl.validate(w.getSerial())) {
        Map<String, Byte> info = gawl.extract(w.getSerial());
        Byte module = (Byte)info.get("module");
        if ((!module.equals(Integer.valueOf(63))) && (Byte.toUnsignedInt(module.byteValue()) == id))
        {
          String passKey = gawl.pass(((Byte)info.get("seed1")).byteValue(), ((Byte)info.get("seed2")).byteValue());
          if (w.getActivationKey() == null) {
            result = CHECK_FAILED_ACTIVATION + SEPARATOR + passKey;
          }
          else if (!gawl.challenge(passKey, w.getActivationKey()))
          {
            result = CHECK_FAILED_ACTIVATION + SEPARATOR + passKey;
          }
        }
        else
        {
          result = CHECK_FAILED_REGISTRATION;
          System.out.println("unmatched serial number to module");
        }
      }
      else {
        result = CHECK_FAILED_REGISTRATION;
        System.out.println("invalid serial number");
      }
    } catch (Throwable e) {
      result = CHECK_FAILED_REGISTRATION;
    }
    return result;
  }
  





  @RequestMapping(value={"/r/{id}/{serial}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public String register(@PathVariable int id, @PathVariable String serial)
  {
    String result = REGISTER_SUCCESS;
    if (gawl.validate(serial)) {
      try
      {
        Map<String, Byte> info = gawl.extract(serial);
        Byte module = (Byte)info.get("module");
        if ((!module.equals(Integer.valueOf(63))) && (Byte.toUnsignedInt(module.byteValue()) == id))
        {
          String passKey = gawl.pass(((Byte)info.get("seed1")).byteValue(), ((Byte)info.get("seed2")).byteValue());
          result = REGISTER_SUCCESS + SEPARATOR + passKey;
          WorkBench bench = new WorkBench();
          bench.setModule(Integer.valueOf(id));
          bench.setSerial(serial);
          bench.setRegisteredDate(new Date());
          workbenchDao.save(bench);
        }
        else {
          result = CHECK_FAILED_REGISTRATION;
        }
      }
      catch (Gawl.UnknownCharacterException e) {
        result = CHECK_FAILED_REGISTRATION;
        System.out.println("register failed due to invalid serial number");
      }
      
    } else {
      result = CHECK_FAILED_REGISTRATION;
    }
    return result;
  }
  





  @RequestMapping(value={"/a/{id}/{activationKey}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public String activate(@PathVariable int id, @PathVariable String activationKey)
  {
    String result = ACTIVATION_SUCCESS;
    try {
      System.out.println("id:" + id);
      WorkBench b = workbenchDao.findByModule(Integer.valueOf(id));
      Map<String, Byte> info = gawl.extract(b.getSerial());
      String passKey = gawl.pass(((Byte)info.get("seed1")).byteValue(), ((Byte)info.get("seed2")).byteValue());
      if (!gawl.challenge(passKey, activationKey)) {
        result = ACTIVATION_FAILED;
        System.out.println("invalid activationKey");
      } else {
        b.setActivationKey(activationKey);
        workbenchDao.save(b);
      }
    } catch (Throwable t) {
      result = ACTIVATION_FAILED;
      t.printStackTrace();
    }
    return result;
  }
  
  @RequestMapping({"/list"})
  @ResponseBody
  public String list() {
    StringBuilder result = new StringBuilder();
    List<WorkBench> list = workbenchDao.list();
    for (WorkBench b : list) {
      result.append(b.getModule()).append(':').append(b.getSerial()).append(':').append(b.getActivationKey()).append("\r\n");
    }
    return result.toString();
  }
  
  @RequestMapping({"/bye/{name}"})
  @ResponseBody
  public String bye(@PathVariable String name) {
    StringBuilder result = new StringBuilder();
    if (name.equals("4321")) {
      List<WorkBench> list = workbenchDao.list();
      for (WorkBench b : list) {
        workbenchDao.delete(b);
        result.append("bye ").append(b.getId()).append("\r\n");
      }
    }
    else {
      result.append("who 're you ").append(name).append('?');
    }
    return result.toString();
  }
}