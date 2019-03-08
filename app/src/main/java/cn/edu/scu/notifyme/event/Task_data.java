package cn.edu.scu.notifyme.event;

public class Task_data {
    private int logo;
    private String headline;
    private String secondline;

    public Task_data(int logo, String headline, String secondline){
        this.logo = logo;
        this.headline = headline;
        this.secondline = secondline;
    }

    public int getLogo() {
        return logo;
    }
    public String getHeadline(){
        return headline;
    }
    public String getSecondline(){
        return secondline;
    }
}
