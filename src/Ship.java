import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class Ship {
  
  int x_pos  = 50;
  int y_pos;
  BufferedImage sprite;
  int movement = 0;
  boolean missleShot = false;
  Missle missle;
  
  public Ship(int height){
    try{
      sprite = ImageIO.read(new File("sprites/ship.png"));
    }catch(IOException e){}
    y_pos = height-90;
  }
  
  public void moveShip(int width){
    if((x_pos <= 0 && movement<0)||(x_pos+sprite.getWidth() >= width && movement>0)){
      movement = 0;
    }
    x_pos+=movement;
  }
  
  public int getX_pos(){
    return x_pos;
  }
  
  public int getY_pos(){
    return y_pos;
  }
  public Image getImage(){
    return sprite;
  }
  
  public void setSpeed(int speed){
    movement = speed;
  }
  
  public void shootMissle(){
    if(!missleShot){
      missleShot = true;
    }
  }
  
  public void deleteMissle(){
    if(missleShot){
      missleShot = false;
    }
  }
  
  public boolean hasShot(){
    return missleShot;
  }
  
  public Rectangle getBounds(){
    return new Rectangle(x_pos,y_pos,sprite.getWidth(), sprite.getHeight());
  }
  public int getWidth(){
    return sprite.getWidth();
  }
  public int getHeight(){
    return sprite.getHeight();
  }
}
