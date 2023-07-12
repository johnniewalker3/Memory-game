import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.*;

public class RulesBox {
      Label lblRules;

      public RulesBox(){
          lblRules = new Label();
          show();
      }

      public void show(){
          lblRules = new Label("Try to match cards that have the same Color.\n" +
                                  "If you select cards that have different color then\n" +
                                  "cards will close.The game has 3 levels easy,medium,hard.\n" +
                                  "Easy supports a grid of 3*4, medium supports a grid 4*5\n" +
                                  "and hard supports grid 5*6.");
          lblRules.setFont(Font.font("Calibri"));

          HBox hBox = new HBox(lblRules);
          hBox.setAlignment(Pos.CENTER);
          Scene scene = new Scene(hBox, 300, 300);

          Modality modality = Modality.APPLICATION_MODAL;

          Stage stage = new Stage();
          stage.initModality(modality);
          stage.setTitle("Memory game rules");
          stage.setScene(scene);
          stage.setMinWidth(300);
          stage.setMinHeight(300);

          stage.show();

      }
}
