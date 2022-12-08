package com.osstudents.fcse.aiu.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class testController {
    public Label statusMessage;
    public Button cleanText;
    private File textFile;
    @FXML
    private TextArea MainArea;

    @FXML
    private void chooseFile(ActionEvent event) throws FileNotFoundException {
        String text = "";
        FileChooser fileChooser = new FileChooser();
        textFile = fileChooser.showOpenDialog(null);
        if (textFile != null) {
            Scanner file = new Scanner(textFile);
            while(file.hasNext()){
            text+=file.nextLine();
            }
        }
        MainArea.setText(text);
    }
    @FXML
    private void saveFile(ActionEvent event) throws IOException {
        if(textFile != null){
            FileWriter writer = new FileWriter(textFile);
            writer.write(MainArea.getText());
            writer.flush();
            return;
        }
        FileChooser chooser = new FileChooser();
        File newFile = chooser.showSaveDialog(null);
        FileWriter writer = new FileWriter(newFile);
        writer.write(MainArea.getText());
        writer.flush();
    }
    @FXML
    private void onTextEdit(ActionEvent event) throws InterruptedException {
        if(MainArea.getLength()<7)
            return;
        if(MainArea.getText().charAt(MainArea.getLength()-1) != ' ')
            return;
        Thread spaceDetection = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Character> fullText = new ArrayList<>();
                for (int i = 0; i<MainArea.getLength(); i++){
                    fullText.add(MainArea.getText().charAt(i));
                }
                for (int i = 0; i<fullText.size(); i++){
                    if(fullText.get(i) == ' ' && fullText.get(i+1) == ' '){
                        int caretPosition = MainArea.getCaretPosition();
                        fullText.remove(i);
                        MainArea.positionCaret(caretPosition);
                    }
                }
                String finalString = "";
                for (int i = 0; i< fullText.size(); i++){
                    finalString += fullText.get(i);
                }
                MainArea.setText(finalString);
            }
        });
        spaceDetection.start();
        spaceDetection.join();
        Thread wordCorrection = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] ArrayPass = MainArea.getText().split(" ");
                String[][] Dictionary = EditorApp.getDic();
                for (int i =0; i<ArrayPass.length; i++){
                    for(int j = 0; j<Dictionary.length; j++){
                        if(ArrayPass[i].equals(Dictionary[j][0])){
                            ArrayPass[i] = Dictionary[j][1];
                        }
                    }
                }
            }
        });
        wordCorrection.start();

    }

}
