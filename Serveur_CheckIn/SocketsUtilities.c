#include "SocketsUtilities.h"

int receiveSize(int socket, char * struc, int size)
{
  char *buf = (char*)malloc(100);
  fflush(stdout);
  int retRecv;
  if ((retRecv=recv(socket, struc, size, 0)) == -1)
  {
    printf("Erreur sur le recv de la socket %d\n", errno);
    fflush(stdout);
    close(socket); /* Fermeture de la socket */
  }
  else
  if (retRecv==0)
  {
    sprintf(buf,"Le client est parti !!!\n"); printf("%s", buf);
    fflush(stdout);
    return 0;
  }
  else
  {
    /*sprintf(buf,"Message recu = %s\n", ((char*)struc));
    printf("%s", buf);*/
    fflush(stdout);
    return 1;
  }
}


int receiveSep(int socket, char * msgClient)
{
  int size = strlen(msgClient);
  char *buf = (char*)malloc(size);
  fflush(stdout);
  int tailleMsgRecu=0;
  int finDetectee = 0;
  int retRecv;

  do
  {
    if ((retRecv=recv(socket, buf, size, 0)) == -1)
    {
      printf("Erreur sur le recv de la socket %d\n", errno);
      fflush(stdout);
      close(socket); /* Fermeture de la socket */
    }
    else
    {
      if (retRecv==0)
      {
        sprintf(buf,"Le client est parti !!!\n"); printf("%s", buf);
        fflush(stdout);
        return 0;
      }
      finDetectee = (buf,retRecv);
      memcpy((char *)msgClient + tailleMsgRecu, buf,retRecv);
      tailleMsgRecu += retRecv;
    }
  } while(retRecv != 0 && retRecv != -1 && !finDetectee);
  return 1;
}

char marqueurRecu (char *m, int nc)
/* Recherche de la sequence \r\n */
{
  static char demiTrouve=0;
  int i;
  char trouve=0;
  if (demiTrouve==1 && m[0]=='\n')
    return 1;
  else demiTrouve=0;
  for (i=0; i<nc-1 && !trouve; i++)
  if (m[i]=='\r' && m[i+1]=='\n')
    trouve=1;
  if (trouve)
    return 1;
  else if (m[nc]=='\r')
  {
    demiTrouve=1;
    return 0;
  }
  else return 0;
}

int sendSep(int socket,char * buf)
{
  char *buffer;
  buffer = (char*)malloc((char)strlen(buf)+2);
  strcat(buffer,"\r\n");
  if (send(socket, buffer, strlen(buffer), 0) == -1)
  {
    printf("Erreur sur le send de la socket %d\n", errno);
    close(socket); /* Fermeture de la socket */
    return 0;
  }
  else return 1;
}

int sendSize(int socket,char * buf, int size)
{

  if (send(socket, buf, size, 0) == -1)
  {
    printf("Erreur sur le send de la socket %d\n", errno);
    close(socket); /* Fermeture de la socket */
    return 0;
  }
  else return 1;
}

int connectToServ(char * ip, int port )
{
    int hSocket;
    struct hostent * infosHost; 
    struct in_addr adresseIP; 
    struct sockaddr_in adresseSocket; 
    unsigned int tailleSockaddr_in;
    int ret;
    
    /* 1. Création de la socket */
    hSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (hSocket == -1)
    {
        printf("Erreur de creation de la socket %d\n", errno);
        exit(1);
    }
    else printf("Creation de la socket OK\n");
    
    if (!inet_aton(ip, &adresseIP))
        exit(0);
    if ( (infosHost = gethostbyaddr((const void *)&adresseIP,sizeof(ip),AF_INET ))==NULL)
    {
        printf("Erreur d'acquisition d'infos sur le host distant %d\n", errno);
        return -1;
    }
    else printf("Acquisition infos host distant OK\n");
    memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
    printf("Adresse IP = %s\n",inet_ntoa(adresseIP));
    /* 3. Préparation de la structure sockaddr_in */
    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET; /* Domaine */
    adresseSocket.sin_port = htons(port); /* conversion port au format réseau */
    memcpy(&adresseSocket.sin_addr, infosHost->h_addr,infosHost->h_length);
    /* 4. Tentative de connexion */
    tailleSockaddr_in = sizeof(struct sockaddr_in);
    if (( ret = connect(hSocket, (struct sockaddr *)&adresseSocket, tailleSockaddr_in) )
    == -1)
    {
        printf("Erreur sur connect de la socket %d\n", errno);
        close(hSocket);
        return -1;
    }
    else printf("Connect socket OK\n");
    
    return hSocket;
}

int confSockSrv(char *adresse,int Port)
{
  struct hostent * infosHost; /*Infos sur le host : pour gethostbyname */
  struct in_addr adresseIP; /* Adresse Internet au format reseau */
  struct sockaddr_in adresseSocket;
  int hSocketEcoute;

  hSocketEcoute = socket(AF_INET,SOCK_STREAM,0);
  if (hSocketEcoute == -1)
  {
    printf("Erreur de creation de la socket %d\n", errno);
    exit(1);
  }
  else printf("Creation de la socket OK\n");
  /* 3. Acquisition des informations sur l'ordinateur local */
  if ( (infosHost = gethostbyname(adresse))==0)
  {
    printf("Erreur d'acquisition d'infos sur le host %d\n", errno);
    exit(1);
  }
  else printf("Acquisition infos host OK\n");
  memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
  printf("Adresse IP = %s\n",inet_ntoa(adresseIP));
  /* 4. Préparation de la structure sockaddr_in */
  memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
  adresseSocket.sin_family = AF_INET;
  adresseSocket.sin_port = htons(Port);
  memcpy(&adresseSocket.sin_addr, infosHost->h_addr, infosHost->h_length);
  /* 5. Le système prend connaissance de l'adresse et du port de la socket */
  if (bind(hSocketEcoute, (struct sockaddr *)&adresseSocket,
  sizeof(struct sockaddr_in)) == -1)
  {
    printf("Erreur sur le bind de la socket %d\n", errno);
    exit(1);
  }
  else printf("Bind adresse et port socket OK\n");

  return hSocketEcoute;
}
int confSockCli(char *adresse,int Port)
{
  struct hostent * infosHost; /*Infos sur le host : pour gethostbyname */
  struct in_addr adresseIP; /* Adresse Internet au format reseau */
  struct sockaddr_in adresseSocket;
  int hSocket,ret,tailleSockaddr_in;

  hSocket = socket(AF_INET, SOCK_STREAM, 0);
  if (hSocket == -1)
  {
    printf("Erreur de creation de la socket %d\n", errno);
    exit(1);
  }
  else printf("Creation de la socket OK\n");
  /* 2. Acquisition des informations sur l'ordinateur distant */
  if ( (infosHost = gethostbyname(adresse))==0)
  {
    printf("Erreur d'acquisition d'infos sur le host distant %d\n", errno);
    exit(1);
  }
  else printf("Acquisition infos host distant OK\n");
  memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
  printf("Adresse IP = %s\n",inet_ntoa(adresseIP));
  /* 3. Préparation de la structure sockaddr_in */
  memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
  adresseSocket.sin_family = AF_INET; /* Domaine */
  adresseSocket.sin_port = htons(Port);

  memcpy(&adresseSocket.sin_addr, infosHost->h_addr,infosHost->h_length);
  tailleSockaddr_in = sizeof(struct sockaddr_in);
  if (( ret = connect(hSocket, (struct sockaddr *)&adresseSocket, tailleSockaddr_in) )
  == -1)
  {
    printf("Erreur sur connect de la socket %d\n", errno);
    switch(errno)
    {
      case EBADF : printf("EBADF - hsocket n'existe pas\n");
      break;
      default : printf("Erreur inconnue ?\n");
    }
    close(hSocket);
    exit(1);
  }
  else printf("Connect socket OK\n");

  return hSocket;
}