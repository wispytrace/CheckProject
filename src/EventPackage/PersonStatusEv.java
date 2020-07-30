package EventPackage;


import java.sql.ResultSet;

public class PersonStatusEv {
    public EventRes eventRes = null;

    public PersonStatusEv(EventRes eventRes){
        this.eventRes = eventRes;
    }

    public void initPersonStatus(){
        try {
            ResultSet resultSet = eventRes.dbManager.dbSearch("Staff", "*", " ");
            resultSet.last();
            int num =(int) Math.sqrt(resultSet.getRow());
            resultSet.beforeFirst();
            eventRes.mainWindow.navigationPanel.personStatus.setGridNum(num);
            eventRes.mainWindow.navigationPanel.personStatus.flushStatusPanel(resultSet);
        }catch (Exception e){
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }
    }

}
