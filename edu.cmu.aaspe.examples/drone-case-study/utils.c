#include <stdio.h>
#include <generatedtypes.h>

/*
typedef struct {
	unsigned8 temp;
	unsigned8 light;
	unsigned8 sound;
}missiondatati;


typedef enum {
	navdatat_up = 0
	,navdatat_down = 1
	,navdatat_left = 2
	,navdatat_right = 3
}navdatat;


typedef struct {
	unsigned8 temp;
	unsigned8 light;
	unsigned8 sound;
}boardcmdreplyti;


typedef int boolean;

typedef enum {
	directiont_up = 0
	,directiont_down = 1
	,directiont_left = 2
	,directiont_right = 3
}directiont;


typedef struct {
	boolean get_temp;
	boolean get_light;
	boolean get_sound;
	directiont direction;
}boardcmdrequestti;
*/



void print_boardreply (char* prefix, boardcmdreplyti reply)
{
	printf ("[%s] BOARD REPLY temp=%u ; light=%u ; sound = %u\n", prefix, reply.temp, reply.light, reply.sound);
}

void print_direction (char* prefix, directiont direction)
{
	switch (direction)
	{
	case directiont_up:
	{
		printf ("[%s] direction UP\n",prefix);
		break;
	}
	case directiont_down:
	{
		printf ("[%s] direction DOWN\n",prefix);
		break;
	}
	case directiont_left:
	{
		printf ("[%s] direction LEFT\n",prefix);
		break;
	}
	case directiont_right:
	{
		printf ("[%s] direction RIGHT\n",prefix);
		break;
	}
	}
}

void print_boardrequest (char* prefix, boardcmdrequestti request)
{
	printf ("[%s] BOARD REQUEST get_temp=%u ; get_light=%u ; get_sound = %u\n", prefix, request.get_temp, request.get_light, request.get_sound);
	print_direction (prefix, request.direction);
}


void print_navdata (char* prefix, navdatat nav)
{
	switch (nav)
	{
	case navdatat_up:
	{
		printf ("[%s] navdata UP\n",prefix);
		break;
	}
	case navdatat_down:
	{
		printf ("[%s] navdata DOWN\n",prefix);
		break;
	}
	case navdatat_left:
	{
		printf ("[%s] navdata LEFT\n",prefix);
		break;
	}
	case navdatat_right:
	{
		printf ("[%s] navdata RIGHT\n",prefix);
		break;
	}
	}
}

void print_mission_data (char* prefix, missiondatati missiondata)
{
	printf ("[%s] MISSION DATA temp=%u ; light=%u ; sound = %u\n", prefix, missiondata.temp, missiondata.light, missiondata.sound);
}
