package meandow.uthgardwatchlist;

/**
 * Created by Meandow on 3/30/2017.
 */

public class Player {
    private String Name;
    private String Guild;
    private String Race;
    private String Class;
    private String Realm;
    private int Xp;
    private int Rp;
    private int Lvl;
    private int RR;

    public Player(){
    }
    public Player(String pname, String pguild, String prace, String pclass, String prealm, int pxp, int prp, int plvl, int prr) {
     Name = pname;
     Guild = pguild;
     Race = prace;
     Class = pclass;
     Realm = prealm;
     Xp = pxp;
     Rp = prp;
     Lvl = plvl;
     RR = prr;
    }


    public void setName(String pname){
        Name = pname;
    }
    public String getName() {
        return Name;
    }
    public void setGuild(String pguild){
        Guild = pguild;
    }
    public String getGuild(){
        return Guild;
    }
    public void setRace(String prace){
        Race = prace;
    }
    public String getRace(){
        return Race;
    }
    public void setClass(String pclass){
        Class = pclass;
    }
    public String getPlayerClass(){
        return Class;
    }
    public void setRealm(String prealm){
        Realm = prealm;
    }
    public String getRealm(){
        return Realm;
    }
    public void setXp(int pxp){
        Xp = pxp;
    }
    public int getXp(){
        return Xp;
    }
    public void setRp(int prp){
        Rp = prp;
    }
    public String getRp(){
        return Integer.toString(Rp);
    }
    public void setLvl(int plvl){
        Lvl = plvl;
    }
    public String getLvl(){
        return Integer.toString(Lvl);
    }
    public void setRR(int prr){
        RR = prr;
    }
    public String getRR(){


        String realmrank = Integer.toString(RR/10) +"L"+ Integer.toString(RR%10);
        return realmrank;
    }
}
