package com.example.unLocode.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
@RedisHash
public class Code implements Serializable {
    @Id
    private int id;
    private String uncode;
    private String name;
    private String func;

    public Code() {}

    public Code(int id, String uncode, String name, String func) {
        this.id = id;
        this.uncode = uncode;
        this.name = name;
        this.func = func;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUncode() {
        return uncode;
    }

    public void setUncode(String uncode) {
        this.uncode = uncode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", uncode='" + uncode + '\'' +
                ", name='" + name + '\'' +
                ", func='" + func + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code = (Code) o;

        if (id != code.id) return false;
        if (!Objects.equals(uncode, code.uncode)) return false;
        if (!Objects.equals(name, code.name)) return false;
        return Objects.equals(func, code.func);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uncode != null ? uncode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (func != null ? func.hashCode() : 0);
        return result;
    }
}
