#include <stdio.h>
#include <generatedtypes.h>


//typedef enum {
//	navdatat_up = 0
//	,navdatat_down = 1
//	,navdatat_left = 2
//	,navdatat_right = 3
//}navdatat;

int iteration = 0;

void navigation (navdatat* navdata)
{
//	print_navdata ("NAVIGATION", navdata);
	if (iteration < 10)
	{
		*navdata = navdatat_up;
	}
	if ( (iteration >= 10) && (iteration < 20))
	{
		*navdata = navdatat_left;
	}
	iteration = (iteration + 1) % 20;
}
