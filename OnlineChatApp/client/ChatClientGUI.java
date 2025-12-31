import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class ChatClientGUI extends Application {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void start(Stage stage) throws Exception {

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        TextField inputField = new TextField();
        Button sendBtn = new Button("Send");

        VBox layout = new VBox(10, chatArea, inputField, sendBtn);
        stage.setScene(new Scene(layout, 400, 400));
        stage.setTitle("Online Chat Client");
        stage.show();

        socket = new Socket("localhost", 12345);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Receive messages
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    chatArea.appendText(msg + "\n");
                }
            } catch (IOException e) {
                chatArea.appendText("Disconnected.\n");
            }
        }).start();

        sendBtn.setOnAction(e -> {
            out.println(inputField.getText());
            inputField.clear();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
