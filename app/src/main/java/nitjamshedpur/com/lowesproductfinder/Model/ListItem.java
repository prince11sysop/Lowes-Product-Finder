package nitjamshedpur.com.lowesproductfinder.Model;

public class ListItem {
    private String itemName;
    private int itemCount;
    private boolean status;
    private String shelf;

    public ListItem(String itemName, int itemCount,boolean status, String shelf) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.status=status;
        this.shelf=shelf;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }
}
