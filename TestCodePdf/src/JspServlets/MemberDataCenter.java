package JspServlets;
public class MemberDataCenter
{
private String nom;
private String prenom;
private String motDePasse;
private String eMail;
public MemberDataCenter()
{
nom = prenom = motDePasse = eMail = null;
}
public MemberDataCenter(String n, String pwd)
{
nom = n;
motDePasse = pwd;
prenom = eMail = null;
}
public boolean equals(MemberDataCenter mdc)
{
return ( nom.equals(mdc.nom) && prenom.equals(mdc.prenom) &&
motDePasse.equals(mdc.motDePasse) );
}
public String getNom() { return nom; }
public String getPrenom() { return prenom; }
public String getMotDePasse() { return motDePasse; }
public String getEMail() { return eMail; }
public void setNom(String x) { nom = x; }
public void setPrenom(String x) { prenom = x; }
public void setMotDePasse(String x) { motDePasse = x; }
public void setEMail(String x) { eMail = x; }
}