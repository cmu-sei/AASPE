#include <stdio.h>

int cons = 0;

void boo_spg (int datain, int* dataout)
{
	*dataout = cons++;
	printf ("[BOO] Sending %d\n", *dataout);
	printf ("[BOO] Receiving %d\n", datain);
}
