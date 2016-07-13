//Code by Reichenstein7 (thejamerson.com)

//Keyboard Controls:
//
// 1 -Motor 1 Left
// 2 -Motor 1 Stop
// 3 -Motor 1 Righ1
//
// 4 -Motor 2 Left
// 5 -Motor 2 Stop
// 6 -Motor 2 Right

// Declare L298N Dual H-Bridge Motor Controller directly since there is not a library to load.

// Motor 1
int dir1PinA = 2;
int dir2PinA = 3;
int speedPinA = 9; // Needs to be a PWM pin to be able to control motor speed

// Motor 2
int dir1PinB = 4;
int dir2PinB = 5;
int speedPinB = 10; // Needs to be a PWM pin to be able to control motor speed

void setup() {  // Setup runs once per reset
// initialize serial communication @ 9600 baud:
Serial.begin(115200);

//Define L298N Dual H-Bridge Motor Controller Pins

pinMode(dir1PinA,OUTPUT);
pinMode(dir2PinA,OUTPUT);
pinMode(speedPinA,OUTPUT);
pinMode(dir1PinB,OUTPUT);
pinMode(dir2PinB,OUTPUT);
pinMode(speedPinB,OUTPUT);

}

#define CMDSIZE 10
#define CMD_NONE 0
#define CMD_UP 1
#define CMD_DOWN 2
#define CMD_LEFT 3
#define CMD_RIGHT 4

void controlmotor1 (int intensity)
{
  if (intensity > 0)
  {
    analogWrite(speedPinA, intensity);//Sets speed variable via PWM 
    digitalWrite(dir1PinA, LOW);
    digitalWrite(dir2PinA, HIGH);
  }
  else
  {
    digitalWrite(dir1PinA, LOW);
    digitalWrite(dir2PinA, LOW);
  }
}

void controlmotor2 (int intensity)
{
  if( intensity > 0)
  {
    analogWrite(speedPinB, intensity);
    digitalWrite(dir1PinB, LOW);
    digitalWrite(dir2PinB, HIGH);
  }
  else
  {
    analogWrite(speedPinB, 0);
    digitalWrite(dir1PinB, LOW);
    digitalWrite(dir2PinB, LOW);
  }
}

void read_command (int* cmdtype, int* cmdvalue)
{
  char cmd[CMDSIZE + 1];
  byte size;
  char* pos_type;
  char* pos_val_begin;
  char* pos_val_end;
  int value_size;
  char valbuf[4];
  
  *cmdtype = CMD_NONE;
  *cmdvalue = 0;
  
  if (Serial.available() == -1)
  {
    return;
  }
  
  size = Serial.readBytes(cmd, CMDSIZE);
  cmd[size] = 0;
    
  pos_type = strchr (cmd, ':');
  
  if (pos_type == NULL)
  {
    return;
  }
  
  if (pos_type[1] == 'U')
  {
    *cmdtype = CMD_UP;
  }

  if (pos_type[1] == 'D')
  {
    *cmdtype = CMD_DOWN;
  }

  if (pos_type[1] == 'L')
  {
    *cmdtype = CMD_LEFT;
  }

  if (pos_type[1] == 'R')
  {
    *cmdtype = CMD_RIGHT;
  }
  
  if (cmdtype == CMD_NONE)
  {
    return;
  }
  
  
  pos_val_begin = strchr (pos_type + 1, ':');
  
  if (pos_val_begin == NULL)
  {
    return;
  }
  pos_val_begin++;
  
  pos_val_end = strchr (pos_val_begin, ':');
  if (pos_val_end == NULL)
  {
    return;
  }
  
  value_size = pos_val_end - pos_val_begin;
  
  if(value_size > 3)
  {
    return;
  }
  
  for (int i = 0 ; i < 4 ; i++)
  {
    valbuf[i] = 0;
  }
  
  for (int i = 0 ; i < value_size ; i++)
  {
    valbuf[i] = pos_val_begin[i];
  }
    Serial.println( "valbuf");

  Serial.println(valbuf);

  *cmdvalue = atoi(valbuf);  
}


int motor1 = 0;
int motor2 = 0;

void loop() {
  int cmdtype;
  int cmdval;

  
  
  read_command (&cmdtype, &cmdval);
  Serial.println("cmdtype"); 
  Serial.println(cmdtype); 
  Serial.println("cmdval"); 
  Serial.println(cmdval);

  if (cmdtype == CMD_UP)
  {
     motor1 = cmdval;
     motor2 = cmdval;
  }
  
  if (cmdtype == CMD_LEFT)
  {
     motor1 = cmdval;
  }
  
  if (cmdtype == CMD_RIGHT)
  {
     motor2 = cmdval;
  }
  
  controlmotor1 (motor1);
  controlmotor2 (motor2);
  
}
