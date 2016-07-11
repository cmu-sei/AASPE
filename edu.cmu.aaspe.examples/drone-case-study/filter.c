#include <stdio.h>
#include <generatedtypes.h>

//	typedef struct {
//		unsigned8 temp;
//		unsigned8 light;
//		unsigned8 sound;
//	}missiondatati;
//
//
//	typedef enum {
//		navdatat_up = 0
//		,navdatat_down = 1
//		,navdatat_left = 2
//		,navdatat_right = 3
//	}navdatat;

//typedef struct {
//	unsigned8 temp;
//	unsigned8 light;
//	unsigned8 sound;
//}boardcmdreplyti;



//typedef struct {
//	boolean get_temp;
//	boolean get_light;
//	boolean get_sound;
//	directiont direction;
//}boardcmdrequestti;


#define BUFSIZE 20


void filter (missiondatati *missiondata, navdatat* navdata, stringi* cmdin, stringi* cmdout)
{
//	*cmdout = cmdin;

//	print_navdata ("FILTER DATA FROM NAVIGATION ", navdata);
//	print_boardreply ("FILTER - REPLY FROM BOARD", cmdin);

//	printf ("[FILTER] navdata = %d\n", navdata);
//	strcpy(cmdout->buf,"blabla");
//	cmdout->size = 6;
//	printf ("[FILTER] from driver = %s\n", cmdin->buf);

	/**
	 * reset the buffer to send
	 */
	for (int i = 0 ; i < BUFSIZE ; i++)
	{
		cmdout->buf[i] = 0;
	}

	if (*navdata == navdatat_up)
	{
		strcpy (cmdout->buf, ":L:200:R:200:");
		cmdout->size = 13;
	}
	if (*navdata == navdatat_left)
	{
		strcpy (cmdout->buf, ":L:200:R:0:");
		cmdout->size = 11;
	}
	if (*navdata == navdatat_none)
	{
		strcpy (cmdout->buf, ":L:0:R:0:");
		cmdout->size = 9;
	}




//	missiondata->temp = cmdin.temp;
//	missiondata->light = cmdin.light;
//	missiondata->sound = cmdin.sound;

//	cmdout->get_temp = 1;
//	cmdout->get_light = 0;
//	cmdout->get_sound = 0;

//	cmdout->direction = directiont_down;
}
