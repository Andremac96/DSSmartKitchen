package services;

import com.google.gson.Gson;
import java.util.Timer;
import java.util.TimerTask;
import models.ToasterModel;
import servicesui.ServiceUI;

/**
 *
 * @author andre
 */
public class ToasterService extends Service {

    private int power;
    private int breadLevel;
    private int toastLevelPercent;
    private Timer timer;
    private static boolean turnToasterOn, isRunning, turnToasterOff, breadInToaster, toasting;

    public ToasterService(String serviceName) {
        super(serviceName, "_toaster._udp.local.");
        //Toaster Starts Off
        timer = new Timer();
        turnToasterOn = false;
        turnToasterOff = true;
        breadInToaster = false;
        isRunning = false;
        breadLevel = 0;
        toasting = false;
        toastLevelPercent = 0;

        //
        ui = new ServiceUI(this, serviceName);
    }

    @Override
    protected void performAction(String a) {
        System.out.println("Connected with service: " + a);
        ToasterModel toaster = new Gson().fromJson(a, ToasterModel.class);

        if (toaster.getAction() == ToasterModel.serviceAction.STATUS) {
            String message = getStatus();
            String json = new Gson().toJson(new ToasterModel(ToasterModel.serviceAction.STATUS, message));
            sendBack(json);
        } else if (toaster.getAction() == ToasterModel.serviceAction.turnToasterOn) {
            turnToasterOn();
            String message = (turnToasterOn) ? "The Toaster has been turned On" : "The Toaster is currently On";
            String json = new Gson().toJson(new ToasterModel(ToasterModel.serviceAction.turnToasterOn, message));
            System.out.println(json);
            sendBack(json);

            String serviceMessage = (turnToasterOn) ? "Toaster turned on!" : "Toaster is on!";
            ui.updateArea(serviceMessage);
        } else if (toaster.getAction() == ToasterModel.serviceAction.turnToasterOff) {
            turnToasterOff();
            String message = (turnToasterOff) ? "The Toaster has been turned Off" : "The Toaster is currently Off";
            String json = new Gson().toJson(new ToasterModel(ToasterModel.serviceAction.turnToasterOff, message));
            System.out.println(json);
            sendBack(json);

            String serviceMessage = (turnToasterOff) ? "Toaster turned Off" : "Toaster is Off";
            ui.updateArea(serviceMessage);
        } else if (toaster.getAction() == ToasterModel.serviceAction.putBreadInToaster) {
            putBreadInToaster();
            String message = (breadInToaster) ? "The bread is in the toaster" : "There is currently bread in the toaster";
            String json = new Gson().toJson(new ToasterModel(ToasterModel.serviceAction.putBreadInToaster, message));
            System.out.println(json);
            sendBack(json);

            String serviceMessage = (breadInToaster) ? "The bread is in the toaster" : "There is currently bread in the toaster";
            ui.updateArea(serviceMessage);
        } else if (toaster.getAction() == ToasterModel.serviceAction.toasting) {
            timer = new Timer();
            timer.schedule(new RemindTask(), 100, 1000);
            String message = (toasting) ? "The bread is toasting" : "The bread is currently toasting";
            String json = new Gson().toJson(new ToasterModel(ToasterModel.serviceAction.toasting, message, toasting));
            System.out.println(json);
            sendBack(json);

            String serviceMessage = (toasting) ? "The bread is toasting" : "The bread is currently toasting";
            ui.updateArea(serviceMessage);
        }
        else if (toaster.getAction() == ToasterModel.serviceAction.finishToasting) {
            finishToasting();
            String message = (toasting) ? "The bread is finished toasting" : "The final toast tempreature is " + toastLevelPercent + "\nPlease take your toast";
            String json = new Gson().toJson(new ToasterModel(ToasterModel.serviceAction.finishToasting, message));
            System.out.println(json);
            sendBack(json);

            String serviceMessage = (toasting) ? "The bread is finished toasting" : "The final toast tempreature is " + toastLevelPercent + "Please take your toast";
            ui.updateArea(serviceMessage);
        } else {
            sendBack(BAD_COMMAND + " - " + a);
        }
    }

    
    
    
    
    
    class RemindTask extends TimerTask {

        @Override
        public void run() {
            isRunning = true;
            if (toastLevelPercent < 100 && breadLevel > 1) {
                toasting = true;
                toastLevelPercent += 10;
            } else {
                toasting = false;
            }
        }
    }

    
    
    
    public void turnToasterOn() {
        if (power <= 0) {
            power += 100;
            System.out.println("The toaster is powered on");
        }
    }
    
    
    
    
    

    public void turnToasterOff() {
        if (power >= 0) {
            power = 0;
            breadLevel = 0;
            System.out.println("The toaster is powered off");
        }
    }
    
    
    
    

    public void putBreadInToaster() {
        if (breadLevel <= 0) {
            breadInToaster = true;
            breadLevel = 4;
            toastLevelPercent = 0;
        } else {
            breadInToaster = false;
        }
    }
    
    
    
    
    public void finishToasting() {
        toasting = false;
        timer.cancel();
        isRunning = false;
        breadLevel = 0;
    }

    
    
    
    
    
    @Override
    public String getStatus() {
        String message = "";

        if (toastLevelPercent == 0 && breadLevel > 0) {
            message = "Bread is ready to Toast!";
        } 
        
        else if (toastLevelPercent > 0 && toastLevelPercent < 100) {
            if ((toasting=true) && (isRunning = true)){
                message = "Bread is " + toastLevelPercent + " degrees celsius";
            } 
            
            else if (toasting = false){
                return null;
            }
        } 
        
        else if (toastLevelPercent >= 100) {
            message = "Toasting is complete";
            toasting = false;
            breadLevel =0;
            
            if (isRunning) {
                timer.cancel();
                isRunning = false;
            }
        }

        return message;
    }

    
    
    
    
    public static void main(String[] args) {
        new ToasterService("ToasterService");
    }

}
