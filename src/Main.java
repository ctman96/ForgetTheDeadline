import ui.AppFrameController;


public class Main {
    public static void main(String[] args) {

        try {
            sql.GameStoreDB.main();
        }catch (Exception e){
          e.printStackTrace();
        }
        new AppFrameController().setVisible(true);
    }
}