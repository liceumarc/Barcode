public class PixelRGB {
    int r;
    int g;
    int b;

    PixelRGB(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public double calculoIluminacion() {
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    public int es0or1(){
        if (calculoIluminacion() < 128){
            return 0;
        } else {
            return 1;
        }
    }

    public String toString(){
        return "red: " + r + " g: " + g + " b: " + b;
    }
}
