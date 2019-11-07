#include <SoftwareSerial.h>



#include <AFMotor.h>
#define trigPin 12 // define the pins of your sensor
#define echoPin 13 
#define trigPin2 9 // define the pins of your sensor
#define echoPin2 10 
#define m11 11    // rear motor
#define m12 12
#define m21 10    // front motor
#define m22 9


volatile long ball,duration, distance,duration2,distance2; // start the scan
volatile int meem=0;
volatile int mode = 7;

char str[2],i;
AF_DCMotor motor2(2, MOTOR12_64KHZ); // create motor #2, 64KHz pwm
AF_DCMotor motor1(1, MOTOR12_64KHZ); // create motor #2, 64KHz pwm
AF_DCMotor motor3(3, MOTOR12_64KHZ); // create motor #2, 64KHz pwm
AF_DCMotor motor4(4, MOTOR12_64KHZ); // create motor #2, 64KHz pwm


void forward()
{
  
   if(mode == 6)
  {
    motor1.setSpeed(220);
   motor2.setSpeed(220);
   motor3.setSpeed(220);
   motor4.setSpeed(220);
  }
  else
  {
    motor1.setSpeed(200);
   motor2.setSpeed(200);
   motor3.setSpeed(200);
   motor4.setSpeed(200);
  }
   //pinMode(LED_BUILTIN, OUTPUT);
   //Serial.println(distance);
   motor1.run(BACKWARD);      // turn it on going forward
  motor2.run(FORWARD);      // turn it on going forward
  motor3.run(FORWARD);      // turn it on going forward
  motor4.run(BACKWARD);      // turn it on going forward
}

void backward()
{
  motor1.setSpeed(200);
   motor2.setSpeed(200);
   motor3.setSpeed(200);
   motor4.setSpeed(200);
   motor1.run(FORWARD);      // turn it on going forward
  motor2.run(BACKWARD);      // turn it on going forward
  motor3.run(BACKWARD);      // turn it on going forward
  motor4.run(FORWARD);      // turn it on going forward
}

void left()
{
  if(mode == 6)
  {
    motor1.setSpeed(200);
   motor2.setSpeed(200);
   motor3.setSpeed(200);
   motor4.setSpeed(200);
  }
   motor1.run(BACKWARD);      // turn it on going forward
  motor2.run(BACKWARD);      // turn it on going forward
  motor3.run(FORWARD);      // turn it on going forward
  motor4.run(FORWARD);      // turn it on going forward
  //delay(1000);
  //forward();
}

void right()
{
  if(mode == 6)
  {
    motor1.setSpeed(200);
   motor2.setSpeed(200);
   motor3.setSpeed(200);
   motor4.setSpeed(200);
  }
   motor1.run(FORWARD);      // turn it on going forward
  motor2.run(FORWARD);      // turn it on going forward
  motor3.run(BACKWARD);      // turn it on going forward
  motor4.run(BACKWARD);      // turn it on going forward
}

void Stop()
{
   motor1.run(RELEASE);      // turn it on going forward
  motor2.run(RELEASE);      // turn it on going forward
  motor3.run(RELEASE);      // turn it on going forward
  motor4.run(RELEASE);      // turn it on going forward
}

void setup() 
{
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
   digitalWrite(LED_BUILTIN, HIGH);
   motor1.setSpeed(200);
   motor2.setSpeed(200);
   motor3.setSpeed(200);
   motor4.setSpeed(200);


   
   pinMode(trigPin, OUTPUT);// set the trig pin to output (Send sound waves)
  pinMode(echoPin, INPUT);// set the echo pin to input (recieve sound waves)
  pinMode(trigPin2, OUTPUT);// set the trig pin to output (Send sound waves)
  //pinMode(echoPin2, INPUT);// set the echo pin to input (recieve sound waves)
  pinMode(m11, OUTPUT);
  pinMode(m12, OUTPUT);
  pinMode(m21, OUTPUT);
  pinMode(m22, OUTPUT);
}


long dist()
{
  if(meem == 1)
  {
    digitalWrite(trigPin, LOW);  
  delayMicroseconds(2); // delays are required for a succesful sensor operation.
  digitalWrite(trigPin, HIGH);

  delayMicroseconds(10); //this delay is required as well!
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  //Serial.println("");
  //Serial.println(duration);
  distance = (duration/2.0) / 29.1;// convert the distance to centimeters.
  //Serial.println(distance);
  }


  else if(meem == 4)
  {
    pinMode(echoPin2, INPUT);
      digitalWrite(trigPin2, LOW);  
  delayMicroseconds(2); // delays are required for a succesful sensor operation.
  digitalWrite(trigPin2, HIGH);

  delayMicroseconds(10); //this delay is required as well!
  digitalWrite(trigPin2, LOW);
  duration2 = pulseIn(echoPin2, HIGH);
  pinMode(echoPin2, OUTPUT);
  //Serial.println("");
  //Serial.println(duration);
  distance2 = (duration2/2.0) / 29.1;// convert the distance to centimeters.
  }

}

void loop() 
{
   
  dist();
 // Serial.println( distance);
  if(meem == 1 && mode== 7)
  {   //Serial.println(distance);

    //meem=0;
    if(distance > 45)
    {
      forward();
    }
    else
    {
      Stop();
      
    }
  }
  else if(meem == 4 && mode== 7)
  { //  Serial.println(distance2);

    //meem=0;
    if(distance2 > 45)
    {
      backward();
    }
    else
    {
      Stop();
      
    }
  }

  else if(mode == 6)
  {   //Serial.println(distance);

    //meem=0;
    if(distance > 80)
    {
      forward();
    }
    else
    {
      backward();
      delay(200);
      int tada = random(0,2);
      if(tada == 0)
      {
        
        
        left();
        delay(480);
      
      
        
      
      }
      else
      {
        
        right();
        delay(480);
      }
    }
  }
  if(Serial.available())
  {
    //motor4.run(FORWARD);
    
    //Serial.println(distance);
    char ch=Serial.read();
    str[i++]=ch;
    
    if(str[i-1]=='1' && mode==7)
    {
     
     Serial.println("Forward");
     meem=1;
     //forward();
     i=0;
    }

    else if(str[i-1]=='2' && mode==7)
    {
     
     Serial.println("Left");
     meem=2;
     right();
     i=0;
    }

    else if(str[i-1]=='3' && mode==7)
    {
      
      Serial.println("Right");
      meem=3;
      left();
      i=0;
    }
    
    else if(str[i-1]=='4' && mode==7)
    {
      Serial.println("Backward");
      //backward();
      meem=4;
      i=0;
    }

    else if(str[i-1]=='5' && mode==7)
    {
      
      Serial.println("Stop");
      Stop();
      meem=5;
      i=0;
    }
    else if(str[i-1]=='6')
    {
      Serial.println("Auto");
      mode = 6;
      meem = 1;
    }
    else if(str[i-1]=='7')
    {
      Serial.println("Manual");
      mode = 7;
      meem = 5;
      Stop();
    }
    delay(100);
  }
}
