package com.hm;

import java.io.Serializable;

/**
 * Created by ant_shake_tree on 15/10/26.
 */
public class RedissionPageInfo implements Serializable ,Comparable<RedissionPageInfo>{
    private static final long serialVersionUID = -1900414231151123123L;
    private Object name;
    private int toltalRecord;

    public Object getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToltalRecord() {
        return toltalRecord;
    }

    public void setToltalRecord(int toltalRecord) {
        this.toltalRecord = toltalRecord;
    }


    @Override
    public int compareTo(RedissionPageInfo o) {
        if(name.equals(o.name))return 0;

        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RedissionPageInfo)) return false;

        RedissionPageInfo that = (RedissionPageInfo) o;

        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
