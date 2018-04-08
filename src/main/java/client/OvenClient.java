/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import clientui.OvenUI;
import models.OvenModel;
import com.google.gson.Gson;


/**
 *
 * @author x14484252
 */

/* @reference 
*  Dominic Carr https://moodle.ncirl.ie/mod/resource/view.php?id=53562
*/


public class OvenClient extends Client {
    private String increaseTemp  = "increaseTempreature";
    private String decreaseTemp  = "decreaseTempreature";
    private String turnOvenOn  = "turnOvenOn";
    private String turnOvenOff = "turnOvenOff";
    private boolean isWarming    = false;
    
    /*
    * Constructor
    */
    
    public OvenClient(){
        super();
        serviceType = "_oven._udp.local.";
        ui = new OvenUI(this);
        name = "Oven";
    }
    
    
    public void increaseTemp(){
        String json = new Gson().toJson(new OvenModel(OvenModel.serviceAction.increaseTemp));
        String message = sendMessage(json);
        OvenModel oven = new Gson().fromJson(message,OvenModel.class);
        System.out.println("Client Found " +json);
        
        if (oven.getAction() == OvenModel.serviceAction.increaseTemp){
            isWarming = oven.getValue();
            ui.updateArea(oven.getMessage());
        }
    }
    
    public void decreaseTemp(){
        String json = new Gson().toJson(new OvenModel(OvenModel.serviceAction.decreaseTemp));
        String message = sendMessage(json);
        OvenModel oven = new Gson().fromJson(message,OvenModel.class);
        System.out.println("Client Found " +json);
        
        if (oven.getAction() == OvenModel.serviceAction.decreaseTemp){
            isWarming = oven.getValue();
            ui.updateArea(oven.getMessage());
        }
    }
    
    public void turnOvenOff(){
        String json = new Gson().toJson(new OvenModel(OvenModel.serviceAction.turnOvenOff));
        String message = sendMessage(json);
        OvenModel oven = new Gson().fromJson(message, OvenModel.class);
        System.out.println("Client Recieved " + json);
        
        if(oven.getAction() == OvenModel.serviceAction.turnOvenOff){
            isWarming = oven.getValue();
            ui.updateArea(oven.getMessage());
        }
    }
    
    public void turnLightsOn(){
        String json = new Gson().toJson(new OvenModel(OvenModel.serviceAction.turnOvenOn));
        String message = sendMessage(json);
        OvenModel oven = new Gson().fromJson(message, OvenModel.class);
        System.out.println("Client Recieved " + json);
        
        if(oven.getAction() == OvenModel.serviceAction.turnOvenOn){
            isWarming = oven.getValue();
            ui.updateArea(oven.getMessage());
        }
    }
    
    @Override
    public void updatePoll(String message) {
        if (message.equals("Oven is 100% warmed.")) {
            isWarming = false;
        }
    }

    @Override
    public void disable() {
        super.disable();
        ui = new OvenUI(this);
        isWarming = false;
    }
    
    
}
