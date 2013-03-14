class Ball {
    //PApplet parent; // The parent PApplet that we will render ourselves onto

    float x, y;
    float diameter;
    float vx = 0;
    float vy = 0;
    int id;
    Ball[] others;
   
    Ball(PApplet p, float xin, float yin, float din, int idin, Ball[] oin) {
    parent = p;
      x = xin;
      y = yin;
      diameter = din;
      id = idin;
      others = oin;

    }
    
    void collide(int numBalls, float spring) {
      for (int i = id + 1; i < numBalls; i++) {
        float dx = others[i].x - x;
        float dy = others[i].y - y;
        float distance = PApplet.sqrt(dx*dx + dy*dy);
        float minDist = others[i].diameter/2 + diameter/2;
        if (distance < minDist) { 
          float angle = PApplet.atan2(dy, dx);
          float targetX = x + PApplet.cos(angle) * minDist;
          float targetY = y + PApplet.sin(angle) * minDist;
          float ax = (targetX - others[i].x) * spring;
          float ay = (targetY - others[i].y) * spring;
          vx -= ax;
          vy -= ay;
          others[i].vx += ax;
          others[i].vy += ay;
        }
      }   
    }
    
    void move(float gravity, float friction) {
      vy += gravity;
      x += vx;
      y += vy;
      if (x + diameter/2 > parent.width) {
        x = parent.width - diameter/2;
        vx *= friction; 
      }
      else if (x - diameter/2 < 0) {
        x = diameter/2;
        vx *= friction;
      }
      if (y + diameter/2 > parent.height) {
        y = parent.height - diameter/2;
        vy *= friction; 
      } 
      else if (y - diameter/2 < 0) {
        y = diameter/2;
        vy *= friction;
      }
    }
    
    void display() {
      parent.fill(255, 204);
      parent.ellipse(x, y, diameter, diameter);
    }
}

