package com.oracle.igcs.hibernate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import oracle.jdbc.OracleClob;
import oracle.sql.CLOB;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vdawar
 *
 */
public class JsonDataUserType implements UserType {

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, gson.toJson(value, Map.class));
        }
    }

    @Override
    public Object deepCopy(Object originalValue) throws HibernateException {
        if (originalValue == null) {
            return null;
        }

        if (!(originalValue instanceof Map)) {
            return null;
        }

        Map<String, Object> resultMap = new HashMap<>();

        Map<?, ?> tempMap = (Map<?, ?>) originalValue;
        tempMap.forEach((key, value) -> resultMap.put((String) key, (Object) value));

        return resultMap;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    	
    	Clob o = (Clob) rs.getObject(names[0]);
        if (o != null) {
        	 String clobStr = o.getSubString(1, (int) o.length());
            return gson.fromJson(clobStr, Map.class);
        }

        return new HashMap<String, String>();
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object copy = deepCopy(value);

        if (copy instanceof Serializable) {
            return (Serializable) copy;
        }

        throw new SerializationException(String.format("Cannot serialize '%s', %s is not Serializable.", value, value.getClass()), null);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return 0;
        }

        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

    @Override
    public Class<?> returnedClass() {
        return Map.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

}
