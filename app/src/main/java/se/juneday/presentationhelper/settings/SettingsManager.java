package se.juneday.presentationhelper.settings;

import se.juneday.presentationhelper.domain.AuthType;

public interface SettingsManager {

    public AuthType authType();
    public int pinCode();
    public String user();
    public String password();

}
