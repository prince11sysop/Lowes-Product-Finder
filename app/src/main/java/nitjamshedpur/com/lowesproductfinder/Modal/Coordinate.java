package nitjamshedpur.com.lowesproductfinder.Modal;

public class Coordinate {

    private String selfName;
    private float scaleX;
    private float scaleY;
    private float x;
    private float y;

    public Coordinate(){

    }

    public Coordinate(String selfName, float scaleX, float scaleY,float x,float y) {
        this.selfName = selfName;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.x=x;
        this.y=y;
    }

    public String getSelfName() {
        return selfName;
    }

    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
