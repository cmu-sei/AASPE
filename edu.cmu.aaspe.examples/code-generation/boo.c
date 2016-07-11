#include <stdio.h>

int cons = 0;

void boo_spg (int* dataout, int* datain)
{
	cons = cons + 1;
	*dataout = cons;
//	printf ("[BOO] Sending %d\n", *dataout);
//	printf ("[BOO] Receiving %d\n", datain);
}
