package Items;

public abstract class Swords extends Items {

    private double attackPower, defensiveBonus;

    private String type;

    public double getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(double attackPower) {
        this.attackPower = attackPower;
    }

    public double getDefensiveBonus() {
        return defensiveBonus;
    }

    public void setDefensiveBonus(double defensiveBonus) {
        this.defensiveBonus = defensiveBonus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
