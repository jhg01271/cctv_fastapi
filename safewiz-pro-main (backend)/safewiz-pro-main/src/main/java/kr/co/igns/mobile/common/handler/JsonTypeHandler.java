package kr.co.igns.mobile.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    private static final Gson gson = new Gson();
    private Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.toObject(rs.getString(columnName), clazz);  
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.toObject(rs.getString(columnIndex), clazz);  
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.toObject(cs.getString(columnIndex), clazz); 
    }

    private String toJson(T object) {
        try {
            return gson.toJson(object); 
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

    private T toObject(String content, Class<?> clazz) {
        if (content != null && !content.isEmpty()) {
            try {
                return (T) gson.fromJson(content, clazz);  
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("Error deserializing JSON to object", e);
            }
        } else {
            return null;  
        }
    }
}