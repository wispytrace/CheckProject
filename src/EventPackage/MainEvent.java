package EventPackage;


import BottomDriver.DbManager;
import BottomDriver.FingerManager;
import UiComponent.MainWindow;

public class MainEvent {

    public LogoHeaderEv logoHeaderEv = null;
    public AttenDeatailEv attenDetailEv = null;
    public PersonStatusEv personStatusEv = null;
    public PersonManagerEv personManagerEv = null;
    public AttenStastisEv attenStastisEv = null;
    public SystemSetEv systemSetEv = null;
    public EventRes eventRes = null;
    public RecordEv recordEv = null;

    public MainEvent(DbManager dbManager, FingerManager fingerManager, MainWindow mainWindow){
        this.eventRes = new EventRes(dbManager, fingerManager, mainWindow);
        this.logoHeaderEv = new LogoHeaderEv(eventRes);
        this.attenDetailEv = new AttenDeatailEv(eventRes);
        this.personStatusEv = new PersonStatusEv(eventRes);
        this.personManagerEv = new PersonManagerEv(eventRes);
        this.systemSetEv = new SystemSetEv(eventRes);
        this.recordEv = new RecordEv(eventRes);
        this.attenStastisEv = new AttenStastisEv(eventRes);
        recordEv.doInit();
        logoHeaderEv.initLogoHeader();
        attenDetailEv.initAttenDetail();
        personManagerEv.initPersonManager();
        personStatusEv.initPersonStatus();
        systemSetEv.initSystemSet();
        attenStastisEv.initAttenStastis();
    }

}
