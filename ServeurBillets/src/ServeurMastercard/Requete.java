/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServeurMastercard;

import java.net.*;
import java.security.PublicKey;

/**
 *
 * @author Vince
 */
public interface Requete {
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs);
}
