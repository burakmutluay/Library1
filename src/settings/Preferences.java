package settings;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import sun.rmi.runtime.Log;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferences {
    public static final String CONFIG_FILE = "config.txt";

    private int nDaysWithoutFine;
    private float finePerDay;
    private String username;
    private String password;

    public Preferences(){
        this.nDaysWithoutFine = 14;
        this.finePerDay = 2;
        this.username = "admin";
        setPassword("admin");

    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length()<16){
            this.password = DigestUtils.sha1Hex(password);

        }else {
            this.password = password;
        }
    }

    public static void initConfig(){

        try {
            Preferences preferences = new Preferences();
            Gson gson = new Gson();
            Writer writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
            writer.close();

        } catch (IOException e) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void writePreferencesToFile(Preferences pref){
        try {
            Preferences preferences = pref;
            Gson gson = new Gson();
            Writer writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
            writer.close();

        } catch (IOException e) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Preferences getPreferences(){
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException e) {
            initConfig();
            e.printStackTrace();
        }

        return preferences;
    }

}
