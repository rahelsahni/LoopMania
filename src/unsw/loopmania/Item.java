package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Item extends StaticEntity {
    private int purchasePrice;
    private int sellPrice;

    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
    
}
