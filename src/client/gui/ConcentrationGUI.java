package client.gui;

/**
 * A class that represents the View in the MVC model for concentration game.
 *
 * @author Shivani Singh ,ss5243@rit.edu
 * @author Ronit Boddu, rb1209@g.rit.edu
 */

import client.controller.ConcentrationController;
import client.model.ConcentrationModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import client.model.Observer;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcentrationGUI extends Application implements Observer<ConcentrationModel> {
    /**
     * label for the moves.
     */
    private Label moves;
    /**
     * label for the matches found.
     */
    private Label matches;
    /**
     * label to display the status of the game.
     */
    private Label status;
    /**
     * Image to set the not revealed card.
     */
    private final Image notRevealed;
    /**
     * model of MVC
     */
    private ConcentrationModel model;
    /**
     * Hashmap to map the images to letters from the ptui version of the game.
     */
    private Map<String,Image> charToImage;
    /**
     * controller from MVC
     */
    private ConcentrationController controller;
    /**
     * Gridpane consisting of clickable buttons.
     */
    private GridPane gridPane;
    /**
     * borderpane representing the game.
     */
    private BorderPane borderPane;

    /**
     * Constructor to initialise the variables
     */
    public ConcentrationGUI(){
        notRevealed = new Image(getClass().getResourceAsStream("images/pokeball.png"));
        model = new ConcentrationModel();
        charToImage = new HashMap<>();
    }

    /**
     * initialises view and controller from MVC
     * @throws IOException
     */
    @Override
    public void init() throws IOException {
        initializeView();
        List<String> args = getParameters().getRaw();
        controller = new ConcentrationController(model,args.get(0),Integer.parseInt(args.get(1)));
    }

    /**
     * create the JAVAFX view of the concentration game.
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        mapCharToImage();
        borderPane = new BorderPane();
        BorderPane innerBorderPane = new BorderPane();

        moves = new Label("Moves: "+this.model.getMovesMade());
        innerBorderPane.setLeft(moves);
        BorderPane.setAlignment(moves, Pos.BOTTOM_CENTER);

        matches = new Label("Matches: "+this.model.getMatchNum());
        innerBorderPane.setCenter(matches);
        BorderPane.setAlignment(matches, Pos.BOTTOM_CENTER);

        status = new Label(this.model.getGameStatus().name());
        innerBorderPane.setRight(status);
        BorderPane.setAlignment(status, Pos.BOTTOM_CENTER);

        makeGridPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(innerBorderPane);
        innerBorderPane.setPrefHeight(20);

        Scene scene = new Scene(borderPane);
        stage.setTitle("ConcentrationGUI");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * creates the gridpane consisting of buttons.
     */
    private void makeGridPane(){
        this.gridPane = new GridPane();
        for(int row=0;row<model.getDim();++row){
            for(int col=0;col<model.getDim();++col){
                DiscButton button = new DiscButton(notRevealed,row,col);
                button.setStyle("-fx-background-color: #00008b;");
                button.setPrefHeight(2.00);
                button.setPrefHeight(100);
                gridPane.add(button,col,row);
                ActionOnClick(button,row,col);
            }
        }
    }

    /**
     * closes the connections and running threads when the view is closed.
     * @throws IOException
     */
    @Override
    public void stop() throws IOException {
        controller.closeConnections();
    }

    /**
     * Button class representing the clickable images.
     */
    private static class DiscButton extends Button {
        /**
         * Image the button represents
         */
        private Image image;
        /**
         * Row number
         */
        private int row;
        /**
         * column number
         */
        private int col;

        /**
         * constructor to initialise varibales.
         * @param image
         * @param row
         * @param col
         */
        public DiscButton(Image image,int row, int col){
            this.image=image;
            this.row= row;
            this.col=col;
            this.setGraphic(new ImageView(this.image));
        }

        /**
         * sets the image for the button.
         * @param image
         */
        public void setImage(Image image){
            this.image=image;
            this.setGraphic(new ImageView(this.image));
        }

        /**
         * gets the row number.
         * @return
         */
        public int getRow() {
            return row;
        }

        /**
         * gets the column number.
         * @return
         */
        public int getCol() {
            return col;
        }
    }

    /**
     * returns the button at specific indexes
     * @param gridPane
     * @param row
     * @param col
     * @return
     */
    private DiscButton getButton(GridPane gridPane,int row,int col){
        Node result=null;
        ObservableList<Node> children = gridPane.getChildren();
        for(Node node:children){
            if (((DiscButton)node).getRow()==row && ((DiscButton)node).getCol()==col){
                result=node;
                break;
            }
        }
        return ((DiscButton) result);
    }

    /**
     * sends the row and column number of the clicked card to the controller.
     * @param button
     * @param row
     * @param col
     */
    private void ActionOnClick(DiscButton button,int row,int col){
        button.setOnAction(event->{
            if(model.getGameStatus()!= ConcentrationModel.Status.YOU_WIN){
                try {
                    if(model.getBoard()[row][col]==".")
                        controller.setClickedCard(row,col);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * updates the view whenever user flips a card.
     * @param card
     * @throws IOException
     * @throws InterruptedException
     */
    public void refresh(ConcentrationModel.CardUpdate card) throws IOException, InterruptedException {
        this.moves.setText("Moves: "+model.getMovesMade());
        this.matches.setText("Matches: "+model.getMatchNum());
        this.status.setText(model.getGameStatus().name());
        if(model.getGameStatus()==ConcentrationModel.Status.YOU_WIN){
            gridPane.setDisable(true);
            controller.closeConnections();
        }else{
            DiscButton button = getButton(gridPane, card.getRow(), card.getCol());
            if (card.getFace().equalsIgnoreCase(".")){
                button.setStyle("-fx-background-color: #00008b; ");
            }
            else{
                button.setStyle("-fx-background-color: #d5ded7; ");
            }
            button.setImage(charToImage.get(card.getFace()));
            ActionOnClick(button, card.getRow(), card.getCol());
        }
    }

    /**
     * updates the view whenever user flips a card.
     * @param model
     * @param card
     */
    @Override
    public void update(ConcentrationModel model,ConcentrationModel.CardUpdate card) {
        Platform.runLater(()-> {
            try {
                this.refresh(card);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * view adds itself to the observer's list.
     */
    private void initializeView() {
        this.model.addObserver(this);
    }

    /**
     * creates a hashmap to map the face letter value of the ptui version to images.
     */
    public void mapCharToImage(){
        File folder = new File("images");
        char letter='A';
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                Image image = new Image(getClass().getResourceAsStream("images/"+fileEntry.getName()));
                if(!fileEntry.getName().equalsIgnoreCase("pokeball.png")){
                    charToImage.put(Character.toString(letter++),image);
                }
                else{
                    charToImage.put(".",image);
                }
            }
        }

    }

}
