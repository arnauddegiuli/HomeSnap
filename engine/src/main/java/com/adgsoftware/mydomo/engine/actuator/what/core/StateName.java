package com.adgsoftware.mydomo.engine.actuator.what.core;

public enum StateName implements StateNames { // Enumeration des noms des états des états définis par notre API

	 

    STATUS(StateNames.STATUS),

    LEVEL(StateNames.LEVEL),

    ALARM_HIGH(StateNames.ALARM_HIGH),

    ALARM_LOW(StateNames.ALARM_LOW),

    HIGHEST_TEMP(StateNames.HIGHEST_TEMP),

    LOWEST_TEMP(StateNames.LOWEST_TEMP),

    UNIT(StateNames.UNIT),

   

    CUSTOM(""); // For user defined states

   

    private String name;

    private StateName(String name) {

                   this.name = name;

    }

   

    public String getName() {

                   return name;

    }

   

    public static StateName fromValue(String name) {

                   for (StateName sn : StateName.values()) {

                                   if (sn.name.equals(name)) {

                                                   return sn;

                                   }

                   }

                  

                   StateName custom = CUSTOM;

                   custom.name = name;

                   return custom;

    }

}
