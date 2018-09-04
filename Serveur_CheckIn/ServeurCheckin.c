#include "ServeurCheckin.h"

pthread_mutex_t mutexIndiceCourant;
pthread_mutex_t mutexFichierTicket;
pthread_cond_t condIndiceCourant;
int indiceCourant=-1;
pthread_t threadHandle[NB_MAX_CLIENTS]; /* Threads pour clients*/
int hSocketConnectee[NB_MAX_CLIENTS]; /* Sockets pour clients*/
int Port_Service;
int Port_Bagage;
char Login_csv[80]={0},Ticket_csv[80] ={0}, AdresseIPServ[80]={0}, AdresseIPServBagage[80]={0};
userLogin vecLogin[20];//a mettre en * et gérer malloc
ticket vecTicket[20];//a mettre en * et gérer malloc
int hSocketEcoute;

int main()
{
    Config();
    struct sigaction sigact;
    sigemptyset(&sigact.sa_mask);
    sigact.sa_handler=HandlerQuit;
    if (sigaction(SIGINT,&sigact,NULL) == -1)
        perror("Erreur d'armement du signal SIGQUIT");

  /* Socket d'ecoute pour l'attente */
  int hSocketService;
  int i,j, /* variables d'iteration */
  retRecv; /* Code de retour dun recv */
  struct hostent * infosHost; /*Infos sur le host : pour gethostbyname */
  struct in_addr adresseIP; /* Adresse Internet au format reseau */
  struct sockaddr_in adresseSocket;
  int tailleSockaddr_in;
  int ret, * retThread;
  char msgServeur[MAXSTRING];
  char adresse[80];
  /* 1. Initialisations */
  puts("* Thread principal serveur demarre *");
  printf("identite = %d.%lu\n", getpid(), pthread_self());
  pthread_mutex_init(&mutexIndiceCourant, NULL);
  pthread_mutex_init(&mutexFichierTicket, NULL);
  pthread_cond_init(&condIndiceCourant, NULL);
  /* Si la socket n'est pas utilisee, le descripteur est a -1 */
  for (i=0; i<NB_MAX_CLIENTS; i++) hSocketConnectee[i] = -1;

  hSocketEcoute=confSockSrv(AdresseIPServ,Port_Service);
  fflush(stdin);


  /* 6. Lancement des threads */
  for (i=0; i<NB_MAX_CLIENTS; i++)
  {
    ret = pthread_create(&threadHandle[i],NULL,fctThread, (void*)i);
    printf("Thread secondaire %d lance !\n", i);
    ret = pthread_detach(threadHandle[i]);
  }
  do
  {
    /* 7. Mise a l'ecoute d'une requete de connexion */
    puts("Thread principal : en attente d'une connexion");
    if (listen(hSocketEcoute,SOMAXCONN) == -1)
    {
      printf("Erreur sur le listen de la socket %d\n", errno);
      close(hSocketEcoute); /* Fermeture de la socket */
      exit(1);
    }
    else printf("Listen socket OK\n");
    /* 8. Acceptation d'une connexion */
    tailleSockaddr_in = sizeof(struct sockaddr_in);
    if ( (hSocketService =
      accept(hSocketEcoute, (struct sockaddr *)&adresseSocket, &tailleSockaddr_in) )
      == -1)
      {
        printf("Erreur sur l'accept de la socket %d\n", errno);
        close(hSocketEcoute); /* Fermeture de la socket */
        exit(1);
      }
      else printf("Accept socket OK\n");
      /* 9. Recherche d'une socket connectee libre */
      printf("Recherche d'une socket connecteee libre ...\n");
      for (j=0; j<NB_MAX_CLIENTS && hSocketConnectee[j] !=-1; j++);
      if (j == NB_MAX_CLIENTS)
      {
        printf("Plus de connexion disponible\n");
        sprintf(msgServeur,DOC);
        sendSize(hSocketService,msgServeur,MAXSTRING);
        close(hSocketService); /* Fermeture de la socket */
      }
      else
      {
        /* Il y a une connexion de libre */
        printf("Connexion sur la socket num. %d\n", j);
        pthread_mutex_lock(&mutexIndiceCourant);
        hSocketConnectee[j] = hSocketService;
        indiceCourant=j;
        pthread_mutex_unlock(&mutexIndiceCourant);
        pthread_cond_signal(&condIndiceCourant);
      }
    }
    while (1);
    /* 10. Fermeture de la socket d'ecoute */
    close(hSocketEcoute); /* Fermeture de la socket */
    printf("Socket serveur fermee\n");
    puts("Fin du thread principal");
    return 0;
  }
  /* -------------------------------------------------------- */
  void * fctThread (void *param)
  {
    char * nomCli, *buf = (char*)malloc(100);
    char msgClient[MAXSTRING], msgServeur[MAXSTRING];
    int vr = (int)(param), finDialogue=0, i, iCliTraite;
    int temps, retRecv;
    char * numThr = getThreadIdentity();
    int hSocketServ;
    char *Buffer = NULL,*str = NULL,*Buffer2 = NULL ;
    char userName[80]={NULL},password[80]={NULL};
    int requete;
    int succes=0;
    int var;
    while (1)
    {
      /* 1. Attente d'un client à traiter */
      pthread_mutex_lock(&mutexIndiceCourant);
      while (indiceCourant == -1)
      pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);
      iCliTraite = indiceCourant; indiceCourant=-1;
      hSocketServ = hSocketConnectee[iCliTraite];
      pthread_mutex_unlock(&mutexIndiceCourant);
      sprintf(buf,"Je m'occupe du numero %d ...", iCliTraite);affThread(numThr, buf);
      /* 2. Dialogue thread-client */
      /*if(receiveSep(hSocketServ, msgClient))
        printf("\nReceive sep = %s\n", msgClient);*/
      finDialogue=0;
      do
      {

        if(receiveSize(hSocketServ, msgClient, MAXSTRING)==0)
        {
          printf("Erreur sur le recv de la socket connectee : %d\n", errno);
          exit(0);
        }
        
        printf("Requete recue = %s",msgClient);
        fflush(stdout);
        if (strcmp(msgClient, EOC)==0)
        {
          finDialogue=1; break;
        }

        Buffer2=strtok(msgClient,";");
        requete=atoi(Buffer2);
        printf("Requete = %d\n", requete);
        fflush(stdout);
        switch (requete)
        {
          case LOGIN_OFFICER:
            str=strtok(NULL,";");
            Buffer=strtok(NULL,";");

            if(str)
              strcpy(userName, str);
            if(Buffer)
              strcpy(password, Buffer);

            succes=Login(userName,password);

            if(succes == 1)
              strcpy(msgServeur,"OK");
            else
              strcpy(msgServeur,"NOK");

            break;
          case LOGOUT_OFFICER:
            break;
          case CHECK_TICKET:
            //chargeVecTicket();
            str=strtok(NULL,";");
            Buffer=strtok(NULL,";");
            var=atoi(Buffer);
            pthread_mutex_lock(&mutexFichierTicket);
            succes = checkTicket(str,var);
            pthread_mutex_unlock(&mutexFichierTicket);
            if(succes == 1)
            {
              strcpy(msgServeur,"OK");
             /* succes=checkLuggage(hSocketServ);
              if(succes==1)
              {
                strcpy(msgServeur,"OK");
                //printf("C'est un succes\n");
                //fflush(stdout);
              }
              else
                sprintf(msgServeur,"NOK");*/
            }
            else
              sprintf(msgServeur,"NOK");

          break;
          case PAYMENT_DONE:
            str=strtok(NULL,";");

            if(strcmp(str,"OK\0")==0)
              printf("Serveur - Paiement confirmé\n");
            else
            {
              Buffer=strtok(NULL,";");
              fopen(Buffer,"w");
              fclose(Buffer);
            }
            fflush(stdout);
            break;
          default:
              break;
          ;

        }

        sendSize(hSocketServ,msgServeur,MAXSTRING);
      }
      while (!finDialogue);
      /* 3. Fin de traitement */
      pthread_mutex_lock(&mutexIndiceCourant);
      hSocketConnectee[iCliTraite]=-1;
      pthread_mutex_unlock(&mutexIndiceCourant);
    }
    close (hSocketServ);
    return (void *)vr;
  }
  char * getThreadIdentity()
  {
    unsigned long numSequence;
    char *buf = (char *)malloc(30);
    sprintf(buf, "%d", getpid());
    return buf;
  }

  void Config()
  {
    int temp;

    FILE* filedesc = fopen("ServeurCheckIn.conf","r");
    if(filedesc==NULL)
    {
        puts("Erreur d'ouverture du fichier");
        exit(0);
    }
    
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
        else if(strcmp(str,"Port_Bagage") == 0)
        {
            temp=atoi(Buf);
            Port_Bagage=temp;
        }
        else if(strcmp(str,"Login_csv") == 0)
        {
            strcpy(Login_csv, "");
            strcpy(Login_csv,Buf);
            printf("ChargeVecLog"),
            chargeVecLogin();
        }
        else if(strcmp(str,"Ticket_csv") == 0)
        {
            strcpy(Ticket_csv, "");
            strcpy(Ticket_csv,Buf);
            temp = strlen(Ticket_csv);
            Ticket_csv[temp-1]=NULL;//Enlever \0

        }
        else if(strcmp(str,"AdresseIPServ") == 0)
        {
          strcpy(AdresseIPServ, "");
          strcpy(AdresseIPServ,Buf);
          temp = strlen(AdresseIPServ);
          AdresseIPServ[temp-1]=NULL;//Enlever \0
        }
        else if(strcmp(str,"AdresseIPServBagage") == 0)
        {
          strcpy(AdresseIPServBagage, "");
          strcpy(AdresseIPServBagage,Buf);
          temp = strlen(AdresseIPServBagage);
          AdresseIPServ[temp-1]=NULL;//Enlever \0
        }
        
      }
    } while(read != -1);

  }

  void chargeVecLogin()
  {
    int temp, i;
    temp = strlen(Login_csv);
    Login_csv[temp-1]=NULL;//Enlever \0
    FILE* filedesc = fopen(Login_csv,"r");
    //perror("Erreur d'ouverture :");
    char *Buf = NULL,*str;
    size_t len = 0;
    ssize_t read;

    i=0;
    do
    {
      read = getline(&Buf,&len,filedesc);
      if(read != -1)
      {
        str=strtok(Buf,";");//recupere avant egal
        Buf=strtok(NULL,";");//recupere apres egal
        strcpy(vecLogin[i].nomUtilisateur, str);
        strcpy(vecLogin[i].password, Buf);
        //printf("chargVec Nom utilisateur : %s\n Mdp: %s", vecLogin[i].nomUtilisateur, vecLogin[i].password);
        temp=strlen(vecLogin[i].password);
        vecLogin[i].password[temp-1]=NULL;
        i++;
      }
    } while(read != -1);

  }

  int Login(char * userName,char *password)
  {
      int i = 0,trouve =0;
      userName[strlen(userName)-1]=NULL;
      password[strlen(password)-1]=NULL;
      printf("Username : %s \nPassword: %s", userName, password);
      //Test si userName et password sont pas à NULL
      if(strcmp(userName,"")== 0|| strcmp(password,"")==0)
      {
        printf("Erreur - le userName ou le password est vide\n");
        trouve=2;
      }
      else
      {
        for(i=0;i<20 && trouve == 0;i++)
        {
            printf("%s =? %s\n", userName, vecLogin[i].nomUtilisateur);
            if(strcmp(userName,vecLogin[i].nomUtilisateur)==0)
            {
                printf("%s == %s\n", userName, vecLogin[i].nomUtilisateur);
                printf("%s =? %s\n", password, vecLogin[i].password);
                if(strcmp(password,vecLogin[i].password)==0)
                {
                    printf("%s == %s\n", password, vecLogin[i].password);
                    trouve = 1;
                }
                
            }
            else
                printf("%s =/= %s\n", userName, vecLogin[i].nomUtilisateur);
              
        }
      }
      printf("Trouve = %d",trouve);
      return trouve;
  }

  void HandlerQuit(int var)
  {
    printf("Fin du serveur\n");
    fflush(stdout);
    close(hSocketEcoute);
    exit(0);
  }
  int checkTicket(char* id_Ticket,int nbrPassagers)
  {
    int temp;
    //FILE* filedesc = fopen(Ticket_csv,"r");
    char *Buf = NULL,*str;
    size_t len = 0;
    ssize_t read;
    int trouve =0;
    char buffer [90];
    fflush(stdout);
    id_Ticket[strlen(id_Ticket)-1]=NULL;
    
    if(strcmp(id_Ticket,"")== 0|| nbrPassagers==0)
    {
      printf("Erreur - l'identifiant du ticket ou le nombre de passager est vide\n");
      trouve=2;
    }
    else
    {
        
      fflush(stdout);
      printf("IDTicket : %s\nPassagers : %d", id_Ticket, nbrPassagers);
      sprintf(buffer, "%s;%d", id_Ticket, nbrPassagers);
      
      //Connexion au serveur + demande de ticket
      
      int sock = connectToServ(AdresseIPServBagage , Port_Bagage );
      sendSize(sock,buffer,MAXSTRING);
      
      
      fflush(stdout);
    }
    return trouve;
  }

int checkLuggage(int hSocketServ)
{
  FILE* filedesc=NULL;
  int temp;
  char msgClient[MAXSTRING],*idTicket,*Poids,*Valise;
  do
  {
    temp=receiveSize(hSocketServ, msgClient, MAXSTRING);
    if(strcmp(msgClient,"FIN")==0 || temp==0)
    {
      fflush(stdout);
      if(filedesc)
        fclose(filedesc);
      return 1;

    }

    idTicket=strtok(msgClient,";");
    Poids=strtok(NULL,";");
    Valise=strtok(NULL,";");
    if(!filedesc)
      filedesc = fopen(idTicket,"w");
    sprintf(msgClient,"%s;%s;%s\n",msgClient,Poids,Valise);
    fwrite(msgClient,strlen(msgClient),1,filedesc);
    sendSize(hSocketServ,Poids,MAXSTRING);
  }while(strcmp(msgClient,"FIN")!=0);
  fclose(filedesc);
  return 1;
}