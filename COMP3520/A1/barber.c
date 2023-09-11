#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int no_of_consumers;
int no_of_chairs;
int last_ticket_no;
int last_ticket_called;
int no_of_chairs_occupied;
int ticket_number;
int barber_busy;

void * barber_routine(void *);
void * consumer_routine(void *);

//declare global mutex and condition variables
pthread_mutex_t chair_mutex;
pthread_mutex_t barber_mutex;
pthread_cond_t full_chairs_cond;
pthread_cond_t empty_chairs_cond;
pthread_cond_t waiting_on_barber_cond;
pthread_cond_t waiting_on_customer_cond;
pthread_cond_t waiting_on_assistant_cond;
pthread_cond_t sitting_on_chair_cond;
pthread_cond_t being_served_cond;

_Bool* tickets;

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
	ticket_number = 0;
	barber_busy = 0;
	last_ticket_called = 0;

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
	pthread_cond_init(&waiting_on_assistant_cond, NULL);
	pthread_cond_init(&waiting_on_customer_cond, NULL);
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

	//create the assistant thread.
	rc = pthread_create(&threads[0], NULL, assistant_routine, (void *) NULL);
	if (rc) {
		printf("ERROR; return code from pthread_create() (assistant) is %d\n", rc);
		exit(-1);
	}

	//create consumers according to the arrival rate
	srand(time(0));
	for (k = 1; k<no_of_consumers; k++)
	{
		sleep((int)rand() % consumer_rate.max_pace) + consumer_rate.min_pace; //sleep a few second before creating a thread
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
	pthread_cond_destroy(&waiting_on_assistant_cond);
	pthread_cond_destroy(&waiting_on_customer_cond);
	pthread_cond_destroy(&sitting_on_chair_cond);
	pthread_cond_destroy(&being_served_cond);
	
	pthread_exit(EXIT_SUCCESS);
}

void * assistant_routine(void * arg) {
	int *work_pace;
	int serve_time = 15;
	work_pace = (int *) arg;
	while (0) {
		pthread_mutex_lock(&barber_mutex);

		if(no_of_chairs_occupied == 0) {
			printf("Assistant: I’m waiting for customers.");
			pthread_cond_wait(&empty_chairs_cond);
		}

		if(barber_busy) {
			printf("Assistant: I’m waiting for barber to become available.");
			pthread_cond_wait(&waiting_on_barber_cond)
		}

		printf("Assistant: Call one customer with a ticket numbered n.");
		pthread_cond_broadcast(&waiting_on_assistant_cond);
		pthread_cond_signal(&waiting_on_customer_cond);

		pthread_mutex_unlock(&barber_mutex);
	}

	printf("Assistant: Hi Barber, we’ve finished the work for the day.");
	pthread_cond_broadcast(&empty_Chairs_cond);
}

void * barber_routine(void * arg) {
	pace_t* serve_pace;
	serve_pace = (pace_t *) arg;

	boolean work_ended = false;

	while (!work_ended) {
		sleep((int)rand() % 5 + 1);
		pthread_mutex_lock(&chair_mutex);
		
		pthread_cond_wait(&waiting_on_customer_cond, &chair_mutex);

		// Cut hair for customer
		sleep(((int) rand() % (serve_pace->max_pace - sleep_pace->min_pace)) + serve_pace->min_pace);

		pthread_cond_signal(&being_served_cond);

		pthread_mutex_unlock(&chair_mutex);
	}

}

void * consumer_routine(void * arg) {
	
	int* consumer_number = (int *)arg;
	int ticket_number = -1;
	printf("Customer [%d]: I have arrived at the barber shop.", *consumer_number);
	
	pthread_mutex_lock(&barber_mutex);
    	if (no_of_chairs_occupied == no_of_chairs) {
		printf("Customer [%d]: oh no! all seats have been taken and I'll leave now!\n", consumer_number);

	} else { 
		
		// int i = 0;
		// while(i < no_of_chairs && ticket_number == -1) {
		// 	if(tickets[i] == 0) {
		// 		ticket_number = i;
		// 		tickets[i] = 1

		// 	}

		// 	i++
		// }

		
		last_ticket_no = last_ticket_no > no_of_chairs ? 1 : last_ticket_no + 1;
	
		printf("Customer [%d]: I'm lucky to get a free seat and a ticket numbered %d\n", consumer_number, last_ticket_no);

		if(no_of_chairs_occupied == 0) {
		    pthread_cond_signal(&empty_chairs_cond);

		}


		no_of_chairs_occupied++;

		// Signal assistant if you are first customer
		if(no_of_chairs_occupied == 1) {
			pthread_cond_signal(&empty_chairs_cond);
		}

		// -- wait for barber seat to be free
		int ticket_to_be_called = last_ticket_called > no_of_chairs ? 1 : last_ticket_called + 1;
		do {
			pthread_cond_wait(&waiting_on_assistant_cond, &barber_mutex);

		} while(ticket_to_be_called != last_ticket_no);

		last_ticket_called = ticket_to_be_called;
		no_of_chairs_occupied--;

		printf("Customer [%d]: I'm to be served.\n", consumer_number);
		printf("Customer [%d]: sit on the barber chair.\n", consumer_number);

		printf("Customer [%d]: I'm being served.\n", consumer_number);

		// // -- wait for barber to finish cutting hair
		pthread_cond_wait(&being_served_cond, &barber_mutex);
		printf("Customer [%d]: Well done. Thank barber, bye!\n", consumer_number);

		// tickets[ticket_number] = 0;
	}

	pthread_mutex_unlock(&barber_mutex);

}
