/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientui;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import client.OvenClient;

/**
 *
 * @author x14484252
 */
public class OvenUI extends ClientUI {

    private static final long serialVersionUID = -5318589393275157185L;
    private JButton increase;
    private JButton decrease;
    private JButton turnOvenOn;
    private JButton turnOvenOff;
    private final OvenClient ovenClientRef;

    public OvenUI(OvenClient ovenClient) {
        super(ovenClient);
        ovenClientRef = ovenClient;
        init();
    }

    @Override
    public void init() {
        super.init();
        increase = new JButton("Increase");
        increase.setEnabled(false);
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{increase});

        decrease = new JButton("Decrease");
        decrease.setEnabled(false);
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{decrease});
        
        turnOvenOff = new JButton("Turn Oven Off");
        turnOvenOff.setEnabled(false);
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{turnOvenOff});

        turnOvenOn = new JButton("Turn Oven On");
        turnOvenOn.setEnabled(true);
        scroll.setBounds(5, 40, UIConstants.COMPONENTWIDTH, 300);
        add(new JButton[]{turnOvenOn});
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == increase) {
            ovenClientRef.increaseTemp();
        } else if (e.getSource() == decrease) {
            ovenClientRef.decreaseTemp();
        } else if (e.getSource() == turnOvenOff) {
            ovenClientRef.turnOvenOff();
            increase.setEnabled(false);
            decrease.setEnabled(false);
            turnOvenOff.setEnabled(false);
            turnOvenOn.setEnabled(true);
        } else if (e.getSource() == turnOvenOn) {
            ovenClientRef.turnLightsOn();
            increase.setEnabled(true);
            decrease.setEnabled(true);
            turnOvenOff.setEnabled(true);
            turnOvenOn.setEnabled(false);
        }
    }
}
