package id.co.knt.helpdesk.api.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingError {
    public static void writeError(String error){
        try{
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dt = new Date();
            String fileName = "Error-"+ft.format(dt);;
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.home")+ File.separator+fileName));
            writer.write(error);

            writer.close();
        }catch (Exception e){

        }
    }
}
