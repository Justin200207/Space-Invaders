 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Integer;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.lang.Integer;



public class SpaceInvaders extends JFrame implements Runnable, KeyListener{ 
  MyDrawPanel gamePanel = new MyDrawPanel();
  Random r = new Random();
  BufferedWriter writer = null;
  BufferedReader reader = null;
  
  Ship ship;
  int shipSpeed = 0;
  Missle playMissle;
  int colAlien = 8;
  int rowAlien = 5;
  Missle alienMissle[] = new Missle[colAlien];
  Alien aliens[][] = new Alien[colAlien][rowAlien];
  int timePassed;
  int lastMove;
  int dir;
  boolean hitWall;
  boolean moveDown;
  boolean moveSide;
  int gameState = 0;
  int aliensKilled = 0;
  int score = 0;
  int round = 1;
  int gameSpeed = 200;
  int lives = 3;
  int highScore = 0;
  BufferedImage background;
  BufferedImage logo;
  Shield[] shields = new Shield[3];
  
  Insets insets;
  int ewInset;
  int nsInset;
  int width;
  int height;
  
  public static void main(String[ ] args){
    new SpaceInvaders();
  }  
  
  public SpaceInvaders(){
    addKeyListener(this);
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    
    try{
      background = ImageIO.read(new File("sprites/background.jpg"));
      logo = ImageIO.read(new File("sprites/logo.png"));
    }catch(IOException e){}
    
    Thread th = new Thread (this);
    th.start ();
    
    this.setSize(1190,900);
    
    this.add(gamePanel);
    this.setResizable(false);
    this.setVisible(true);
  }
  
  public void setupGame(){
    ship = new Ship(height);
    int a_x = 10;
    int a_y = 130;
    for (int i = 0; i<colAlien; i++){
      alienMissle[i] = null;
      a_y = 130;
      for (int o = 0; o<rowAlien; o++){
        aliens[i][o] = new Alien(a_x, a_y, o);
        a_y += 65;
      }
      a_x += 70;
    }
    timePassed = 0;
    lastMove = 0;
    dir = 1;
    hitWall = false;
    moveDown = false;
    moveSide = true;
    playMissle = null;
    aliensKilled = 0;
    gameSpeed = 200;
    ship.deleteMissle();
    try{
      reader = new BufferedReader(new FileReader("scores.txt"));
      highScore = Integer.parseInt(reader.readLine());
    }catch(IOException e){}
  }
  
  public void resetGame(){
    score = 0;
    lives = 3;
    for(int i = 0; i<3; i++){
      shields[i]=new Shield(width/4*(1+i)-40, height-210);
    }
  }
  
  public void run (){ 
    setupGame();
    while (true){ 
      try{ 
        Thread.sleep (5);
      }
      catch (InterruptedException ex){}
      
      insets = this.getInsets();
      ewInset = insets.left+insets.right;
      nsInset = insets.top+insets.bottom;
      width = this.getWidth()-ewInset;
      height = this.getHeight()-nsInset;
      
      if(gameState == 1){
        ship.moveShip(width);
        
        if(playMissle!=null && ship.hasShot()){
          if(playMissle.moveMissle(height)){
            playMissle = null;
            ship.deleteMissle();
          }
          checkHit();  
        }
        
        for(int i = 0; i<colAlien; i++){
          if(alienMissle[i]!=null){
            if(alienMissle[i].moveMissle(height)){
              alienMissle[i] = null;
            }
            if(checkShipHit(i)){
              try{ 
                Thread.sleep (500);
              }
              catch (InterruptedException ex){}
            }
            checkShieldHit(i);
          }
        }
        
        if(timePassed>=lastMove+gameSpeed && gameState == 1){
          moveAliens();
        }
        
        if(aliensKilled == 40){
          aliensKilled = 0;
          gameState = 3;
        }

        if(lives == 0){
          gameState = 2;
        }
        
        switch(aliensKilled){
          case 8: gameSpeed = 125; break;
          case 16: gameSpeed = 75; break;
          case 24: gameSpeed = 50; break;
          case 32: gameSpeed = 30; break;
        }
        
        timePassed+=1;
      }
      repaint();
      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }
      
    }
  
  public void keyTyped(KeyEvent e) {}
  public void keyPressed(KeyEvent e) { 
    if (e.getKeyCode() == KeyEvent.VK_D)
      ship.setSpeed(3);
    if (e.getKeyCode() == KeyEvent.VK_A)
      ship.setSpeed(-3);
    if(e.getKeyCode() == KeyEvent.VK_SPACE){
      if(!ship.hasShot()&&gameState==1){
        playMissle = new Missle(ship.getX_pos()+(ship.getWidth()/2), ship.getY_pos(), -3, "missle.png");
        ship.shootMissle();
      }else if(gameState == 0||gameState ==2){
        gameState = 1;
        resetGame();
        setupGame();
      }else if(gameState == 3){
       round+=1;
       gameState = 1;
       setupGame(); 
      }
    }
    if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
      gameState = 0;
    }
  }
  
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_D)
      ship.setSpeed(0);
    if (e.getKeyCode() == KeyEvent.VK_A)
      ship.setSpeed(0);
  }
  
  public void moveAliens(){
    boolean shoot = false;
    boolean bottom = false;
    int ranNum;
    if(moveSide){
      for (int i = 0; i<colAlien; i++){
        shoot = false;
        ranNum = r.nextInt(5);
        if(ranNum == 1){
          shoot = true;
        }
        for (int o = 0; o<rowAlien; o++){
          if(aliens[i][o]!=null){
            aliens[i][o].moveAlien(width, dir);
            if(aliens[i][o].checkWall(width)){
              moveDown = true;
              moveSide = false;
            }
            if(shoot){
              if(o==4){
                bottom = true;
              }else{
                for (int z = rowAlien-1; z>o; z--){
                  if(aliens[i][z]!=null){
                    bottom = false;
                  }else{
                    bottom = true;
                  }
                }
              }
              if(bottom){
                if(alienMissle[i]==null){
                  alienMissle[i] = new Missle(aliens[i][o].getX_pos()+aliens[i][o].getWidth()/2, aliens[i][o].getY_pos(), 3, "laser.png");
                }
              }
            }
          }
        }
        lastMove = timePassed;
      }
    }else if(moveDown){
      for (int i = 0; i<colAlien; i++){
        for (int o = 0; o<rowAlien; o++){
          if(aliens[i][o]!=null){
            if(aliens[i][o].checkLoss(height)){
              gameState = 2;
            }
          }
        }
      }
      if(gameState == 1){
        for (int i = 0; i<colAlien; i++){
          for (int o = 0; o<rowAlien; o++){
            if(aliens[i][o]!=null){
              aliens[i][o].moveAlienDown();
            }
          }
        }
      }
      moveDown = false;
      moveSide = true;
      dir/=-1;
      lastMove = timePassed;
    }
  }
  
  public void checkHit(){
    for (int i = 0; i<colAlien; i++){
      for (int o = 0; o<rowAlien; o++){
        if(aliens[i][o]!=null&&playMissle!=null){
          if(playMissle.collision(aliens[i][o].getBounds())){
            aliens[i][o] = null;
            aliensKilled+=1;
            score += 50;
            playMissle = null;
            ship.deleteMissle();
          }
        }
      }
      if(alienMissle[i]!=null&&playMissle!=null){
        if(playMissle.collision(alienMissle[i].getBounds())){
          alienMissle[i]=null;
          playMissle=null;
          ship.deleteMissle();
          score += 5;
        }
      }
    }
    for(int z = 0; z<3; z++){
      for(int i = 0; i<4; i++){
        for(int o = 0; o<4; o++){
          if(shields[z]!=null&&playMissle!=null){
            if(playMissle.collision(shields[z].getBounds(i,o))&&shields[z].getHealth(i,o)>0){
              shields[z].hit(i,o);
              playMissle=null;
              ship.deleteMissle();
            }
          }
        }
      }
    }
  }
  
  public boolean checkShipHit(int i){
    if(alienMissle[i]!=null && alienMissle[i].collision(ship.getBounds())){
      lives--;
      alienMissle[i]=null;
      try{
        reader = new BufferedReader(new FileReader("scores.txt"));
        highScore = Integer.parseInt(reader.readLine());
        reader.close();
      }catch(IOException e){}
      if(score>highScore){
        try{
          writer = new BufferedWriter(new FileWriter("scores.txt"));
          writer.write(new Integer(score).toString());
          writer.close();
        }catch(IOException e){}
      }
      return true;
    }
    return false;
  }
  
  public void checkShieldHit(int n){
    for(int z = 0; z<3; z++){
      for(int i = 0; i<4; i++){
        for(int o = 0; o<4; o++){
          if(shields[z]!=null&&alienMissle[n]!=null){
            if(alienMissle[n].collision(shields[z].getBounds(i,o))&&shields[z].getHealth(i,o)>0){
              shields[z].hit(i,o);
              alienMissle[n]=null;
            }
          }
        }
      }
    }
  }
  
  public class MyDrawPanel extends JPanel{
    public void paintComponent (Graphics g){
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      String start = "Press Space To Start";
      String restart = "Defeat! Press Space To Restart";
      String winMessage = "You Win! Press Space For Next Round";
      Font font = new Font("Britannic Bold",Font.PLAIN, 32);
      Color mainColor = new Color(250, 244, 2);
      FontMetrics fm;
      
      g2.setColor(mainColor);
      g2.setFont(font);
      fm = g2.getFontMetrics();
      
      g2.drawImage(background, 0, 0, this);
      g2.setColor(mainColor);
      
      //g2.drawRect(width/2, 0, 1, height);
      //g2.drawRect(0, height/2, width, 1);
      
      if(gameState == 0){
        g2.drawImage(logo, width/2-350/2, height/2-157/2, logo.getWidth()/2, logo.getHeight()/2, this);
        g2.drawString(start, width/2 - fm.stringWidth(start) / 2, height/2+logo.getHeight()/2/2+30);
      }else{
        g2.drawImage(ship.getImage(),ship.getX_pos(),ship.getY_pos(),this);
        
        if(ship.hasShot()){
          g2.drawImage(playMissle.getImage(), playMissle.getX_pos(),playMissle.getY_pos(),this);
        }
        
        for (int i = 0; i<colAlien; i++){
          if(alienMissle[i]!=null){
            g2.drawImage(alienMissle[i].getImage(), alienMissle[i].getX_pos(),alienMissle[i].getY_pos(), this);
          }
          for (int o = 0; o<rowAlien; o++){
            if(aliens[i][o]!=null){
              g2.drawImage(aliens[i][o].getImage(),aliens[i][o].getX_pos(),aliens[i][o].getY_pos(),this);
            }
          }
          g2.drawString(("Score: "+score), 10, fm.getHeight());
          if(score>highScore){
            g2.drawString("High Score: "+score, 50+fm.stringWidth("Score: "+score), fm.getHeight());
          }else{
            g2.drawString("High Score: "+highScore, 50+fm.stringWidth("Score: "+score), fm.getHeight());
          }
          g2.drawString(("Round: "+round), width-fm.stringWidth("Round: "+round), fm.getHeight());
        }
        switch(lives){
          case 1: g2.drawImage(ship.getImage(), width/2-ship.getWidth()/4, 10, ship.getWidth()/2, ship.getHeight()/2, this); break;
          case 2: g2.drawImage(ship.getImage(), width/2-ship.getWidth()/4, 10, ship.getWidth()/2, ship.getHeight()/2, this); 
          g2.drawImage(ship.getImage(), width/2+ship.getWidth()/4+ship.getWidth()/2, 10, ship.getWidth()/2, ship.getHeight()/2, this);
          break;
          case 3: g2.drawImage(ship.getImage(), width/2-ship.getWidth()/4, 10, ship.getWidth()/2, ship.getHeight()/2, this); 
          g2.drawImage(ship.getImage(), width/2+ship.getWidth()/4+ship.getWidth()/2, 10, ship.getWidth()/2, ship.getHeight()/2, this);
          g2.drawImage(ship.getImage(), width/2-ship.getWidth()/4-ship.getWidth(), 10, ship.getWidth()/2, ship.getHeight()/2, this);
          break;
        }
        for(int z = 0; z<3; z++){
          for(int i = 0; i<4; i++){
            for(int o = 0; o<4; o++){
              if(shields[z]!=null){
                switch(shields[z].getHealth(i,o)){
                  case 1:g2.setColor(Color.red);g2.fillRect(shields[z].getX_pos(i,o), shields[z].getY_pos(i,o),20,20);break;
                  case 2:g2.setColor(Color.yellow);g2.fillRect(shields[z].getX_pos(i,o), shields[z].getY_pos(i,o),20,20);break;
                  case 3:g2.setColor(new Color(80, 250, 4));g2.fillRect(shields[z].getX_pos(i,o), shields[z].getY_pos(i,o),20,20);break;
                }
              }
            }
          }
        }
        g2.setColor(mainColor);
        if(gameState == 2){
          g2.drawString(restart, width/2-fm.stringWidth(restart)/2, height/2+fm.getHeight()/2);
        }
        if(gameState == 3){
          g2.drawString(winMessage, width/2-fm.stringWidth(winMessage)/2, height/2+fm.getHeight()/2);
        }
      }
    }
  }
}



