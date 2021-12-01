package api;

public class EdgeDataImp implements EdgeData {

    int src;
    int dst;
    double weight;
    String info;
    int tag;

    public EdgeDataImp(int src, int dst, double weight, String info, int tag){
        this.src = src;
        this.dst = dst;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    public EdgeDataImp(int src, int dst, double weight){
        this.src = src;
        this.dst = dst;
        this.weight = weight;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dst;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}
