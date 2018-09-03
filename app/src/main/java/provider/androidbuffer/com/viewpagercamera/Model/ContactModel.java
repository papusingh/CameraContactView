package provider.androidbuffer.com.viewpagercamera.Model;

import java.io.Serializable;

/**
 * Created by incred-dev on 31/8/18.
 */

public class ContactModel implements Serializable{

    String name;

    String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
