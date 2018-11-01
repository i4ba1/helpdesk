package id.co.knt.helpdesk.api.utilities;

import id.web.pos.integra.gawl.KNT_SN.Gawl;

import java.util.Map;

public class GeneratePasskey {
    public static void main(String[] args){
        Gawl gawl = new Gawl();
        try{
            Map<String, Byte> extractResult = gawl.extract("mhizkajydp");
            String passKey = gawl.pass(extractResult.get(Gawl.SEED1),extractResult.get(Gawl.SEED2));
            System.out.println(passKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
