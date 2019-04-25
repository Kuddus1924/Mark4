import javafx.util.Pair;

import java.util.ArrayList;

public class Model {
    private  double mu;
    private  double lymda;
    private double delta;
    private int worker;
    private int N;
    private int k;
    private int blocks;
    public Model(double mu,double lymda,double delta, int worker,int n,int k,int blocks)
    {
        this.mu = mu;
        this.lymda = lymda;
        this.delta = delta;
        this.worker = worker;
        this.N = n;
        this.k = k;
        this.blocks = blocks;
    }
    public void workFirst()
    {
        ArrayList<ArrayList<Pair<Double,Double>>> time = new ArrayList<>();
        ArrayList<Double> K = new ArrayList<>();
        ArrayList<Double> K_teor = new ArrayList<>();
        for(int i = 0; i < N; i++)
        {
            time.add(new ArrayList<Pair<Double,Double>>());
        }
        double fullTime = k * delta;
        double Tav = 0;
        double TRecAv = 0;
        int count = 0;
        for(int i = 0; i < N; i++)
        {
            double tmp = 0;
            while (tmp < fullTime)
            {
                double work = getLymda();
                double recovery = getMu();
                 time.get(i).add(new Pair(tmp,tmp+work));
                 Tav += work;
                 TRecAv += recovery;
                 tmp = tmp + work + recovery;
                 count++;
            }
        }
        Tav /= count;
        TRecAv /= count;
        for(int i = 0; i < k; i++)
        {
            int counter = 0;
            for(int j = 0; j < N; j++)
            {
                counter+= Iswork(time.get(j), i * delta);
            }
            K.add((double)counter/N);
            K_teor.add(Tav/(Tav + TRecAv));
        }
        Schedule.print(K_teor,K,"Коэф готовности", k);
    }
    public  void workSecond()
    {
        ArrayList<ArrayList<ArrayList<Pair<Double,Double>>>> list = new ArrayList<>();
        ArrayList<Double> K = new ArrayList<>();
        ArrayList<Double> K_teorUp = new ArrayList<>();
        ArrayList<Double> K_teorDown1 = new ArrayList<>();
        ArrayList<Double> K_teorDown2 = new ArrayList<>();

        for(int i = 0; i < N;i++)
        {
            list.add(Oneblock());
        }
        for(int i = 0; i < k; i++)
        {
            int counter = 0;
            for(int j = 0; j < N; j++)
            {
                if(Workhard(list.get(j), i * delta))
                {
                    counter++;
                }
            }
            K.add((double)counter/N);
            //K_teorUp.add(Math.pow(1-Math.pow(1 - (mu/(mu+lymda)),3),2));
            //K_teorDown1.add(Math.pow(1-((1 - ((2*mu*lymda + Math.pow(mu,2))/(Math.pow(2*lymda,2)+ 2*mu*lymda + Math.pow(mu,2))))*(1 - (mu/(mu+lymda)))),2));
            //K_teorDown2.add((1-Math.pow(1 - (mu/(mu+lymda)),2)) * 1-((1 - ((2*mu*lymda + Math.pow(mu,2))/(Math.pow(2*lymda,2)+ 2*mu*lymda + Math.pow(mu,2))))*(1 - (mu/(mu+lymda)))));
            K_teorUp.add((mu/(mu+lymda)) * (1-Math.pow(1 - (mu/(mu+lymda)),2)));
        }

       // Schedule.print4(K,K_teorUp,K_teorDown1,K_teorDown2,"Сложная система",k);
        Schedule.print(K,K_teorUp,"sss",k);


    }
    private boolean Workhard(ArrayList<ArrayList<Pair<Double,Double>>> list , double t)
    {
        /*if(Iswork(list.get(0),t) == 0 && Iswork(list.get(1),t) == 0 && Iswork(list.get(2),t) == 0)
        {
            return  false;
        }
         else if(Iswork(list.get(3),t) == 0 && Iswork(list.get(4),t) == 0 && Iswork(list.get(5),t) == 0)
        {
            return  false;
        }
        return true;*/
        if(Iswork(list.get(0),t) == 0 && Iswork(list.get(1),t) == 0)
        {
            return  false;
        }
        else if(Iswork(list.get(2),t) == 0 && Iswork(list.get(3),t) == 0 )
        {
            return  false;
        }
        return true;
    }

    private ArrayList<ArrayList<Pair<Double,Double>>> Oneblock()
    {
        ArrayList<ArrayList<Pair<Double,Double>>> timeWork = new ArrayList<>();
        double workerB[] = new double[worker];
        boolean times[] = new boolean[blocks];
        double t[] = new double[blocks];
        double recovery[] = new double[blocks];
        double fullTime = k * delta;
        double time = 0;
        for(int i = 0; i < worker; i++)
        {
            workerB[i] = 0.0;
        }
        for(int i = 0; i< blocks ; i++)
        {
            timeWork.add(new ArrayList<>());
            times[i] = false;
            t[i] = 0;
        }
        boolean allFlag = true;
        double nowTime = 0;
        for(int i = 0; i < blocks; i++)
        {

                double workT = getLymda();
                recovery[i] = getMu();
                double tmpsr = t[i];
                timeWork.get(i).add(new Pair<Double, Double>(tmpsr,(t[i] + workT)));
                t[i] = t[i] + workT;

        }
        while (nowTime <= fullTime)
        {
            for(int i = 0; i < blocks; i++)
            {
                if(times[i])
                {
                    double workT = getLymda();
                    double tmps = t[i];
                    t[i] = t[i] + workT;
                    double tmpss = t[i];
                    timeWork.get(i).add(new Pair<Double, Double>(tmps,tmpss));
                    times[i] = false;
                }
            }
            ArrayList<Integer> tmp = getMin(t);
            ArrayList<Integer> tmp1;
            nowTime += 0.1;
            for(int i = 0; i < blocks; i++)
            {
                int index = tmp.get(i);
                if(t[index] <= nowTime) {
                    tmp1 = getMin(workerB);
                    if (t[index] < workerB[tmp1.get(0)]) {
                        t[index] =  workerB[tmp1.get(0)];
                        times[index] = true;
                    }
                    else {
                        times[index] = true;
                    }
                    t[index] += getMu();
                    workerB[tmp1.get(0)] = t[index];
                }
            }
        }
        return timeWork;
    }
    private ArrayList<Integer> getMin(double[] time)
    {
        ArrayList<Integer> minn = new ArrayList<>();
        double [] mas = new double[time.length];
        for(int i = 0; i < time.length; i++)
        {
            mas[i] = time[i];
        }
        boolean flag = true;
        while (minn.size() != time.length)
        {
            int min = 0;
            for(int i = 0; i < mas.length;i++)
            {
                if(mas[min] > mas[i])
                {
                    min = i;
                }
            }
            minn.add(min);
            mas[min] = 100;
        }
        return minn;
    }
    private int Iswork(ArrayList<Pair<Double,Double>> time,double t)
    {
        for(int i = 0; i < time.size(); i++)
        {
            Pair<Double,Double> tmp = time.get(i);
            if(tmp.getKey() <= t && tmp.getValue() >= t)
            {
                return 1;
            }
        }
        return 0;
    }

    private double getLymda()
    {
        return Math.log(Math.random())/lymda*(-1);
    }
    private double getMu()
    {
        return Math.log(Math.random())/mu*(-1);
    }


}
