package nitjamshedpur.com.lowesproductfinder.Modal;

public class ItemModal {
    private String category, subCategory, price, floor, shelf, description, name, imageUrl;


    public ItemModal(String category, String subCategory, String price, String floor, String shelf, String description, String name, String imageUrl) {
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.floor = floor;
        this.shelf = shelf;
        this.description = description;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
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
