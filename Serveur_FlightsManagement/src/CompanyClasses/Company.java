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
public interface Company {
    public void createDestination(String v, String p);
    public void createFlight(String v, String p, Date d, Time t, double pr);
    public void cancelDestination(String v, String p);
    public void cancelFlights(String v, String p, Date d, Time t);         
}
