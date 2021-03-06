import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class Missle{
  int x_pos;
  int y_pos;
  int fireDir;
  BufferedImage sprite;
  
  public Missle(int xLoc, int yLoc, int dir, String img){
    fireDir = dir;
    try{
        sprite = ImageIO.read(new File(("sprites/missle/"+img)));
      }catch(IOException e){}
    if(dir<0){  
      y_pos = yLoc-sprite.getHeight();
    }else if(dir>0){
      y_pos = yLoc+sprite.getHeight();
    }
    x_pos = xLoc-sprite.getWidth()/2;
  }
  
  public boolean moveMissle(int height){
    if(y_pos>0&&fireDir<0){
      y_pos+=fireDir;
      return false;
    }else if(y_pos<height&&fireDir>0){
      y_pos+=fireDir;
      return false;
    }else{
      return true;
    }
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
  
  public Rectangle getBounds(){
    return new Rectangle(x_pos,y_pos,sprite.getWidth(), sprite.getHeight());
  }
  
  public boolean collision(Rectangle entity){
    if(getBounds().intersects(entity)){
      return true;
    }else{
      return false;
    }
  }
}