package com.oracle.igcs.hibernate;

import org.hibernate.dialect.Oracle12cDialect;

import java.sql.Types;

/**
 * @author vdawar
 *
 */
public class CustomOracleDialect extends Oracle12cDialect {

    public CustomOracleDialect() {
        //this.registerColumnType(Types.JAVA_OBJECT, "blob");
    }
}
