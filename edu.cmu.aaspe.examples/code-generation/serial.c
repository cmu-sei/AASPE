#include <stdio.h>

int cons = 0;

void driver (int datain, int* dataout)
{
	*dataout = cons++;
	printf ("[DRIVER] Sending %d\n", *dataout);
	printf ("[DRIVER] Receiving %d\n", datain);
}
