package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class msgOb implements Serializable {
    String msg;
    Object object;

    public msgOb() {}

    public msgOb(String msg, Object object ) {
        this.msg = msg;
        this.object = object;
    }

    public msgOb(String msg) {
        this.msg = msg;
        this.object = null;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
