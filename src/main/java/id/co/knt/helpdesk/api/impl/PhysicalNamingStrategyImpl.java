package id.co.knt.helpdesk.api.impl;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class PhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment contenxt){
        return new Identifier(addUnderScores(name.getText()), name.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addUnderScores(name.getText()), name.isQuoted());
    }

    protected static String addUnderScores(String name){
        final StringBuilder buf = new StringBuilder(name.replace('-', '_'));

        for (int i=1; i<buf.length() -1; i++){
            if (Character.isLowerCase(buf.charAt(i-1)) && Character.isUpperCase(buf.charAt(i)) && Character.isLowerCase(buf.charAt(i + 1))) {
                buf.insert(i++, '_');
            }
        }

        return buf.toString().toLowerCase(Locale.ROOT);
    }
}
