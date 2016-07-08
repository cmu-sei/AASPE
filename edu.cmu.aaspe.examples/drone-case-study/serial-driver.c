#include <stdio.h>
#include <generatedtypes.h>



static uint8_t temp = 10;
static uint8_t sound = 50;
static uint8_t light = 125;

void driver (boardcmdrequestti from_filter, boardcmdreplyti* to_filter)
{
//	*to_outside = from_outside;
//	print_boardrequest ("SERIAL", from_filter);

	temp = (temp + 1) % 255;
	sound = (sound + 1) % 255;
	light = (light + 1) % 255;

	to_filter->temp = temp;
	to_filter->light = light;
	to_filter->sound = sound;
}
