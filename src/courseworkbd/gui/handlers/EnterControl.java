/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.EnterForm;
import courseworkbd.gui.utility.FormController;
import courseworkbd.gui.handlers.MainMenuControl;
import courseworkbd.gui.utility.Listener;
import courseworkbd.utility.StaticID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class EnterControl implements Listener, FormController {

    EnterForm enterForm;

    public EnterControl() {
        enterForm = new EnterForm();
        enterForm.addListener(this);
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.buttonLogIn: {
                try {
                    onLogIn();
                } catch (IOException ex) {
                    Logger.getLogger(EnterControl.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            default: {
                System.out.println("Error");
            }
        }
    }

    @Override
    public void show() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                enterForm.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                enterForm.setVisible(false);
            }
        });
    }

    private void onLogIn() throws FileNotFoundException, IOException {

        File password = new File("passwords.pass");
        BufferedReader br = new BufferedReader(new FileReader(password));
        String line = br.readLine();
        if (!line.equals("<passwords>")) {
            return;
        }
        line = br.readLine();
        for (; !line.equals("<!passwords>"); line = br.readLine()) {
            String[] parts = line.split("~");
            if (enterForm.getjTextFieldLogin().getText().equals(parts[0])
                    && enterForm.getjPasswordFieldPassword().getText().equals(parts[1])) {
                if (parts[2].equals("1")) {
                    StaticID.admin = true;
                }
                this.hide();
                MainMenuControl mmc = new MainMenuControl();
                mmc.show();
            }
        }
        JOptionPane.showMessageDialog(enterForm, "Wrong login or password.");
    }

}
