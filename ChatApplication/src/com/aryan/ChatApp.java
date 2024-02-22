package com.aryan;



import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


public class ChatApp extends Application{

private boolean isServer = false;

private TextArea messages = new TextArea();
private NetworkConnection connection = isServer ? createServer(): createClient();

private Parent createContent(){
messages.setPrefHeight(550);
TextField input = new TextField();
Button sendButton = new Button("Send");
   sendButton.setDefaultButton(true);
   sendButton.setOnAction(event -> {
String message= isServer ? "Molini: " : "Shreyash: " ;
message += input.getText();
input.clear();

messages.appendText(message +"\n");

try{
connection.send(message);
}

catch(Exception e) {
messages.appendText("Failed to send\n");
}

});
   sendButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

VBox root = new VBox (20, messages, input,sendButton);
root.setPrefSize(600,600);
root.setStyle("-fx-background-color:white;");
return root;
}
@Override
    public void init() throws Exception {
connection.startConnection();
}
   
   
    @Override
    public void start(Stage primaryStage) throws Exception {

primaryStage.setScene(new Scene(createContent()));
primaryStage.show();
}

@Override
public void stop() throws Exception {
connection.closeConnection();
}


 private Server createServer() {
 return new Server(55555, data -> {
 Platform.runLater(() -> {
 messages.appendText(data.toString()+ "\n");
       
      });
 });
 }
 
private Client createClient(){
  return new Client("127.0.0.1",55555, data -> {
 Platform.runLater(() ->  {
 messages.appendText(data.toString()+ "\n");
 
  });
  });
}
   
 
public static void main(String[] args) {
launch(args);


}
}
