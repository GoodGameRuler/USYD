/*
   COMP3520 Exercise 4 - FCFS Dispatcher

usage:

./fcfs <TESTFILE>
where <TESTFILE> is the name of a job list
*/

/* Include files */
#include "dispatch.h"
#include "pcb.h"
#include <stdio.h>
#include <stdlib.h>

int timer;
int turnaround_time;
double av_turnaround_time = 0.0, av_wait_time = 0.0;

int main (int argc, char *argv[])
{
    /*** Main function variable declarations ***/

    FILE * input_list_stream = NULL;

    PcbPtr dispatched_process = NULL;
    PcbPtr process = NULL;

    // Dispatch Queue
    PcbPtr dispatch_queue = NULL;

    PcbPtr lv0_ready_queue = NULL;
    PcbPtr lv1_ready_queue = NULL;
    PcbPtr lv2_ready_queue = NULL;

    //int timer = 0;
    int quantum_0, quantum_1;
    int max_iter;

    // int turnaround_time;
    // double av_turnaround_time = 0.0, av_wait_time = 0.0;
    int n = 0;

    // Populate quantim values
    printf("Enter Qunatam Value 1 for Ready Queue Level 0\n");
    scanf("%d", &quantum_0);
    if (quantum_0 <= 0)
    {
        printf("Time quantum must be greater than 0.\n");
        exit(EXIT_FAILURE);
    }

    printf("Enter Qunatam Value 2 for Ready Queue Level 1\n");
    scanf("%d", &quantum_1);
    if (quantum_1 <= 0)
    {
        printf("Time quantum must be greater than 0.\n");
        exit(EXIT_FAILURE);
    }

    printf("Enter Max Number of Iterations for Level 1\n");
    scanf("%d", &max_iter);
    if (max_iter <= 0)
    {
        printf("Max num of iterations must be greater than 0.\n");
        exit(EXIT_FAILURE);
    }

    //  1. Populate the FCFS queue

    if (argc <= 0)
    {
        fprintf(stderr, "FATAL: Bad arguments array\n");
        exit(EXIT_FAILURE);
    }
    else if (argc != 2)
    {
        fprintf(stderr, "Usage: %s <TESTFILE>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    if (!(input_list_stream = fopen(argv[1], "r")))
    {
        fprintf(stderr, "ERROR: Could not open \"%s\"\n", argv[1]);
        exit(EXIT_FAILURE);
    }

    while (!feof(input_list_stream)) {  // put processes into fcfs_queue
        process = createnullPcb();
        if (fscanf(input_list_stream,"%d, %d",
                    &(process->arrival_time), 
                    &(process->service_time)) != 2) {
            free(process);
            continue;
        }
        process->remaining_cpu_time = process->service_time;
        process->status = PCB_INITIALIZED;
        dispatch_queue = enqPcb(dispatch_queue, process);
        n++;
    }

    // int time_quantum = 1; 
    int quantum = 0;
    PcbPtr current_process = NULL;
    // PcbPtr current_queue = NULL;
    // PcbPtr next_queue; 
    // int curr_queue;


    //  2. Whenever there is a running process or the FCFS queue is not empty:

    while (current_process || dispatched_process || dispatch_queue || lv0_ready_queue || lv1_ready_queue || lv2_ready_queue)
    {
        if (current_process)
            //      i. If there is a currently running process;
        {
            //          a. Decrement the process's remaining_cpu_time variable;
            current_process->remaining_cpu_time -= quantum;

            //          b. If the process's allocated time has expired:
            if (current_process->remaining_cpu_time <= 0)
            {
                //              A. Terminate the process;
                // terminatePcb(current_process);

                //		calculate and acumulate turnaround time and wait time
                turnaround_time = timer - current_process->arrival_time;
                av_turnaround_time += turnaround_time;
                av_wait_time += turnaround_time - current_process->service_time;   

                //              B. Deallocate the PCB (process control block)'s memory
                printf("Process %d-%d: terminated \n", current_process->arrival_time, current_process->service_time);
                terminatePcb(current_process);
                free(current_process);
                current_process = NULL;
                // current_queue = NULL;
                // next_queue = NULL;
                // curr_queue = -1;

            } else {

                switch (current_process->level_no) {
                    case LEVEL0:
                        current_process->level_no = LEVEL1;
                        lv1_ready_queue = enqPcb(lv1_ready_queue, current_process);
                        printf("Process %d-%d: queued from level 0 to level 1 ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
                        suspendPcb(current_process);
                        current_process->num_iter = 0;
                        current_process = NULL;
                        
                        break;

                    case LEVEL1:
                        if(current_process->num_iter < 0) {
                            exit(EXIT_FAILURE);
                        }

                        if(max_iter - current_process->num_iter <= 0) {
                            current_process->level_no = LEVEL2;
                            lv2_ready_queue = enqPcb(lv2_ready_queue, current_process);
                            printf("Process %d-%d: queued from level 1 to level 2 ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
                            suspendPcb(current_process);
                            current_process = NULL;
                            

                        } else {
                            current_process->level_no = LEVEL1;
                            lv1_ready_queue = enqPcb(lv1_ready_queue, current_process);
                            printf("Process %d-%d: requeued into round robin level 1 ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
                            suspendPcb(current_process);
                            current_process->num_iter++;
                            current_process = NULL;
                        }


                        break;
                    case LEVEL2:
                        if(lv0_ready_queue || lv1_ready_queue) {
                            // Queue to Head
                            lv2_ready_queue = enqPcb(current_process, lv2_ready_queue);
                            printf("Process %d-%d: requeued into level 2 head ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
                            suspendPcb(current_process);
                            current_process = NULL;
                        }

                        break;
                    default:
                        break;
                }
            }
        }
        
        //      ii. If there is no running process and there is a process ready to run:
        while (!dispatched_process && dispatch_queue && dispatch_queue->arrival_time <= timer)
        {
            //          Dequeue the process at the head of the queue, set it as currently running and start it
            dispatched_process = deqPcb(&dispatch_queue);
            lv0_ready_queue = enqPcb(lv0_ready_queue, dispatched_process);
            printf("Process %d-%d: dispatched into level 0 ready queue at time %d\n", dispatched_process->arrival_time, dispatched_process->service_time, timer);
            dispatched_process = NULL;
        }

        //    iii. If no process currently running && RR queue is not empty:
        if(lv0_ready_queue && !current_process) {
            current_process = deqPcb(&lv0_ready_queue);
            printf("Process %d-%d: ready from level 0 ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
            // time_quantum = quantum_1;
            // curr_queue = 1;
            current_process->level_no = LEVEL0;
            

        } else if (lv1_ready_queue && !current_process) {
            current_process = deqPcb(&lv1_ready_queue);
            printf("Process %d-%d: ready from level 1 ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
            // time_quantum = quantum_1;
            // curr_queue = 2;
            current_process->level_no = LEVEL1;


        } else if (lv2_ready_queue && !current_process) {
            current_process = deqPcb(&lv2_ready_queue);
            printf("Process %d-%d: ready from level 2 ready queue at time %d\n", current_process->arrival_time, current_process->service_time, timer);
            // time_quantum = 1;
            // curr_queue = 3;
            current_process->level_no = LEVEL2;

        }

        if(current_process)
            startPcb(current_process);

        //      iv. Increment the dispatcher's timer;

        quantum = 1;
        if(current_process) {
            switch (current_process->level_no) {
                case LEVEL0:
                    quantum = current_process && current_process->remaining_cpu_time < quantum_0 ?
                        current_process->remaining_cpu_time :
                        !(current_process) ? 1 : quantum_0;
                    break;
                case LEVEL1:
                    quantum = current_process && current_process->remaining_cpu_time < quantum_1 ?
                        current_process->remaining_cpu_time :
                        !(current_process) ? 1 : quantum_1;
                    break;
                case LEVEL2:
                    quantum = 1;
                    break;
                
                default:
                    quantum = 1;
                    break;
            
            }

        }
        sleep(quantum);
        timer += quantum;

            
        //       v. Increment dispatcher timer;


        //      vi. Go back to 2.
    }

    //  print out average turnaround time and average wait time
    av_turnaround_time = av_turnaround_time / n;
    av_wait_time = av_wait_time / n;
    printf("Average turnaround time = %f\n", av_turnaround_time);
    printf("Average wait time = %f\n", av_wait_time);
    printf("Total time = %d\n", timer);

    //  3. Terminate the FCFS dispatcher
    exit(EXIT_SUCCESS);
}
