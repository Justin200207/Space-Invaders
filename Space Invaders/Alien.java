import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class Alien{
  int x_pos;
  int y_pos;
  BufferedImage sprite1, sprite2, sprite;
  int onMove;
  
  public Alien(int x, int y, int row){
    x_pos = x;
    y_pos = y;
    
    if(row==0){
      try{
        sprite1 = ImageIO.read(new File("sprites/aliens/alien3-1.png"));
        sprite2 = ImageIO.read(new File("sprites/aliens/alien3-2.png"));
      }catch(IOException e){}
    }else if(row==1||row==2){
      try{
        sprite1 = ImageIO.read(new File("sprites/aliens/alien2-1.png"));
        sprite2 = ImageIO.read(new File("sprites/aliens/alien2-2.png"));
      }catch(IOException e){}
    }else{
      try{
        sprite1 = ImageIO.read(new File("sprites/aliens/alien1-1.png"));
        sprite2 = ImageIO.read(new File("sprites/aliens/alien1-2.png"));
      }catch(IOException e){}
    }
    sprite = sprite1;
    onMove = 1;
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
  
  public void moveAlien(int width, int dir){ 
    if(dir>0){
      x_pos+=35;
    }else if(dir<0){
      x_pos-=35;
    }
    swapSprite();
  }
  
  public boolean checkLoss(int height){
    if(y_pos+65>=height-155){
      return true;
    }else{
      return false;
    }
  }
  
  public void moveAlienDown(){
    y_pos+=65;
    swapSprite();
  }
  
  public void swapSprite(){
        onMove/=-1;
    if(onMove>0){
      sprite = sprite1;
    }else if(onMove<0){
      sprite = sprite2;
    }
  }
  
  public boolean checkWall(int width){
    if(x_pos+70>=width){
      return true;
    }else if(x_pos-35<=0){
      return true;
    }else{
      return false;
    }
  }
  
  public Rectangle getBounds(){
    return new Rectangle(x_pos,y_pos,sprite.getWidth(), sprite.getHeight());
  }
  public int getWidth(){
    return sprite.getWidth();
  }
}