#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int no_of_consumers;
int no_of_chairs;
int no_of_chairs_occupied;

void * barber_routine(void *);
void * consumer_routine(void *);

//declare global mutex and condition variables
pthread_mutex_t chair_mutex;
pthread_mutex_t barber_mutex;
pthread_cond_t full_chairs_cond;
pthread_cond_t empty_chairs_cond;
pthread_cond_t waiting_on_barber_cond;
pthread_cond_t sitting_on_chair_cond;
pthread_cond_t being_served_cond;

typedef struct {
	int min_pace;
	int max_pace;

} pace_t;

int main(int argc, char ** argv)
{
	pthread_t *threads; //system thread id
	int *t_ids; //user-defined thread id
	pace_t barbers_pace;
	pace_t consumer_rate;
 	int k, rc, t;

	no_of_chairs_occupied = 0;
	
	// ask for number of chairs
	printf("Enter the number of seats at the Barber's shop (int): \n");
	scanf("%d", &no_of_chairs);
	last_ticket_no = no_of_chairs;
	
	// tickets = (_Bool *) calloc(no_of_chairs, sizeof(_Bool));
	
	// ask for the total number of consumers.
	printf("Enter the total number of consumers (int): \n");
	scanf("%d", &no_of_consumers);
	
	//ask for barber's working pace
	printf("Enter barber's min pace (int): \n");
	scanf("%d", &barbers_pace.min_pace);

	printf("Enter barber's max pace (int): \n");
	scanf("%d", &barbers_pace.max_pace);
	
	//ask for consumers' arrival rate
	printf("Enter consumers min pace (int): \n");
	scanf("%d", &consumer_rate.min_pace);

	printf("Enter consumers max pace (int): \n");
	scanf("%d", &consumer_rate.max_pace);
		
	//Initialize mutex and condition variable objects 
	pthread_mutex_init(&chair_mutex, NULL);
	pthread_mutex_init(&barber_mutex, NULL);
	pthread_cond_init(&full_chairs_cond, NULL);
	pthread_cond_init(&empty_chairs_cond, NULL);
	pthread_cond_init(&waiting_on_barber_cond, NULL);
	pthread_cond_init(&being_served_cond, NULL);
	pthread_cond_init(&sitting_on_chair_cond, NULL);

				
	threads = malloc((no_of_consumers+1) * sizeof(pthread_t)); //total is No_Of_Consuers + 1 to include barber 
	if(threads == NULL){
		fprintf(stderr, "threads out of memory\n");
		exit(1);
	}	
	t_ids = malloc((no_of_consumers+1) * sizeof(int)); //total is No_Of_Consuers + 1 to include barber
	if(t_ids == NULL){
		fprintf(stderr, "t out of memory\n");
		exit(1);
	}	
	
	//create the barber thread.
	rc = pthread_create(&threads[0], NULL, barber_routine, (void *) &barbers_pace); //barber routine takes barber_pace as the arg
	if (rc) {
		printf("ERROR; return code from pthread_create() (barber) is %d\n", rc);
		exit(-1);
	}

	//create consumers according to the arrival rate
	srand(time(0));
	for (k = 1; k<no_of_consumers; k++)
	{
		sleep((int)rand() % (consumer_pace.max_pace - consumer_pace.min_pace + 1) + consumer_pace.min_pace); //sleep a few second before creating a thread
		t_ids[k] = k;
		rc = pthread_create(&threads[k], NULL, consumer_routine, (void *)&t_ids[k]); //consumer routine takes thread id as the arg
		if (rc) {
			printf("ERROR; return code from pthread_create() (consumer) is %d\n", rc);
			exit(-1);
		}
	}
    
	//join consumer threads.
	for (k = 1; k<no_of_consumers; k++) 
	{
		pthread_join(threads[k], NULL);
	}
	
	//terminate the barber thread using pthread_cancel().
	pthread_cancel(threads[0]); 
	
	//deallocate allocated memory
	free(threads);
	free(t_ids);
	
	//destroy mutex and condition variable objects
	pthread_mutex_destroy(&chair_mutex);
	pthread_mutex_destroy(&barber_mutex);
	pthread_cond_destroy(&full_chairs_cond);
	pthread_cond_destroy(&empty_chairs_cond);
	pthread_cond_destroy(&waiting_on_barber_cond);
	pthread_cond_destroy(&sitting_on_chair_cond);
	pthread_cond_destroy(&being_served_cond);
	
	pthread_exit(EXIT_SUCCESS);
}

void * barber_routine(void * arg) {
	pace_t *work_pace;
	int serve_time = 3;
	work_pace = (pace_t*) arg;
	while (1)
	{
		sleep (*serve_time);
		pthread_mutex_lock(&chair_mutex);
			if(no_of_chairs_occupied == 0) {
				printf("Barber: The number of free seats is %d. No customers waiting and I'll go to sleep.\n", no_of_chairs);
                pthread_cond_wait(&empty_chairs_cond, &chair_mutex);
			} else {
				printf("Barber: The number of free seats now is %d. Call next customer. \n", no_of_chairs - no_of_chairs_occupied);
				printf("Barber: wait for the customer to sit on the barber chair.\n");
				pthread_cond_signal(&waiting_on_barber_cond);
				pthread_cond_wait(&sitting_on_chair_cond, &chair_mutex);
				
				pthread_mutex_trylock(&barber_mutex);
				no_of_chairs_occupied--;
				
				printf("Barber: Start serving the customer.\n");
				sleep((int)rand() % (work_pace->max_pace - work_pace->min_pace) + work_pace->min_pace);

				pthread_cond_signal(&being_served_cond);
				printf("Barber: finished cutting. Bye!\n");
				pthread_mutex_unlock(&barber_mutex);
			}
		pthread_mutex_unlock(&chair_mutex);
	}
}

void * consumer_routine(void * arg) {
	
	int consumer_number = *((int *)arg);
	printf("Customer %d arrives.\n", consumer_number);
	
	pthread_mutex_trylock(&barber_mutex);
    if (no_of_chairs_occupied == no_of_chairs) {
		printf("Customer %d: oh no! all seats have been taken and I'll leave now!\n", consumer_number);

	} else { 
        printf("Customer %d: I'm lucky to get a free seat from %d\n", consumer_number, no_of_chairs);

        if(no_of_chairs_occupied == 0) {
            pthread_cond_signal(&empty_chairs_cond);

        }

		no_of_chairs_occupied++;

		// -- wait for barber seat to be free
		pthread_cond_wait(&waiting_on_barber_cond, &barber_mutex);
		printf("Customer %d: I'm to be served.\n", consumer_number);
		printf("Customer %d: sit on the barber chair.\n", consumer_number);
		pthread_cond_signal(&empty_chairs_cond);

		printf("Customer %d: I'm being served.\n", consumer_number);

		// -- wait for barber to finish cutting hair
		pthread_cond_wait(&being_served_cond, &barber_mutex);
		printf("Customer %d: Well done. Thank barber, bye!\n", consumer_number);
		
	}

	pthread_mutex_unlock(&barber_mutex);

}
