#include "ApplicationCheckIn.h"
int Port_Service;

int hSocket; /* Handle de la socket */
struct hostent * infosHost; /*Infos sur le host : pour gethostbyname */
struct in_addr adresseIP; /* Adresse Internet au format reseau */
struct sockaddr_in adresseSocket;
int tailleSockaddr_in;
int ret; /* valeur de retour */
char msgClient[MAXSTRING],msgSendClient[MAXSTRING], msgServeur[MAXSTRING],numTicket[80],AdresseIPServ[80]={0};
int nbPassagers;
int stop;
char adresse[80];
int main()
{
  int retourMenu, retourMenuPrinc;
  Config();

  Init();

  do
  {
    retourMenu = MenuConnexion();
    do
    {
        retourMenuPrinc = MenuPrincipal();
    }while(retourMenuPrinc!=2);
      
    
  }while (retourMenu !=2 );

  /* 9. Fermeture de la socket */
  close(hSocket); /* Fermeture de la socket */
  printf("Socket client fermee\n");
  return 0;
}
void Config()
{
  int temp;

  FILE* filedesc = fopen("ServeurCheckIn.conf","r");
  char *Buf = NULL,*str;
  size_t len = 0;
  ssize_t read;
  do
  {
    read = getline(&Buf,&len,filedesc);
    if(read != -1)
    {
      str=strtok(Buf,"=");//recupere avant egal
      Buf=strtok(NULL,"=");//recupere apres egal
      if(strcmp(str,"Port_Service") == 0)
      {
          temp=atoi(Buf);
          Port_Service=temp;
      }
      else if(strcmp(str,"AdresseIPServ") == 0)
      {
        strcpy(AdresseIPServ, "");
        strcpy(AdresseIPServ,Buf);
        temp = strlen(AdresseIPServ);
        AdresseIPServ[temp-1]=NULL;//Enlever \0
      }
    }
  } while(read != -1);

}
void Init()
{
  hSocket=confSockCli(AdresseIPServ,Port_Service);
  
}
int Login()
{
  char nomUtilisateur[80], password[80];
  int trouve=0;
  printf("Veuillez entrer votre nom d'utilisateur :\n");
  fflush(stdout);
  fflush(stdin);
  fgets(nomUtilisateur, sizeof(nomUtilisateur), stdin);
  fgets(nomUtilisateur, sizeof(nomUtilisateur), stdin);
  printf("Utilisateur : %s\n", nomUtilisateur);
  printf("Veuillez entrer votre mot de passe :\n");
  fflush(stdout);
  fflush(stdin);
  fgets(password,sizeof(password), stdin);
  printf("Password : %s\n", password);
  strcpy(msgClient, nomUtilisateur);
  strcat(msgClient,";");
  strcat(msgClient, password);

  //envoi de la requete
  sprintf(msgSendClient,"%d;%s",LOGIN_OFFICER,msgClient);
  printf("msgSendClient = %s\n", msgSendClient);
  sendSize(hSocket,msgSendClient,MAXSTRING);
  //reception de la requete
  if (strcmp(msgSendClient,EOC))
  {
    /* 6. Reception de l'ACK du serveur au client */
    receiveSize(hSocket, msgServeur, MAXSTRING);
    if(strcmp(msgServeur,"OK")==0)
    {
      trouve=1;
      printf("Login reussi\n");
    }
    else
        printf("Erreur dans le login veuillez réessayer\n");
  }
  return trouve;
}

int MenuPrincipal()
{
  int cpt;
  float i;
  int ret, succes;
  float poidsTotal;
  char Buffer[2];
  printf("1. Vérification des tickets\n");
  printf("2. Se déconnecter\n");
  fflush(stdin);
  fgets(Buffer, sizeof(Buffer), stdin);
  ret = atoi(Buffer);
  stop=0;
  switch(ret)
  {
    case 1:
        if(demandeTicket()==1)
        {
            printf("Ticket vérifié\n");
          /*i=0;
          poidsTotal=0;
          do
          {
            poidsTotal=addLugagge(i,poidsTotal);
            i++;
          }
          while(i<nbPassagers && stop != 1);
          i--;
          if(i>0)
          {
            poidsTotal=poidsTotal-(i*20);
            if(poidsTotal>0)
            {
              printf("Poids excédant: %f \n",poidsTotal);
              fflush(stdout);
            }
          }
          printf("Paiement effectué ? (Y ou N)\n");
          fgets(Buffer, sizeof(Buffer), stdin);
          if(strcmp("Y", Buffer)==0)
            sprintf(msgClient, "%d;OK", PAYMENT_DONE);
          else
            sprintf(msgClient, "%d;NOK;%s", PAYMENT_DONE, numTicket);
          succes = sendSize(hSocket, msgClient, MAXSTRING);*/
        }
      break;
  }
  return ret;
}

int MenuConnexion()
{
  char Buffer[2];
  int ret=0;
  
  do
  {
      
      printf("1. Se connecter\n");
      printf("2. Quitter\n");
      fflush(stdin);
      fgets(Buffer, sizeof(Buffer), stdin);
      system("clear");
      if(strcmp(Buffer, "\n\0")==0)
          printf("Erreur - Veuillez selectionner un chiffre entre 1 et 2\n");
      else 
        ret = atoi(Buffer);
  }while(ret!=1 && ret!=2);
  
  if(ret==1)
  {
      if(Login()==1)
          ret=1;
      else
          ret =0;
  }
  if(ret==2)
      exit(0);

  return ret;
}

int demandeTicket()
{
  int trouve=0;
  char id[80],numero[80];
  printf("Veuillez entrer l'identifiant du ticket :");
  fflush(stdout);
  fflush(stdin);
  fgets(id, sizeof(id), stdin);
  fgets(id, sizeof(id), stdin);
  strcpy(numTicket,id);
  printf("Veuillez entrer le nombre de passagers pour ce ticket :");
  fflush(stdout);
  fflush(stdin);
  fgets(numero, sizeof(numero), stdin);
  nbPassagers=atoi(numero);
  strcpy(msgClient, id);
  strcat(msgClient,";");
  strcat(msgClient, numero);

  //envoi de la requete
  sprintf(msgSendClient,"%d;%s",CHECK_TICKET,msgClient);
  printf("msgSendClient = %s\n", msgClient);
  sendSize(hSocket,msgSendClient,MAXSTRING);

  //reception de la requete
  if (strcmp(msgSendClient,EOC))
  {
    receiveSize(hSocket, msgServeur, MAXSTRING);
    if(strcmp(msgServeur,"OK")==0)
    {
      trouve=1;
    }
    else
      printf("Ticket invalide\n");
    fflush(stdout);
  }
  return trouve;
}
int addLugagge(int i,int Poids)
{
  char Buffer[2];
  char BufPoids[78];
  printf("Poids du bagage numéro %d: <ENTER> si fini\n",i);
  fflush(stdin);
  fgets(BufPoids, sizeof(BufPoids), stdin);
  BufPoids[strlen(BufPoids)]='\0';
  printf("%s taille : %zd\n",BufPoids,strlen(BufPoids));
  fflush(stdout);
  if(strcmp(BufPoids,"\n")==0)
  {
    strcpy(msgClient, "FIN");
    fflush(stdout);
    stop = 1;
    //printf("FIN\n",i);
    sendSize(hSocket,msgClient,MAXSTRING);
    return Poids;
  }
  else
  {
    printf("Valise ? (Y ou N):\n");
    fflush(stdin);
    fgets(Buffer, sizeof(Buffer), stdin);
    numTicket[strlen(numTicket)-1]='\0';
    BufPoids[strlen(BufPoids)-1]='\0';
    Buffer[strlen(Buffer)-1]='\0';
    sprintf(msgClient, "%s;%s;%s", numTicket, BufPoids, Buffer);
  }
  printf("MsgClient = %s",msgClient);
  sendSize(hSocket,msgClient,MAXSTRING);

  receiveSize(hSocket,msgServeur,MAXSTRING);
  Poids=Poids+atoi(BufPoids);
  return(Poids);
}