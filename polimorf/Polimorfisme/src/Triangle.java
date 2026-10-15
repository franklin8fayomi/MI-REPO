public class Triangle extends Shape {

    double b;
    double h;

    public Triangle(double b, double h) {
        this.b =  b;
        this.h = h;
    }

    @Override
    public double area() {
        return (b*h) / 2;
    }
}
