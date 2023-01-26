package Model;


public class TestParts extends Part {

    private int extraPart;

    public TestParts(int id, String name, double price, int stock, int min, int max, int extraPart) {
        super(id, name, price, stock, min, max);
        this.extraPart = extraPart;
    }

    public static void addPart(TestParts light) {
    }

    public int getExtraPart() {
        return extraPart;
    }

    //test data, not sure it works
    static void addTestData(){
        TestParts light = new TestParts(4, "light", 12.00, 5, 0, 0, 0);
        TestParts.addPart(light);

    }

}
