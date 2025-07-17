
package Home;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Main extends Application{
    @Override
    public void start(Stage stage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Parent root =  loader.load();
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Surge Manager");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
        
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
