#include <stdio.h>

volatile static int sval = 0;

void user_do_ping_spg (int* val)
{
	*val = sval;
	sval++;
	printf ("Sending %d\n", *val);
}

void user_ping_spg (int val)
{
	printf ("Receiving %d\n", val);
}
