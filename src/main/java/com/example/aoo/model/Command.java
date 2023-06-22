package com.example.aoo.model;

public enum Command {
    DATE("!Date"),
    METEO("!Meteo"), MAIL("!Mail"), TIME("!Heure"), END_OF_CLASS("!FinDeCours"), LOCAL_DATE("!DateLocale"), LOCAL_TIME("!HeureLocale"), DATE_IN_COUNTRY("!DatePays"), HELP("!Help");

    private final String value;

    Command(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        //return the value of each enum instance
        return this.value;
    }
}
