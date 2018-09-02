/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompanyClasses;

import database.utilities.BeanBD;
import java.sql.Date;
import java.sql.Time;

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
    public void createDestination(String v, String p,BeanBD Bc) {
        //System.out.println("CREATE DESTINATION : ville: " + v + " pays: " + p);
        Bc.addDestination(v,p);
    }

    @Override
    public void createFlight(String v, String p, Date d, Time t, double pr,BeanBD Bc,int nTickets) {
        //System.out.println("createFlight : VILLE : " + v + " PAYS: " + p + " DATE: " + d.toString() + " TIME: " + t.toString() + " PRIX : " + pr);
        Bc.addFlight(v,p,d,t,pr,nTickets);
    }

    @Override
    public void cancelDestination(String v, String p,BeanBD Bc) {
        System.out.println("TEST 3");
        Bc.delDestination(v,p);
    }

    @Override
    public void cancelFlights(String v, String p, Date d, Time t,BeanBD Bc) {
        System.out.println("TEST 4");
        Bc.delFlight(v, p, d, t);
    }
    
}
