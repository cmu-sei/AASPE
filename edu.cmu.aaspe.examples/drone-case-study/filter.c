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



void filter (missiondatati *missiondata, navdatat navdata, boardcmdreplyti cmdin, boardcmdrequestti* cmdout)
{
//	*cmdout = cmdin;

	print_navdata ("FILTER DATA FROM NAVIGATION ", navdata);
	print_boardreply ("FILTER - REPLY FROM BOARD", cmdin);

	missiondata->temp = cmdin.temp;
	missiondata->light = cmdin.light;
	missiondata->sound = cmdin.sound;

	cmdout->get_temp = 1;
	cmdout->get_light = 0;
	cmdout->get_sound = 0;

	cmdout->direction = directiont_down;
}
