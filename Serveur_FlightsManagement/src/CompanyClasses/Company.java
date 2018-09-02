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
public interface Company {
    public void createDestination(String v, String p, BeanBD Bc);
    public void createFlight(String v, String p, Date d, Time t, double pr,BeanBD Bc,int nTickets);
    public void cancelDestination(String v, String p,BeanBD Bc);
    public void cancelFlights(String v, String p, Date d, Time t,BeanBD Bc);         
}
