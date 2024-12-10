import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


//here we created the class called BallMouseDemo and  extended Application to access some methods
class Star {
    // array of starting points
    private double growthFactor = 1.0;
    //here we are adjusting 10 different  points in order to make the star
    private double[] startingPoints = {
            400.0, 300.0,  // Top point
            370.0, 350.0,  // Right upper point
            320.0, 350.0,  // Right point
            370.0, 380.0,  // Right lower point
            350.0, 430.0,  // Bottom right point
            400.0, 400.0,  // Bottom point
            450.0, 430.0,  // Bottom left point
            430.0, 380.0,  // Left lower point
            480.0, 350.0,  // Left point
            430.0, 350.0   // Left upper point
    };
    //here we are making a list of colors in order to loop over specific colors and prevent lacking the ball color in the star .
    // color template
    private Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.BLUE, Color.INDIGO, Color.VIOLET, Color.PINK,
            Color.CYAN, Color.MAGENTA
    };
    // random class to choose new color
    private Random rand = new Random();

    // array list to save lines
    ArrayList<Line> lines = new ArrayList<>();
    // variable to hold the state of interaction with the star
    private boolean touched = false;
    //contructor
    public Star() {

        for (int i = 0; i < startingPoints.length; i += 2) {
            int nextIndex = (i + 2) % startingPoints.length;  // Wrap around to form a closed polygon
            Line line = new Line(startingPoints[i], startingPoints[i + 1], startingPoints[nextIndex], startingPoints[nextIndex + 1]);
            line.setStroke(colors[rand.nextInt(10)]);  // Set a different color for each line
            line.setStrokeWidth(2);
            lines.add(line);  // Add the line to the pane
        }

    }

    // function to return lines to be shown on the pane
    public ArrayList<Line> getLines() {
        return lines;
    }

    public void grow() {
        double centerX = 400.0;  // Center X of the screen (middle of the 800x800 scene)
        double centerY = 380.0;  // Center Y of the screen (middle of the 800x800 scene)

        // Loop through each pair of points to scale them outward
        for (int i = 0; i < startingPoints.length; i += 2) {
            double x = startingPoints[i];      // Get the current X coordinate of the point
            double y = startingPoints[i + 1];  // Get the current Y coordinate of the point

            // Calculate the vector from the center to this point
            double dx = x - centerX;
            double dy = y - centerY;

            // Scale the vector outward by the growth factor
            double newX = centerX + dx * growthFactor;
            double newY = centerY + dy * growthFactor;

            // Update the starting points with the new scaled values
            startingPoints[i] = newX;
            startingPoints[i + 1] = newY;
        }

        // Update the lines to reflect the new positions
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);

            // Get the start and end point indices for the current line
            int startIdx = i * 2;  // Each line has two consecutive points
            int endIdx = (startIdx + 2) % startingPoints.length;  // Wrap around to the first point

            // Get the new start and end coordinates for the line
            double startX = startingPoints[startIdx];
            double startY = startingPoints[startIdx + 1];
            double endX = startingPoints[endIdx];
            double endY = startingPoints[endIdx + 1];

            // Update the line's start and end coordinates
            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX(endX);
            line.setEndY(endY);
        }

        // Increase the growth factor to continue expanding
        growthFactor += 0.0008; // control the growing of the growth factor
    }

    public void setVisible(boolean state){
        if (!state) {
            for(Line line: lines){
                line.setVisible(false);

            }
        }
    }
    public void setTouched(boolean touched){
        this.touched = touched; //
    }
    public boolean isTouched(){
        return touched;
    }

}
class Ball{
    private Random rand = new Random();
    private Circle circle;
    private Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.BLUE, Color.INDIGO, Color.VIOLET, Color.PINK,
            Color.CYAN, Color.MAGENTA
    };
    public Ball(double x, double y , double radius){
        this.circle = new Circle(x ,y, radius);
    }
    public void setColorRandom(){
        this.circle.setFill(colors[rand.nextInt(10)]);
    }
    public Circle getCircle(){
        return this.circle;
    }
    public void setCenterX(double x){
        this.circle.setCenterX(x);
    }
    public void setCenterY(double y){
        this.circle.setCenterY(y);
    }
}


class Player implements Comparable<Player>{
    private String name;
    private int score;

    public void setName(String name) {
        this.name = name;
    }
    public void setScore(int score){
        this.score = score;
    }
    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }

    @Override
    public int compareTo(Player o) {
        if (this.score>o.score) {

            return -1;
        }
        else if(this.score==o.score){
            return 0;

        }
        else{
            return 1;
        }

    }
    @Override
    public String toString(){
        return name+" "+score;
    }
}



public class BallMouseDemo extends Application {
    public void intializing(ArrayList<Player> pList ) throws IOException {
        FileInputStream fileByteStream = null; // File input stream
        Scanner inFS = null;
        String name;

        fileByteStream = new FileInputStream("C:\\Users\\zahid\\IdeaProjects\\Proj\\src\\namesScores.csv");
        inFS = new Scanner(fileByteStream);

        while(inFS.hasNextLine()){
            String[] columns = inFS.nextLine().split(",");
            String tempName = columns[0];
            int tempScore = Integer.parseInt(columns[1]);
            Player p1 = new Player();
            p1.setName(tempName);
            p1.setScore(tempScore);
            pList.add(p1);




        }
        pList.sort(null);

        fileByteStream.close();


    }
    private int index = -1;
    public void startPage(Stage primaryStage){

        Text welcomeText = new Text("Welcome To The Game!!!");
        Text dashboardTitle = new Text("Go to the LeaderBoard");
        Button dashboard = new Button(" LeaderBoard");
        Text gameName = new Text("Flying Stars Game ");
        TextField playerName = new TextField();

        Text madeBy = new Text("Made By:\n *Abdulaaziz AlKhatib \n *Yaseen AlYaseen \n *Hamza Ammous ");



        madeBy.setFill(Color.FORESTGREEN);
        madeBy.setFont(Font.font("Impact",20));
        madeBy.setX(600);
        madeBy.setY(500);


        gameName.setFill(Color.BISQUE);
        gameName.setFont(Font.font("Impact",50));
        gameName.setX(10);
        gameName.setY(50);


        dashboardTitle.setFill(Color.RED);
        dashboardTitle.setFont(Font.font(20));
        dashboardTitle.setX(590);
        dashboardTitle.setY(40);


        welcomeText.setFill(Color.FORESTGREEN);
        welcomeText.setFont(Font.font("Lucida Calligraphy",25));
        welcomeText.setX(255);
        welcomeText.setY(200);

        dashboard.setTextFill(Color.RED);
        dashboard.setFont(Font.font(15));
        dashboard.setLayoutX(630);
        dashboard.setLayoutY(50);



        Button starting = new Button("Start the game");
        starting.setTextFill(Color.BLUE);
        starting.setFont(Font.font("Garamond",20));
        starting.setLayoutX(318);
        starting.setLayoutY(320);



        playerName = new TextField();
        playerName.setPrefColumnCount(15);
        playerName.setEditable(true);
        playerName.setPromptText("Enter Your Name Here ");
        playerName.setLayoutX(295);
        playerName.setLayoutY(250);

        ArrayList<Player> players = new ArrayList<Player>();
        try {
            intializing(players);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Player p: players){
            if(playerName.getText().equals(p.getName())){
                index = players.indexOf(p);


            }
        }


        TextField finalPlayerName = playerName;
        starting.setOnAction(event -> {gamePage(primaryStage,players,index, finalPlayerName.getText());});
        dashboard.setOnAction(event -> {leaderBoardPage(primaryStage,players);});




        Pane root0 = new Pane();
        root0.getChildren().addAll(welcomeText,starting,dashboardTitle,dashboard,gameName,playerName,madeBy);
        Scene scene0 = new Scene(root0, 800, 600);
        scene0.getRoot().setStyle("-fx-background-color: lightblue;");
        primaryStage.setScene(scene0);

    }
    public void update(ArrayList<Player> players, int index, int[] score, String text){
        int arrayScore;
        if (index!=-1){
            arrayScore = players.get(index).getScore();
            if (score[0]>arrayScore){
                arrayScore = score[0];
            }
            players.get(index).setScore(arrayScore);
        }
        else {
            Player newPlayer = new Player();
            newPlayer.setName(text);
            newPlayer.setScore(score[0]);
            players.add(newPlayer);

        }
        players.sort(null);
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream("C:\\Users\\zahid\\IdeaProjects\\Proj\\src\\namesScores.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PrintWriter filePrinter = new PrintWriter(fileStream);
        for (int i = 0; i<players.size(); ++i){
            filePrinter.println(players.get(i).getName()+","+players.get(i).getScore());
        }
        filePrinter.close();


    }
    private Timeline gameTimeLine;// main pain

    public void gamePage(Stage primaryStage, ArrayList<Player> players, int index, String text){
        final int[] playerScore = {0};
        Pane root = new Pane();

        //create ball and set proprties
        Ball ball = new Ball(200,200,20);
        ball.setColorRandom();
        root.getChildren().add(ball.getCircle());
        root.setOnMouseMoved(even -> {
            ball.setCenterX(even.getX());
            ball.setCenterY(even.getY());
        });


        ///// create tries, starting with 3 /////
        ArrayList<Circle> tries = new ArrayList<>();
        for (int i = 0; i< 3; ++i){
            Circle bb = new Circle(740+i*20,40,5);
            bb.setFill(Color.RED);
            tries.add(bb);
            root.getChildren().add(bb);
        }
        ////////////////////////////////////////


        // corner labels //
        Label score = new Label();
        Label reactionTimeLabel = new Label();
        reactionTimeLabel.setText("Your Average Reaction time: 0s");
        score.setText("Your score: 0");
        reactionTimeLabel.setLayoutX(0);
        reactionTimeLabel.setLayoutY(10);
        score.setLayoutX(0);
        score.setLayoutY(0);
        root.getChildren().addAll(score,reactionTimeLabel);
;
        double[] reactionTime = {0.0,0.0}; // index 0 = all time , index 1 = number of stars touched.
        ////////////////



        // running the game //
        // lastest version
        gameTimeLine = new Timeline(new KeyFrame(Duration.seconds(1.75), eveynt -> {
            Star star = new Star();
            for (Line line : star.getLines()) {
                root.getChildren().add(line);
            }
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
                star.grow();
                reactionTime[0] += 0.05; // increase the number mille second
                for (Line l : star.getLines()){
                    Shape intersection = Shape.intersect(l, ball.getCircle());
                    if (intersection.getBoundsInLocal().getWidth() > 0 && intersection.getBoundsInLocal().getHeight() > 0){ // intersect

                        if (l.getStroke().equals(ball.getCircle().getFill()) && !star.isTouched()) { // same color
                            star.setVisible(false);
                            playerScore[0]+=1;
                            star.setTouched(true); // interaction to be changed to true
                            score.setText("Your score: "+playerScore[0]);
                            reactionTime[1]++; // increase the number of starts touched
                            reactionTimeLabel.setText("Your Average Reaction time: " + reactionTime[0]/reactionTime[1]);

                        }



                        if (!(l.getStroke().equals(ball.getCircle().getFill())) && !star.isTouched()){// diffrent colors
                            // interaction to be changed to true
                            reactionTime[1]++; // increase the number of starts touched
                            reactionTimeLabel.setText("Your Average Reaction time: " + reactionTime[0]/reactionTime[1]);

                            if(tries.size()==1){
                                // here we will switch scences
                                // gameTimeLine.stop();
                                update(players,index,playerScore,text);
                                endPage(primaryStage,players);
                                gameTimeLine.stop();


                            }
                            else if (!star.isTouched()){
                                root.getChildren().remove(tries.get(tries.size()-1)); // remove last circle from pane
                                tries.removeLast(); // remove last circle from tries array
                                star.setVisible(false);
                                star.setTouched(true);

                            }
                        }
                    }
                }
                if (star.getLines().get(0).getStartY() <= -220) {
                    star.setVisible(false);
                    star.setTouched(true);
                }
            }));
            timeline.setCycleCount(-1);
            timeline.play();
        }));
        gameTimeLine.setCycleCount(-1);
        gameTimeLine.play();

        Scene scene = new Scene(root, 800, 780, Color.BLACK);
        primaryStage.setScene(scene);


    }
    public void leaderBoardPage(Stage primaryStage, ArrayList<Player> players){
        ArrayList<Player> topPlayers = new ArrayList<>(players.subList(0, Math.min(5, players.size())));//3shan ya5th top 5

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(50);
        gridPane.setVgap(20);

        Label nameHeader = new Label("Name");
        nameHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label scoreHeader = new Label("Score");
        scoreHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        gridPane.add(nameHeader, 0, 0);  // Header for "Name"
        gridPane.add(scoreHeader, 1, 0); // Header for "Score"

        for (int i = 0; i < topPlayers.size(); i++) {
            Label nameLabel = new Label(topPlayers.get(i).getName());
            nameLabel.setStyle("-fx-font-size: 18px;");

            Label scoreLabel = new Label(String.valueOf(topPlayers.get(i).getScore()));
            scoreLabel.setStyle("-fx-font-size: 18px;");

            gridPane.add(nameLabel, 0, i + 1);
            gridPane.add(scoreLabel, 1, i + 1);
        }

        Scene scene = new Scene(gridPane, 800, 780);
        primaryStage.setScene(scene);



    }
    public void endPage(Stage primaryStage,ArrayList<Player> players){
        Text gameOver = new Text("Game Over \n       *_*");
        gameOver.setFont(Font.font(60));
        gameOver.setFill(Color.RED);
        gameOver.setX(250);
        gameOver.setY(300);


        Button lbButton = new Button("Leaderboard");
        Button resetButton = new Button("Restart");
        lbButton.setPrefSize(100,50);

        lbButton.setLayoutX(250);
        lbButton.setLayoutY(500);
        resetButton.setPrefSize(100,50);
        resetButton.setLayoutX(500);
        resetButton.setLayoutY(500);
        resetButton.setOnAction(event -> {
            startPage(primaryStage);
        });
        lbButton.setOnAction(event -> {leaderBoardPage(primaryStage,players);});
        Pane root = new Pane();
        root.getChildren().addAll(gameOver,lbButton,resetButton);
        Scene scene = new Scene(root, 800, 780, Color.BLACK);
        primaryStage.setScene(scene);
    }


    //here we created the Star class in order to create our star
    public void start(Stage primaryStage) {

        startPage(primaryStage);

        primaryStage.setTitle("Flying Stars Game");
        primaryStage.show();



    }
    public static void main(String[] args) {
        launch(args);
    }}