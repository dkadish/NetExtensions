import cct.net.StringClient;
import cct.net.StringServer;

int port = 10002;
boolean myServerRunning = true;
StringServer myServer;
String msg;

// Ball Variables
int numBalls = 10;
float spring = (float) 0.005;
float gravity = (float) 0.1;// 03;
float friction = (float) -0.9;
Ball[] balls = new Ball[numBalls];

void setup() {
  // Server Setup
  myServer = new StringServer(this, port); // Starts a myServer on port
                        // 10002

  // Ball Setup
  size(640, 360);
  noStroke();
  for (int i = 0; i < numBalls; i++) {
    balls[i] = new Ball(this, random(width), random(height), random(30,
        70), i, balls);
  }

  // print(Server.ip() + ":" + "10002");
}

void draw() {
  // Server Code
  if (myServerRunning == true) {
    StringClient thisClient = myServer.available();
    if (thisClient != null && thisClient.active()) {
      println("Available Client!\n");
      // if (thisClient.available() > 0) {
      msg = thisClient.readString();
      if (msg.equals("high\r\n")) {
        println("HIGH!");
        gravity *= -1;
      } else if (msg.equals("low\r\n")) {
        println("LOW!");
        gravity *= -1;
      } else {
        print("MESSAGE: ");
        print(msg);
        print(", ");
        print(msg.equals("high\r\n"));
        print(", ");
        println(msg.equals("low\r\n"));
      }
      thisClient.writeString("Hey there, arduino! How are you doing?");
      println("Message Returned");
      // }
    } else if (thisClient != null && !thisClient.active()) {
      println("INACTIVE CLIENT!");
    }
  } else {
    println("Server isn't running...");
  }

  // Ball Code
  background(0);
  for (int i = 0; i < numBalls; i++) {
    balls[i].collide(numBalls, spring);
    balls[i].move(gravity, friction);
    balls[i].display();
  }
}
