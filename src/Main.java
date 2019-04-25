public class Main {

    public static void main(String[] args) {
        double lyambda = 0.8;
        double mu = 1.2;
        double delta = 0.1;
        int k = 100;
        int N = 50000;
        int workers = 4;

        Model mod = new Model(mu, lyambda, delta,workers, N,k,4);
        mod.workFirst();
        mod.workSecond();
    }
}
