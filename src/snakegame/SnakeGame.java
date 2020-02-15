
package snakegame;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeGame extends Application {

   
    final int[] boardX = new int[484];
    final int[] boardY = new int[484];

    
    LinkedList<Position> snake = new LinkedList();

    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    Image lookToRightImage = new Image(getClass().getResourceAsStream("/images/face-look-right.jpg"));
    Image lookToLeftImage = new Image(getClass().getResourceAsStream("/images/face-look-left.jpg"));
    Image lookToUpImage = new Image(getClass().getResourceAsStream("/images/face-look-up.jpg"));
    Image lookToDownImage = new Image(getClass().getResourceAsStream("/images/face-look-down.jpg"));

    Image snakeBodyImage = new Image(getClass().getResourceAsStream("/images/body.png"));

    Image fruitImage = new Image(getClass().getResourceAsStream("/images/fruit.png"));

    int lengthOfSnake = 3;

    int[] fruitXPos = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};
    int[] fruitYPos = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460};

    Timeline timeline = new Timeline();

    int moves = 0;

    int totalScore = 0;
    int fruitEaten = 0;
    int scoreReverseCounter = 99;

    int bestScore = readBestScorefromTheFile();

    Random random = new Random();

    int xPos = random.nextInt(22);
    int yPos = 5 + random.nextInt(17);

    boolean isGameOver = false;

    private void writeBestScoreInTheFile() {
        if (totalScore >= bestScore) {
            try {
                FileOutputStream fos = new FileOutputStream("./snake-game-best-score.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                osw.write(bestScore + "");
                osw.flush();
                osw.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

   
    private int readBestScorefromTheFile() {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("./snake-game-best-score.txt"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String str = "";
            int c;
            while ((c = br.read()) != -1) {
                if (Character.isDigit(c)) {
                    str += (char) c;
                }
            }
            if (str.equals("")) {
                str = "0";
            }

            br.close();
            return Integer.parseInt(str);
 
    }  
        catch (FileNotFoundException ex) {
            Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

  
  
    }
  
  
  
  
  
    private void drawShapes(GraphicsContext gc) {
 gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 750, 500);
       
        gc.setFill(Color.color(0.2, 0.2, 0.2));
        for (int i = 6; i <= 482; i += 17) {
            for (int j = 6; j <= 482; j += 17) {
                gc.fillRect(i, j, 13, 13);
            }
        }
        gc.setFill(Color.BLACK);
        gc.fillRect(20, 20, 460, 460);


  
if (moves == 0) {
            boardX[2] = 40;
            boardX[1] = 60;
            boardX[0] = 80;
            boardY[2] = 100;
            boardY[1] = 100;
            boardY[0] = 100;
            scoreReverseCounter = 99;
            timeline.play();
        }
        gc.drawImage(lookToRightImage, boardX[0], boardY[0]);

    gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        gc.fillText("Snake", 575, 35);
        
    gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gc.fillText("by", 605, 55);
        gc.fillText("AShraf & Hazem & Noura & Fatma", 510, 70);
        
    gc.setFill(Color.CYAN);
    
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        gc.fillText("+ " + scoreReverseCounter, 510, 222);
        
        if (scoreReverseCounter != 10) {
            scoreReverseCounter--;
        }

    gc.setFill(Color.LIGHTGRAY);
    gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        gc.fillText("Best Score", 576, 110);
        gc.fillRect(550, 120, 140, 30);
        gc.setFill(Color.BLACK);
        gc.fillRect(551, 121, 138, 28);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(bestScore + "", 550 + (142 - new Text(bestScore + "").getLayoutBounds().getWidth()) / 2, 142);

        
        if (totalScore > bestScore) {
            bestScore = totalScore;
        }
    

        gc.fillText("Total Score", 573, 190);
        gc.fillRect(550, 200, 140, 30);
        gc.setFill(Color.BLACK);
        gc.fillRect(551, 201, 138, 28);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(totalScore + "", 550 + (142 - new Text(totalScore + "").getLayoutBounds().getWidth()) / 2, 222);

        gc.fillText("Fruit Eaten", 575, 270);
        gc.fillRect(550, 280, 140, 30);
        gc.setFill(Color.BLACK);
        gc.fillRect(551, 281, 138, 28);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(fruitEaten + "", 550 + (142 - new Text(fruitEaten + "").getLayoutBounds().getWidth()) / 2, 302);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.fillText("Controls", 550, 360);

        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        gc.fillText("Pause / Start : Space", 550, 385);
        gc.fillText("Move Up : Arrow Up", 550, 410);
        gc.fillText("Move Down : Arrow Down", 550, 435);
        gc.fillText("Move Left : Arrow Left", 550, 460);
        gc.fillText("Move Right : Arrow Right", 550, 485);
        
        snake.clear();

        for (int i = 0; i < lengthOfSnake; i++) {
            if (i == 0 && left) {
                gc.drawImage(lookToLeftImage, boardX[i], boardY[i]);
            } else if (i == 0 && right) {
                gc.drawImage(lookToRightImage, boardX[i], boardY[i]);
            } else if (i == 0 && up) {
                gc.drawImage(lookToUpImage, boardX[i], boardY[i]);
            } else if (i == 0 && down) {
                gc.drawImage(lookToDownImage, boardX[i], boardY[i]);
            } else if (i != 0) {
                gc.drawImage(snakeBodyImage, boardX[i], boardY[i]);
            }

            snake.add(new Position(boardX[i], boardY[i]));
        }

        if (scoreReverseCounter != 10) {
            scoreReverseCounter--;
        }

        for (int i = 1; i < lengthOfSnake; i++) {
            if (boardX[i] == boardX[0] && boardY[i] == boardY[0])
            {
                if (right)
                    gc.drawImage(lookToRightImage, boardX[1], boardY[1]);
                
                else if (left)
                    gc.drawImage(lookToLeftImage, boardX[1], boardY[1]);
                
                else if (up)
                    gc.drawImage(lookToUpImage, boardX[1], boardY[1]);
                
                else if (down)
                    gc.drawImage(lookToDownImage, boardX[1], boardY[1]);

               
                isGameOver = true;

                timeline.stop();

                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 50));
                gc.fillText("Game Over", 110, 220);

                gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                gc.fillText("Press Space To Restart", 130, 260);

                writeBestScoreInTheFile();
            }
        }

        if ((fruitXPos[xPos] == boardX[0]) && fruitYPos[yPos] == boardY[0]) {
            totalScore += scoreReverseCounter;
            scoreReverseCounter = 99;
            fruitEaten++;
            lengthOfSnake++;
        }

        for (int i = 0; i < snake.size(); i++) {
            if (snake.get(i).x == fruitXPos[xPos] && snake.get(i).y == fruitYPos[yPos]) {
                xPos = random.nextInt(22);
                yPos = random.nextInt(22);
            }
        }

        gc.drawImage(fruitImage, fruitXPos[xPos], fruitYPos[yPos]);

        if (right)
        {
            for (int i = lengthOfSnake - 1; i >= 0; i--)
                boardY[i + 1] = boardY[i];

            for (int i = lengthOfSnake; i >= 0; i--)
            {
                if (i == 0)
                    boardX[i] = boardX[i] + 20;
                else
                    boardX[i] = boardX[i - 1];
                

                if (boardX[i] > 460) 
                    boardX[i] = 20;
            }
        } 
        else if (left) {
            for (int i = lengthOfSnake - 1; i >= 0; i--)
                boardY[i + 1] = boardY[i];

            for (int i = lengthOfSnake; i >= 0; i--)
            {
                if (i == 0)
                    boardX[i] = boardX[i] - 20;
                
                else
                    boardX[i] = boardX[i - 1];

                if (boardX[i] < 20)
                    boardX[i] = 460;
            }
        }
        else if (up)
        {
            for (int i = lengthOfSnake - 1; i >= 0; i--)
                boardX[i + 1] = boardX[i];

            for (int i = lengthOfSnake; i >= 0; i--)
            {
                if (i == 0)
                    boardY[i] = boardY[i] - 20;
                else
                    boardY[i] = boardY[i - 1];

                if (boardY[i] < 20)
                    boardY[i] = 460;
            }
        } 
        else if (down)
        {
            for (int i = lengthOfSnake - 1; i >= 0; i--)
                boardX[i + 1] = boardX[i];

            for (int i = lengthOfSnake; i >= 0; i--)
            {
                if (i == 0)
                    boardY[i] = boardY[i] + 20;
                else
                    boardY[i] = boardY[i - 1];

                if (boardY[i] > 460)
                    boardY[i] = 20;
            }
        }
        
    }

    
    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(750, 500);
        
       GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        stage.setTitle("Snake --O:");

        stage.setScene(scene);

        stage.show();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1), (ActionEvent event) -> {
            drawShapes(gc);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {

            if (null != e.getCode())
            {
                switch (e.getCode())
                {
                    case SPACE:
                        if (timeline.getStatus() == Timeline.Status.RUNNING && isGameOver == false)
                        {
                            timeline.stop();
                        }
                        else if (timeline.getStatus() != Timeline.Status.RUNNING && isGameOver == false)
                        {
                            timeline.play();
                        }
                        else if (timeline.getStatus() != Timeline.Status.RUNNING && isGameOver == true)
                        {
                            isGameOver = false;
                            timeline.play();
                            moves = 0;
                            totalScore = 0;
                            fruitEaten = 0;
                            lengthOfSnake = 3;
                            right = true;
                            left = false;
                            xPos = random.nextInt(22);
                            yPos = 5 + random.nextInt(17);
                        }
                        break;

                    case RIGHT:
                        moves++;
                        right = true;
                        if (!left) {
                            right = true;
                        }
                        else
                        {
                            right = false;
                            left = true;
                        }
                        up = false;
                        down = false;
                        break;

                    case LEFT:
                        moves++;
                        left = true;
                        if (!right)
                        {
                            left = true;
                        }
                        else
                        {
                            left = false;
                            right = true;
                        }
                        up = false;
                        down = false;
                        break;

                    case UP:
                        moves++;
                        up = true;
                        if (!down)
                        {
                            up = true;
                        }
                        else {
                            up = false;
                            down = true;
                        }
                        left = false;
                        right = false;
                        break;

                    case DOWN:
                        moves++;
                        down = true;
                        if (!up)
                        {
                            down = true;
                        }
                        else {
                            up = true;
                            down = false;
                        }
                        left = false;
                        right = false;
                        break;
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
