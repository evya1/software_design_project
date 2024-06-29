package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class MessageObject implements Serializable {
    String msg;
    Object object;

    public MessageObject() {}

    public MessageObject(String msg, Object object ) {
        this.msg = msg;
        this.object = object;
    }

    public MessageObject(String msg) {
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
