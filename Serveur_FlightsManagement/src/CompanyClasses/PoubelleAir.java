/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompanyClasses;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author tibha
 */
public class PoubelleAir implements Company {
    private boolean lowcost;
    public PoubelleAir()
    {
    
    }
    public PoubelleAir(boolean l)
    {
        
    }
    @Override
    public void createDestination(String v, String p) {
        System.out.println("CREATE DESTINATION : ville: " + v + " pays: " + p);
    }

    @Override
    public void createFlight(String v, String p, Date d, Time t, double pr) {
        System.out.println("TEST 2");
    }

    @Override
    public void cancelDestination(String v, String p) {
        System.out.println("TEST 3");
    }

    @Override
    public void cancelFlights(String v, String p, Date d, Time t) {
        System.out.println("TEST 4");
    }
    
}
