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
    int         retVal = 0;
    ShmData     shm; //creating instance of ShmData structure
    ShmData*    shmPtr;
    shmPtr      = &shm; //creating pointer to ShmData structure
    char        filename[] = "/myfile";
    size_t      shmSize = sizeof(shmPtr);


    int fd = shm_open(filename, O_RDWR, 0666) != 0;
    if (fd == -1) {
        printf("Failed to create shared memory\n");
    }

    char* mapAddr = mmap(NULL, shmSize, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (mapAddr == MAP_FAILED) {
        printf("Failed to create map\n");
    }

    printf("[Client]: Waiting for valid data ...\n");

    while(shmPtr->status != VALID)
    {
        sleep(1);
    }

    printf("[Client]: Received %d\n",shmPtr->data);

    shmPtr->status = CONSUMED;

    if ((munmap(mapAddr, shmSize) != 0)) {
        printf("Failed to unmap map\n");
        exit(1);
    }

    printf("[Client]: Client exiting...\n");

    return(retVal);

}

