package ProtocoleLUGAPM;

import java.io.Serializable;

/**
 * Created by Vince on 13-12-17.
 */

public class Bagage implements Serializable {
    private String idBagage;
    private int nbValise;
    private float poids;
    private String receptionne;
    private String chargeEnSoute;

    public Bagage(String id, int nb, float p, String recep, String charge) {
        setIdBagage(id);
        setNbValise(nb);
        setPoids(p);
        setReceptionne(recep);
        setChargeEnSoute(charge);
    }

    @Override
    public String toString() {
        return getIdBagage()+" avec "+nbValise+" valises";
    }

    public String getIdBagage() {
        return idBagage;
    }

    public void setIdBagage(String idBagage) {
        this.idBagage = idBagage;
    }

    public int getNbValise() {
        return nbValise;
    }

    public void setNbValise(int nbValise) {
        this.nbValise = nbValise;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public String isReceptionne() {
        return receptionne;
    }

    public void setReceptionne(String receptionne) {
        this.receptionne = receptionne;
    }

    public String isChargeEnSoute() {
        return chargeEnSoute;
    }

    public void setChargeEnSoute(String chargeEnSoute) {
        this.chargeEnSoute = chargeEnSoute;
    }
}
