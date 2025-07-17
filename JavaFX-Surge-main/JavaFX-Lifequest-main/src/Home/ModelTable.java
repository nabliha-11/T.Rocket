/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

/**
 *
 * @author USER
 */
public class ModelTable {

    String Activity, XP_gained, Hours_spent, total_XP, bonus_xp;

    public ModelTable(String Activity, String XP_gained, String Hours_spent) {
        this.Activity = Activity;
        this.XP_gained = XP_gained;
        this.Hours_spent = Hours_spent;
        
    }
    public ModelTable(String total_XP, String bonus_xp){
        this.total_XP = total_XP;
        this.bonus_xp = bonus_xp;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String Activity) {
        this.Activity = Activity;
    }

    public String getXP_gained() {
        return XP_gained;
    }

    public void setXP_gained(String XP_gained) {
        this.XP_gained = XP_gained;
    }

    public String getHours_spent() {
        return Hours_spent;
    }

    public void setHours_spent(String Hours_spent) {
        this.Hours_spent = Hours_spent;
    }

    public String getTotal_XP() {
        return total_XP;
    }

    public void setTotal_XP(String total_XP) {
        this.total_XP = total_XP;
    }

    public String getBonus_xp() {
        return bonus_xp;
    }

    public void setBonus_xp(String bonus_xp) {
        this.bonus_xp = bonus_xp;
    }

}
