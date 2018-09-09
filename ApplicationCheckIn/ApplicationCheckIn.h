#ifndef APPLICATIONCHECKIN_H
#define APPLICATIONCHECKIN_H
#include <stdio.h>
#include <stdlib.h> /* pour exit */
#include <string.h> /* pour memcpy */
#include <unistd.h>
//#include <sys/types.h>
#include <sys/socket.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>
#include <netinet/in.h> /* pour la conversion adresse reseau->format dot
ainsi que le conversion format local/format
reseau */
#include <netinet/tcp.h> /* pour la conversion adresse reseau->format dot */
#include <arpa/inet.h> /* pour la conversion adresse reseau->format dot */
#include "../Serveur_CheckIn/SocketsUtilities.h"
//#include "tcpiter.h"
#define DOC "DENY_OF_CONNEXION" /*  dans tcpiter.h */
#define EOC "END_OF_CONNEXION"
#define MAXSTRING 160 /* Longueur des messages */

#define LOGIN_OFFICER 1
#define LOGOUT_OFFICER 2
#define CHECK_TICKET 3
#define PAYMENT_DONE 4

void Config();
void Init();
int Login();
int MenuConnexion();
int MenuPrincipal();
int demandeTicket();
int sendMessage(char *);
int addLugagge(int,int);
char * rcvMessage();
#endif