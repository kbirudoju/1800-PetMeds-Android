package com.petmeds1800.model.refillreminder.response;

import java.io.Serializable;

/**
 * Created by Sarthak on 24-Oct-16.
 */

public class MonthList implements Serializable {
    private String name;

    private String value;

    private String shortName;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getShortName ()
    {
        return shortName;
    }

    public void setShortName (String shortName)
    {
        this.shortName = shortName;
    }
}
