/*
 * In the previous assignment we learned that timers were based off of the clock and would periodically return values whenever prompted by the program. In this
 * assignment we used a similar idea: create a server and have it wait on the client to prompt it to finish running. In this fashion, a real implementation of
 * this idea of inter-process communication could be to create a server that waits for a ping from a client(consumer), and when pinged by the client, will provide
 * them with the information stored on the server. The server could listen multiple times every second (periodically) so the client-server-client communication
 * is quick.
 * */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include "shm.h"

int main(int argc, char* argv[])
{
    int         retVal = 0;
    ShmData     shm; //creating instance of ShmData structure
    ShmData*    shmPtr;
    shmPtr      = &shm; //creating pointer to ShmData structure
    char        filename[] = "/myfile";
    size_t      shmSize = sizeof(shmPtr);

    if (argc != 2) {
        printf("Invalid number of arguments\n");
        exit(1);
    }

    int fd = shm_open(filename, O_CREAT | O_RDWR, 0666) != 0;
    if (fd == -1) {
        printf("Failed to create shared memory\n");
    }

    ftruncate(fd, shmSize);

    char* mapAddr = mmap(NULL, shmSize, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (mapAddr == MAP_FAILED) {
        printf("Failed to create map\n");
    }

    shmPtr->status = INVALID;
    shmPtr->data = atoi(argv[1]);
    shmPtr->status = VALID;

    printf("[Server]: Server data Valid... waiting for client\n");

    while(shmPtr->status != CONSUMED)
    {
        sleep(1);
    }

    printf("[Server]: Server Data consumed!\n");

    if ((munmap(mapAddr, shmSize) != 0)) {
        printf("Failed to unmap map\n");
        exit(1);
    }

    close(fd);
    shm_unlink(filename);

    printf("[Server]: Server exiting...\n");

    return(retVal);
}





