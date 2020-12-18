package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javafx.scene.shape.ArcType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Controller {
    double startX,startY;
    double[] save=new double[]{0,0,0,0};
    @FXML
    Button clean;
    @FXML
    Canvas canvas;
    @FXML
    Canvas paintCanvas;
    @FXML
    ComboBox comboBox;
    @FXML
    ComboBox imageBox;

    @FXML
    ImageView imageView;
    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        //gc.strokeRect(
        //        0,              //x of the upper left corner
        //        0,              //y of the upper left corner
        //        canvasWidth,    //width of the rectangle
         //       canvasHeight);  //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

    }
    @FXML
    public void initialize(){
        
        ObservableList geometric= FXCollections.observableArrayList();
        ObservableList images= FXCollections.observableArrayList();
        geometric.add("Circle");
        geometric.add("Rectangle");
        geometric.add("Polygon");
        geometric.add("Arc");

        for(Box box: Box.values()){
            images.add(box.name().toLowerCase());
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();
        comboBox.setItems(geometric);
        imageBox.setItems(images);
        imageBox.setOnAction(ActionEvent->{
            String str=(String) imageBox.getSelectionModel().getSelectedItem();
            Image image = null;
            try {
                image = new Image(new FileInputStream("src\\sample\\image\\"+str+".jpg"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImage(image);

        });
        comboBox.setOnAction(ActionEvent->{
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFill(Color.BLUE);
            String str=(String) comboBox.getSelectionModel().getSelectedItem();
            switch (str){
                case "Circle":
                    gc.fillOval(50,50,150,150);
                    break;
                case "Rectangle":
                    gc.fillRect(50,50,150,150);
                    break;
                case "Polygon":
                    gc.fillPolygon(new double[]{50,100,150,200,150,100},new double[]{150,200,200,150,100,100},6);
                    break;
                case "Arc":
                    gc.fillArc(50,50,150,150,90,270, ArcType.ROUND);
                    break;
            }

        });
        clean.setOnAction(ActionEvent->{
            GraphicsContext gcp = paintCanvas.getGraphicsContext2D();
            gcp.clearRect(0, 0, paintCanvas.getWidth(), paintCanvas.getHeight());

        });
        final GraphicsContext graphicsContext = paintCanvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        paintCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        startX = event.getX();
                        startY = event.getY();
                    }
                });

        paintCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {

                        graphicsContext.clearRect(save[0], save[1], save[2], save[3]);

                        //graphicsContext.lineTo(event.getX(), event.getY());
                        //graphicsContext.stroke();
                        if(event.getX() - startX < 0 && event.getY() - startY < 0){
                            graphicsContext.strokeRect(event.getX(), event.getY(), startX - event.getX(), startY - event.getY());
                            save = new double[]{event.getX(), event.getY(), startX - event.getX()+2, startY - event.getY()+2};
                        }else if(event.getX() - startX < 0){
                            graphicsContext.strokeRect(event.getX(), startY, startX - event.getX(), event.getY() - startY);
                            save = new double[]{event.getX(), startY, startX - event.getX()+2, event.getY() - startY+2};
                        }else if (event.getY() - startY < 0) {
                            graphicsContext.strokeRect(startX, event.getY(), event.getX() - startX, startY - event.getY());
                            save = new double[]{startX, event.getY(), event.getX() - startX+2, startY - event.getY()+2};
                        } else {
                            graphicsContext.strokeRect(startX, startY, event.getX() - startX, event.getY() - startY);
                            save = new double[]{startX, startY, event.getX() - startX+2, event.getY() - startY+2};
                        }

                    }
                });

        paintCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        //graphicsContext.lineTo(event.getX(), event.getY());
                        //graphicsContext.stroke();
                        if (event.getX() - startX < 0 || event.getY() - startY < 0) {
                            graphicsContext.strokeRect(event.getX(), event.getY(), startX - event.getX(), startY - event.getY());
                        } else {
                            graphicsContext.strokeRect(startX, startY, event.getX() - startX, event.getY() - startY);
                        }
                        save = new double[]{0,0,0,0};
                    }
                });
    }
}

enum Box{
    CVETOK,
    GLAZ,
    KOT;
    public Object image;
}