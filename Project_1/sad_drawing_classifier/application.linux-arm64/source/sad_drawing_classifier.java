import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sad_drawing_classifier extends PApplet {

PGraphics pg;
ArrayList<PVector> vectors = new ArrayList<PVector>();
boolean startDrawing = false;
boolean madeShape = false;
PShape lineShape;

public void reset(){
  vectors = new ArrayList<PVector>();
  startDrawing = false;
  madeShape = false; 
  pg = createGraphics(width, height);
  background(255,200,0);
  textSize(80);
  text("Make a Happy or Sad Drawing!",100,400);
  textSize(50);
  text("1. Click different points to draw a line drawing.",100,550);
  text("2. Press Enter to See if you made a happy/sad drawing.",100,610);
  text("Press Enter to start!",100,670);
}


public PShape makeShape(){
  stroke(255,0,0);
  PShape lineDrawing = createShape(GROUP);
  for(int i=0; i<vectors.size()-1; i++){
    PShape childLine = createShape(LINE, vectors.get(i).x,vectors.get(i).y,
                     vectors.get(i+1).x,vectors.get(i+1).y);
    lineDrawing.addChild(childLine);
  }
  stroke(0,0,0);
  return lineDrawing;
}


public void setup(){
  
  reset();
}

public void draw(){
}

public PVector meanVector(ArrayList<PVector> vectorList){
  float sumx = 0, sumy = 0;
  for(PVector vec: vectorList){
    sumx = sumx+vec.x;
    sumy = sumy+vec.y;
  }
  return new PVector(sumx/vectorList.size(),sumy/vectorList.size());
}

public String sadClassifier(ArrayList<PVector> vectorList){
  float meanY = meanVector(vectorList).y;
  int counterSad = 0;
  for(PVector vec: vectorList){
    if(vec.y > meanY){
      counterSad+=1;
    }
    else if(vec.y < meanY){
      counterSad-=1;
    }
  }
  if(counterSad > 0){
    return "Sad Drawing! :(";
  }
  else if(counterSad < 0){
    return "Happy Drawing! :)";
  }
  else{
    return "Neutral Drawing. :O";
  }
}

public void keyPressed(){
  if (key == ENTER){
    if(startDrawing){
      if(!madeShape){
        pg.beginDraw();
        pg.clear();
        background(255,200,0);
        lineShape = makeShape();
        pg.shape(lineShape, 0, 0);
        pg.endDraw();
        image(pg,0,0);
        madeShape=true;
        String textval = sadClassifier(vectors);
        text("This is a " + textval,100,400);
        print("This is a " + textval+"\n");
        text("Press enter to restart!", 100, 500);      
      }
      else{
        reset();
      }
    }
    else{
      startDrawing=true;
      background(255,200,0);
    }
  }
}

public void mouseClicked(){
  if(startDrawing){
    if (!madeShape){
      vectors.add(new PVector(mouseX,mouseY));
      if (vectors.size() > 1){
        PVector prevVector = vectors.get(vectors.size() - 2);
        PVector curVector = vectors.get(vectors.size() - 1);
        pg.beginDraw();
        pg.line(prevVector.x,prevVector.y,curVector.x, curVector.y);
        pg.endDraw();
        image(pg, 0, 0);
      }
    }
    else{
      System.out.println("Entered here");
    }
  }
}

  public void settings() {  size(1500,950); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sad_drawing_classifier" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
