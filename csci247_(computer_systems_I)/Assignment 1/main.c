/* Alec Harb
 * CSCI 247
 * November 5, 2018
 *
 * For this assignment, I was unable to get the clock function to work so I was unable to get values for the duration of each thread. I do believe, however, that
 * in the third row of TimeStamp, the first index may be the delta value of the time taken to execute the "doprocess" function for each thread. However, the
 * values for the time elapsed are inaccurate. I believe this because when using a much quicker "doprocess" function (smaller while loop), all the timestamp values
 * in the first index of the third row roughly summed to the total duration of the program.
 *
 *
 * I do not think I called the threads correctly because the duration of each compilation was inconsistent. This may be due to the fact that
 * when I signal the condition variable, I do so in chronological order based off of the thread count rather than the threads' respective scheduling policies.
 * Additionally, I was unable to figure out the "setschedparam" operation in the threadFunction so I set the policies to a fixed value in the main function. I think
 * this fact, in addition to the chronological order of the thread signals, created a program that compiles all the threads, but does so in an inconsistent manner.
 *
 * Sometimes, when I run my program, it goes on for a very long time. This may be do to an instance where a thread is waiting for a signal to continue, but the signal has already
 * been called so it waits indefinitely for a signal, even though that signal will not be called again.
 *
 * When I changed the "doprocess" function to print the value of a long operation (count to a high number, then decrement back to 0), all 9 functions printed a value of 0
 * which leads me to believe that I did not have any race conditions in my threads and that each thread was able to run without interruption or sharing of variables.
 *
 * In regards to analyzing the data, unless a computer has multiple cores, only one thread can utilize the cpu at a time. All the threads should have roughly the same
 * compile time because they all run the same "doprocess" function. Since the get time function is called after the wait condition is signaled, each thread should
 * take the same amount of time to finish executing. A thread with a higher priority will always preempt a thread with a lower priority. Additionally, the SCHED_FIFO (realtime)
 * thread will continue running until it is finished with its functions wheareas a SCHED_RR (realtime) will run for a certain amount of time and then yield so the
 * scheduler can reallocate usage for the cpu.
 * */

#include <time.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <inttypes.h>

//========================================================================================================================================================
//#defines

#define MAX_THREAD_COUNT 9
#define MAX_TASK_COUNT 3



//========================================================================================================================================================
//Typedefs
typedef struct{
    int threadCount;
    pthread_t threadId;
    int threadPolicy;
    int threadPri;
    long processTime;
    int64_t timeStamp[MAX_TASK_COUNT+1];
    time_t startTime;
    time_t endTime;
}ThreadArgs;

//Globals
//Try to change this to use a single condition variable
pthread_mutex_t g_ThreadMutex [MAX_THREAD_COUNT];
pthread_cond_t g_conditionVar [MAX_THREAD_COUNT];
ThreadArgs g_ThreadArgs[MAX_THREAD_COUNT];

//========================================================================================================================================================
void InitGlobals(void)
{
// Initialize all globals

}

//========================================================================================================================================================
void DisplayThreadSchdAttributes( pthread_t threadID, int policy, int priority )
{

    printf("\nDisplayThreadSchdAttributes:\n threadID = 0x%lx\n policy = %s\n priority = %d\n",
           threadID,
           (policy == SCHED_FIFO) ? "SCHED_FIFO" :
           (policy == SCHED_RR)	? "SCHED_RR" :
           (policy == SCHED_OTHER) ? "SCHED_OTHER" :
           "???",
           priority);
}

//========================================================================================================================================================
void DisplayThreadArgs(ThreadArgs*	myThreadArg) {
    int i, y;

    if (myThreadArg) {
        DisplayThreadSchdAttributes(myThreadArg->threadId, myThreadArg->threadPolicy, myThreadArg->threadPri);
        printf(" startTime = %s endTime = %s", ctime(&myThreadArg->startTime), ctime(&myThreadArg->endTime));
        printf(" TimeStamp [%"PRId64"]\n", myThreadArg->timeStamp[0]);

        for (y = 1; y < MAX_TASK_COUNT + 1; y++) {
            printf(" TimeStamp [%"PRId64"] Delta [%"PRId64"]us\n", myThreadArg->timeStamp[y],
                   (myThreadArg->timeStamp[y] - myThreadArg->timeStamp[y - 1]));
        }
    }
}


    //========================================================================================================================================================
    void DoProcess(void)
    {
        unsigned int longVar = 1;

        while(longVar < 0xffffffff) longVar++;

    }

    //========================================================================================================================================================


    void* threadFunction(void *arg)
    {
        int y = 0;

        ThreadArgs*	myThreadArg;
        myThreadArg = (ThreadArgs*)arg;

        /* struct sched_param param;
        pthread_setschedparam(pthread_self(), SCHED_FIFO, &param);
         */

        struct timespec tms;

        // Initialize mutex for current thread
        if (pthread_mutex_init(&g_ThreadMutex[myThreadArg->threadCount], NULL) != 0) {
            printf("Failed to create initialize mutex for thread %d\n", myThreadArg->threadCount);
        }

        // lock mutex for current thread
        if (pthread_mutex_lock(&g_ThreadMutex[myThreadArg->threadCount]) != 0) {
            printf("Failed to lock mutex %d\n", myThreadArg->threadCount);
        }

        // wait for condition variable to allow thread to continue
        while (!pthread_cond_signal(&g_conditionVar[myThreadArg->threadCount])) {
            pthread_cond_wait(&g_conditionVar[myThreadArg->threadCount], &g_ThreadMutex[myThreadArg->threadCount]);
        }

        // after condition variable is met, start "DoProcess" and get time values
        clock_gettime(CLOCK_REALTIME, &tms);
        DoProcess();
        myThreadArg->startTime = tms.tv_sec * 1000000;
        myThreadArg->timeStamp[1] = tms.tv_sec *1000000;
        myThreadArg->timeStamp[2] += tms.tv_nsec/1000;
        if(tms.tv_nsec % 1000 >= 500 ) myThreadArg->timeStamp[y+1]++;

        // unlock mutex for next thread
        pthread_mutex_unlock( &g_ThreadMutex[myThreadArg->threadCount] );

    }

    //========================================================================================================================================================
    int main (int argc, char *argv[]) {

        // Did not call "InitGlobals" because function is empty

        // Initialize threadCount to 0 and priority to 5
        int threadCount = 0;
        int priority = 10;

        //Create 9 threads inside main thread
        while (threadCount < MAX_THREAD_COUNT) {

            pthread_attr_t *threadAttrib;

            if ((pthread_create(&(g_ThreadArgs[threadCount].threadId), threadAttrib, &threadFunction,
                                &g_ThreadArgs[threadCount]))) {
                printf("pthread_create failed for thread number [%d] : [%d]\n", threadCount);
            }

            // Initialize first 3 threads to SCHED_FIFO with a set priority
            if (threadCount < 3) {

                g_ThreadArgs[threadCount].threadPolicy = SCHED_FIFO;
                g_ThreadArgs[threadCount].threadPri = priority;
                g_ThreadArgs[threadCount].threadCount = threadCount;

                priority += 1;
            }

                // Initialize 4-6 threads to SCHED_RR with a set priority
            else if (threadCount > 2 && threadCount < 6) {

                g_ThreadArgs[threadCount].threadPolicy = SCHED_RR;
                g_ThreadArgs[threadCount].threadPri = priority;
                g_ThreadArgs[threadCount].threadCount = threadCount;

                priority += 1;
            }

                // Initialize last 3 threads to SCHED_OTHER with no priority because their default is 0
            else {

                g_ThreadArgs[threadCount].threadPolicy = SCHED_OTHER;
                g_ThreadArgs[threadCount].threadCount = threadCount;
            }

            threadCount++;
        }

        // All 9 threads wait until signaled by the main thread to run
        for (int i = 0; i < MAX_THREAD_COUNT; i++) {
        pthread_cond_signal(&g_conditionVar[i]);
        pthread_join(g_ThreadArgs[i].threadId, NULL);
    }

        for (int j = 0; j < MAX_THREAD_COUNT; j++) {
            DisplayThreadArgs(&g_ThreadArgs[j]);
        }
    }

    //========================================================================================================================================================



/* 

************* HINTS ******************

========================================================================================================================================================
Every time you run into issues with usage of an API, please look up samples on how that API is used here...

http://www.yolinux.com/TUTORIALS/LinuxTutorialPosixThreads.html

========================================================================================================================================================


Please check the return values from all system calls and print an error message in all error cases including the error code.. That will help catch errors quickly.
========================================================================================================================================================


You can use the following technique to pass the address of the element corresponding to a particular thread to the thread function...

	void* threadFunction(void *arg)
	{
		ThreadArgs*	myThreadArg;

		myThreadArg = (ThreadArgs*)arg;

	}


	int main (int argc, char *argv[]) 
	{

		while(threadCount < MAX_THREAD_COUNT)
		{
		...
			if( pthread_create(&(g_ThreadArgs[threadCount].threadId), &threadAttrib, &threadFunction, &g_ThreadArgs[threadCount]) )	
		...

		}
	}
========================================================================================================================================================

Here is the usage for clock_gettime

	clock_gettime(CLOCK_REALTIME, &tms);
	myThreadArg->timeStamp[y+1] = tms.tv_sec *1000000;
	myThreadArg->timeStamp[y+1] += tms.tv_nsec/1000;
	if(tms.tv_nsec % 1000 >= 500 ) myThreadArg->timeStamp[y+1]++;

========================================================================================================================================================

Here is how you wait on a condition event 

	pthread_mutex_lock ( &g_ThreadMutex[myThreadArg->threadCount] );
	pthread_cond_wait ( &g_conditionVar[myThreadArg->threadCount], &g_ThreadMutex[myThreadArg->threadCount] ); 
	pthread_mutex_unlock( &g_ThreadMutex[myThreadArg->threadCount] );

========================================================================================================================================================

Note that this sample is changing the policy of the current thread... so if you follow this sample, make sure you are making the call from the thread function.


	http://man7.org/linux/man-pages/man3/pthread_setschedparam.3.html

	if (main_sched_str != NULL) {
	if (!get_policy(main_sched_str[0], &policy))
		usage(argv[0], "Bad policy for main thread (-m)\n");
		param.sched_priority = strtol(&main_sched_str[1], NULL, 0);

	s = pthread_setschedparam(pthread_self(), policy, &param);
	if (s != 0)
		handle_error_en(s, "pthread_setschedparam");
	}
	
========================================================================================================================================================
For those confused about my comment on trying to using a single Condition variable instead of an array... please read the following...

http://pubs.opengroup.org/onlinepubs/9699919799/functions/pthread_cond_signal.html

You can use the broadcast API to wake multiple threads waiting on the same condition variable.

For those who really like to go deeper, know that you have access to the code for most of the Linux system APIs... here is the code pthread_cond_broadcast...

https://code.woboq.org/userspace/glibc/nptl/pthread_cond_broadcast.c.html

========================================================================================================================================================
*/

