#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// Sleep Func
#include <unistd.h>

// MACROS
#define CUST_TEXT ("Customer [%d]: ")
#define ASIT_TEXT ("Assistant [A]: ")
#define BARB_TEXT ("Barber    [B]: ")

// Customer Vars
int no_of_customers;
int no_of_barbers;
int no_of_chairs;
int new_ticket_no;
int last_ticket_called;
int no_of_chairs_occupied;
int ticket_number;
int ticket_already_called;
int num_customer_finished;
int num_customer_visited;

// Barber Vars
void *barber_routine(void *);
void *customer_routine(void *);
void *assistant_routine(void *);

// declare global mutex and condition variables
/* pthread_mutex_t barber_mutex; */
pthread_mutex_t customer_mutex;
pthread_cond_t full_chairs_cond;
pthread_cond_t empty_chairs_cond;
pthread_cond_t waiting_on_barber_cond;
/* pthread_cond_t waiting_on_customer_cond; */
/* pthread_cond_t confirm_customer_left_cond; */
pthread_cond_t waiting_on_assistant_cond;
pthread_cond_t sitting_on_chair_cond;
pthread_cond_t being_served_cond;
pthread_cond_t confirm_customer_cond;
pthread_cond_t waiting_all_customers_left_cond;

_Bool *tickets;

typedef struct {
  int min_pace;
  int max_pace;

} pace_t;

typedef struct {
  int customer_ticket_no;
  int customer_id_no;
  int barber_busy;
  int barber_id;
  int work_ended;
  pthread_cond_t confirm_customer_left_cond;
  pthread_cond_t waiting_on_customer_cond;
  pthread_cond_t being_served_cond;
  pthread_cond_t waiting_on_barber_cond;
  pthread_mutex_t barber_n_mutex;

} barber_info;

pace_t barbers_pace;
barber_info *barb_infos; // user-defined thread id
barber_info *new_barber_info;

/* barber_info *barber_curr_info; */

int main(int argc, char **argv) {
  pthread_t *threads; // system thread id
  int *t_ids;         // user-defined thread id
  pace_t customer_rate;
  int k, rc, t;

  no_of_chairs_occupied = 0;
  ticket_number = 0;
  last_ticket_called = 0;
  ticket_already_called = 0;
  num_customer_finished = 0;
  num_customer_visited = 0;

  /* barber_info b1_info; */
  /* b1_info.barber_busy = 0; */
  /* b1_info.customer_ticket_no = -1; */
  /* b1_info.customer_id_no = -1; */

  /* barber_curr_info = &b1_info; */

  // ask for number of chairs
  printf("Enter the number of seats at the Barber's shop (int): \n");
  scanf("%d", &no_of_chairs);
  new_ticket_no = no_of_chairs;

  // tickets = (_Bool *) calloc(no_of_chairs, sizeof(_Bool));

  // ask for the total number of customers.
  printf("Enter the total number of customers (int): \n");
  scanf("%d", &no_of_customers);

  // ask for the total number of barbers.
  printf("Enter the total number of barbers (int): \n");
  scanf("%d", &no_of_barbers);

  // ask for barber's working pace
  printf("Enter barber's min pace (int): \n");
  scanf("%d", &barbers_pace.min_pace);

  printf("Enter barber's max pace (int): \n");
  scanf("%d", &barbers_pace.max_pace);

  // ask for customers' arrival rate
  printf("Enter customers min pace (int): \n");
  scanf("%d", &customer_rate.min_pace);

  printf("Enter customers max pace (int): \n");
  scanf("%d", &customer_rate.max_pace);

  // Initialize mutex and condition variable objects
  /* pthread_mutex_init(&barber_mutex, NULL); */
  pthread_mutex_init(&customer_mutex, NULL);
  pthread_cond_init(&full_chairs_cond, NULL);
  pthread_cond_init(&empty_chairs_cond, NULL);
  pthread_cond_init(&waiting_on_barber_cond, NULL);
  pthread_cond_init(&waiting_on_assistant_cond, NULL);
  /* pthread_cond_init(&confirm_customer_left_cond, NULL); */
  /* pthread_cond_init(&waiting_on_customer_cond, NULL); */
  pthread_cond_init(&being_served_cond, NULL);
  pthread_cond_init(&sitting_on_chair_cond, NULL);
  pthread_cond_init(&confirm_customer_cond, NULL);
  pthread_cond_init(&waiting_all_customers_left_cond, NULL);

  threads = malloc(
      (no_of_customers + no_of_barbers + 1) *
      sizeof(pthread_t)); // total is No_Of_Consuers + 1 to include barber
  if (threads == NULL) {
    fprintf(stderr, "threads out of memory\n");
    exit(1);
  }
  t_ids = malloc((no_of_customers + 1) *
                 sizeof(int)); // total is No_Of_Consuers + 1 to include barber
  if (t_ids == NULL) {
    fprintf(stderr, "t out of memory\n");
    exit(1);
  }

  barb_infos = malloc((no_of_barbers + 1) *
                      sizeof(barber_info)); // No of barbers created

  if (barb_infos == NULL) {
    fprintf(stderr, "t out of memory\n");
    exit(1);
  }

  // create the barber thread.
  /* rc = pthread_create( */
  /*     &threads[0], NULL, barber_routine, */
  /*     (void *)&barbers_pace); // barber routine takes barber_pace as the arg
   */
  /* if (rc) { */
  /*   printf("ERROR; return code from pthread_create() (barber) is %d\n", rc);
   */
  /*   exit(-1); */
  /* } */

  for (k = 0; k < no_of_barbers; k++) {
    sleep(1); // sleep a few second before creating a thread
    barb_infos[k].barber_id = k + 1;
    /* barb_infos[k].barber_cond = ; */
    pthread_cond_init(&barb_infos[k].confirm_customer_left_cond, NULL);
    pthread_cond_init(&barb_infos[k].waiting_on_customer_cond, NULL);
    pthread_cond_init(&barb_infos[k].being_served_cond, NULL);
    pthread_cond_init(&barb_infos[k].waiting_on_barber_cond, NULL);
    pthread_mutex_init(&barb_infos[k].barber_n_mutex, NULL);
    barb_infos[k].work_ended = 0;

    rc = pthread_create(
        &threads[k + no_of_customers + 1], NULL, barber_routine,
        (void *)&barb_infos[k]); // barber routine takes thread id as the arg

    if (rc) {
      printf("ERROR; return code from pthread_create() (barber) is %d\n", rc);
      exit(-1);
    }
  }

  // create the assistant thread.
  rc = pthread_create(&threads[0], NULL, assistant_routine, (void *)NULL);
  if (rc) {
    printf("ERROR; return code from pthread_create() (assistant) is %d\n", rc);
    exit(-1);
  }

  // create customers according to the arrival rate
  srand(time(0));
  for (k = 1; k < no_of_customers + 1; k++) {
    if (customer_rate.max_pace == customer_rate.min_pace) {
      sleep(customer_rate.max_pace);
    } else
      sleep((int)rand() % (customer_rate.max_pace - customer_rate.min_pace) +
            customer_rate
                .min_pace); // sleep a few second before creating a thread
    t_ids[k] = k;
    rc = pthread_create(
        &threads[k], NULL, customer_routine,
        (void *)&t_ids[k]); // customer routine takes thread id as the arg
    if (rc) {
      printf("ERROR; return code from pthread_create() (customer) is %d\n", rc);
      exit(-1);
    }
  }

  // join customer threads.
  for (k = 0; k < no_of_customers + 1; k++) {
    pthread_join(threads[k], NULL);
  }

  for (k = no_of_customers + 1; k < no_of_customers + no_of_barbers + 1; k++) {
    pthread_join(threads[k], NULL);
  }

  // terminate the barber thread using pthread_cancel().
  /* pthread_cancel(threads[0]); */

  // deallocate allocated memory
  free(threads);
  free(t_ids);

  // destroy mutex and condition variable objects
  // pthread_mutex_destroy(&barber_mutex);
  pthread_mutex_destroy(&customer_mutex);
  pthread_cond_destroy(&full_chairs_cond);
  pthread_cond_destroy(&empty_chairs_cond);
  pthread_cond_destroy(&waiting_on_barber_cond);
  pthread_cond_destroy(&waiting_on_assistant_cond);
  /* pthread_cond_destroy(&waiting_on_customer_cond); */
  /* pthread_cond_destroy(&confirm_customer_left_cond); */
  pthread_cond_destroy(&sitting_on_chair_cond);
  pthread_cond_destroy(&being_served_cond);
  pthread_cond_destroy(&confirm_customer_cond);
  pthread_cond_destroy(&waiting_all_customers_left_cond);

  for (int i = 0; i < no_of_barbers; i++) {
    pthread_cond_destroy(&barb_infos[k].waiting_on_customer_cond);
    pthread_cond_destroy(&barb_infos[k].confirm_customer_left_cond);
    pthread_cond_destroy(&barb_infos[k].being_served_cond);
    pthread_cond_destroy(&barb_infos[k].waiting_on_barber_cond);
    pthread_mutex_destroy(&barb_infos[k].barber_n_mutex);
  }

  pthread_exit(EXIT_SUCCESS);
}

void *assistant_routine(void *arg) {
  int *work_pace;
  int serve_time = 15;
  int barber_found = 0;
  barber_info *barber_curr_info;
  work_pace = (int *)arg;
  while (1) {

    pthread_mutex_lock(&customer_mutex);

    if (num_customer_visited == no_of_customers && no_of_chairs_occupied == 0) {
      printf("Assistant: Waiting for barbers to complete cuts.\n");
      pthread_cond_wait(&waiting_all_customers_left_cond, &customer_mutex);
      printf("Assistant: Hi Barbers, we’ve finished the work for the day.\n");
      pthread_mutex_unlock(&customer_mutex);
      for (int i = 0; i < no_of_barbers && barber_found == 0; i++) {
        pthread_mutex_lock(&barb_infos[i].barber_n_mutex);
        barb_infos[i].work_ended = 1;
        pthread_mutex_unlock(&barb_infos[i].barber_n_mutex);
        pthread_cond_signal(&barb_infos[i].waiting_on_customer_cond);
      }
      break;
    }

    if (no_of_chairs_occupied == 0 && num_customer_visited != no_of_customers) {
      printf("Assistant: I’m waiting for customers.\n");
      pthread_cond_wait(&empty_chairs_cond, &customer_mutex);
    }

    pthread_mutex_unlock(&customer_mutex);

    for (int i = 0; i < no_of_barbers && barber_found == 0; i++) {
      pthread_mutex_lock(&barb_infos[i].barber_n_mutex);
      if (barb_infos[i].barber_busy == 0) {
        barber_found = 1;
        barber_curr_info = &barb_infos[i];
      }
      pthread_mutex_unlock(&barb_infos[i].barber_n_mutex);
    }

    pthread_mutex_lock(&barber_curr_info->barber_n_mutex);

    if (barber_curr_info->barber_busy) {
      printf("Assistant: I’m waiting for barber to become available.\n");
      pthread_cond_wait(&waiting_on_barber_cond,
                        &barber_curr_info->barber_n_mutex);
      printf("Assistant: Barber [%d] has become available.\n",
             barber_curr_info->barber_id);
    }

    {
      pthread_mutex_unlock(&barber_curr_info->barber_n_mutex);
      pthread_mutex_lock(&customer_mutex);

      // Always holds synchronously as we wait for customer to be confirmed
      // before confirming next customer number.
      new_barber_info = barber_curr_info;

      int ticket_to_be_called =
          last_ticket_called >= no_of_chairs ? 1 : last_ticket_called + 1;

      // new_ticket_no = new_ticket_no >= no_of_chairs ? 1 : new_ticket_no +
      // 1;
      printf("Assistant: Call one customer with a ticket numbered %d.\n",
             ticket_to_be_called);

      ticket_already_called = 0;
      printf("Assistant: Waiting on Customer with ticket %d.\n",
             ticket_to_be_called);

      pthread_cond_broadcast(&waiting_on_assistant_cond);
      pthread_cond_wait(&confirm_customer_cond, &customer_mutex);

      pthread_mutex_unlock(&customer_mutex);
      pthread_mutex_lock(&barber_curr_info->barber_n_mutex);

      barber_curr_info->barber_busy = 1;
      barber_curr_info->customer_ticket_no = ticket_to_be_called;
      barber_curr_info->customer_id_no = 1; // TODO

      barber_found = 0;

      pthread_mutex_unlock(&barber_curr_info->barber_n_mutex);

      pthread_cond_signal(&barber_curr_info->waiting_on_customer_cond);
    }
  }
}

void *barber_routine(void *arg) {
  // pace_t *serve_pace;
  // serve_pace = (pace_t *)arg;
  barber_info *barber_n_info = (barber_info *)arg;

  // TODO fix this work ended as it would need to be protected by mutex
  while (1) {
    pthread_mutex_lock(&barber_n_info->barber_n_mutex);
    sleep((int)rand() % 5 + 1);

    if (barber_n_info->work_ended != 0) {
      break;
    }

    printf("Barber [%d]: I’m now ready to accept a new customer.\n",
           barber_n_info->barber_id);

    if (barber_n_info->barber_busy == 0)
      pthread_cond_wait(&barber_n_info->waiting_on_customer_cond,
                        &barber_n_info->barber_n_mutex);

    if (barber_n_info->work_ended != 0) {
      break;
    }

    printf("Barber [%d]: Hello, customer with ticket number %d.\n",
           barber_n_info->barber_id, barber_n_info->customer_ticket_no);

    // Cut hair for customer
    if (barbers_pace.max_pace == barbers_pace.min_pace) {
      sleep(barbers_pace.max_pace);

    } else
      sleep(((int)rand() % (barbers_pace.max_pace - barbers_pace.min_pace)) +
            barbers_pace.min_pace);

    printf("Barber [%d]: Finished cutting. Good bye, customer %d.\n",
           barber_n_info->barber_id, barber_n_info->customer_ticket_no);

    pthread_cond_signal(&barber_n_info->being_served_cond);
    printf("Barber [%d]: Waiting for customer with ticket %d to leave.\n",
           barber_n_info->barber_id, barber_n_info->customer_ticket_no);

    pthread_cond_wait(&barber_n_info->confirm_customer_left_cond,
                      &barber_n_info->barber_n_mutex);

    printf("Barber [%d]: Customer with ticket %d has left.\n",
           barber_n_info->barber_id, barber_n_info->customer_ticket_no);

    barber_n_info->barber_busy = 0;
    barber_n_info->customer_ticket_no = -1;

    pthread_cond_signal(&barber_n_info->waiting_on_barber_cond);

    pthread_mutex_unlock(&barber_n_info->barber_n_mutex);
  }

  printf("Barber [%d]: Thank Assistant and see you tomorrow!\n",
         barber_n_info->barber_id);
}

void *customer_routine(void *arg) {

  int *customer_number = (int *)arg;
  barber_info *my_barber_info;
  printf("Customer [%d]: I have arrived at the barber shop.\n",
         *customer_number);

  pthread_mutex_lock(&customer_mutex);
  num_customer_visited++;
  if (no_of_chairs_occupied == no_of_chairs) {
    printf("Customer [%d]: oh no! all seats have been taken and I'll leave "
           "now!\n",
           *customer_number);
    num_customer_finished++;

  } else {
    new_ticket_no = new_ticket_no >= no_of_chairs ? 1 : new_ticket_no + 1;
    int my_ticket = new_ticket_no;

    printf("Customer [%d]: I'm lucky to get a free seat and a ticket numbered "
           "%d.\n",
           *customer_number, my_ticket);

    no_of_chairs_occupied++;

    // Signal assistant if you are first customer
    if (no_of_chairs_occupied == 1) {
      pthread_cond_signal(&empty_chairs_cond);
    }

    // -- wait for barber seat to be free
    int ticket_to_be_called =
        last_ticket_called >= no_of_chairs ? 1 : last_ticket_called + 1;
    do {
      pthread_cond_wait(&waiting_on_assistant_cond, &customer_mutex);

      ticket_to_be_called =
          last_ticket_called >= no_of_chairs ? 1 : last_ticket_called + 1;
      printf("Customer [%d]: Waiting for assistant to call %d, current call "
             "%d.\n",
             *customer_number, my_ticket, ticket_to_be_called);

    } while (ticket_to_be_called != my_ticket || ticket_already_called);

    ticket_already_called = 1;
    last_ticket_called = ticket_to_be_called;
    no_of_chairs_occupied--;
    my_barber_info = new_barber_info;

    pthread_mutex_unlock(&customer_mutex);
    pthread_mutex_lock(&my_barber_info->barber_n_mutex);

    my_barber_info->customer_id_no = *customer_number;

    pthread_mutex_unlock(&my_barber_info->barber_n_mutex);
    pthread_mutex_lock(&customer_mutex);

    printf("Customer [%d]: Confirming with assistant for ticket number %d.\n",
           *customer_number, last_ticket_called);
    pthread_cond_signal(&confirm_customer_cond);

    pthread_mutex_unlock(&customer_mutex);
    pthread_mutex_lock(&my_barber_info->barber_n_mutex);

    printf("Customer [%d]: My ticket number %d has been called. Hello, Barber "
           "[%d].\n",
           *customer_number, my_ticket, my_barber_info->barber_id);

    // // -- wait for barber to finish cutting hair
    pthread_cond_wait(&my_barber_info->being_served_cond,
                      &my_barber_info->barber_n_mutex);
    printf("Customer [%d]: Well done. Thank barber, bye!\n", *customer_number);
    pthread_cond_signal(&my_barber_info->confirm_customer_left_cond);

    pthread_mutex_unlock(&my_barber_info->barber_n_mutex);

    pthread_mutex_lock(&customer_mutex);

    num_customer_finished++;

    if (num_customer_finished == no_of_customers) {
      printf("Customer [%d]: Final Customer Waves Goodbye!\n",
             *customer_number);
      pthread_cond_signal(&waiting_all_customers_left_cond);
    }

    pthread_mutex_unlock(&customer_mutex);
  }
  return NULL;
}
