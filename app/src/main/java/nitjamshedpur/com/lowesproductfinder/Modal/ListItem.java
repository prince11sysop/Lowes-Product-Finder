package nitjamshedpur.com.lowesproductfinder.Modal;

public class ListItem {
    private String category, subCategory, price, floor, shelf, description, name;
    private int itemCount;
    private boolean status;

    public ListItem(String category, String subCategory, String price, String floor, String shelf, String description, String name, int itemCount, boolean status) {
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.floor = floor;
        this.shelf = shelf;
        this.description = description;
        this.name = name;
        this.itemCount = itemCount;
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getItemCount() {
        return itemCount;
    }

    public boolean isStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getPrice() {
        return price;
    }

    public String getFloor() {
        return floor;
    }

    public String getShelf() {
        return shelf;
    }
}
