#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include "shm.h"

int main(int argc, char* argv[])
{
    int retVal = 0;
    int fd;
    ShmData *ptr;
    size_t shmSize = sizeof(shmPtr);

    fd = shm_open("/myfile", 0666, NULL);

    if ((mmap(NULL, 0, PROT_NONE, MAP_PRIVATE, 0, 0)) == -1) {
        printf("Failed to create mmap\n");
    }
    else {
        printf("hi");
    }

    printf("[Client]: Waiting for valid data ...\n");

    while((&shmPtr)->status != VALID)
    {
        sleep(1);
    }

    printf("[Client]: Received %d\n",(&shmPtr)->data);

    (&shmPtr)->status = CONSUMED;



    return(retVal);

}


/* <Use the POSIX "shm_open" API to open file descriptor with
 "O_RDWR" options and the "0666" permissions>

<Use the "mmap" API to memory map the file descriptor>

printf("[Client]: Waiting for valid data ...\n");

while(shmPtr->status != VALID)
 {
   sleep(1);
 }

printf("[Client]: Received %d\n",shmPtr->data);

shmPtr->status = CONSUMED;

<use the "munmap" API to unmap the pointer>

printf("[Client]: Client exiting...\n"); */




