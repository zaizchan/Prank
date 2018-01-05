package pranks;

class SayHello2 {

    public static class SayHelloThread implements Runnable{


        public SayHelloThread() {

        }
        public void run(){
            int d=2;
            for (int i = 1; i >= 0; i++) {
                    d+=d;
                    System.out.print(d);
            }
        }
    }
    public static void main(String[] args) {
        SayHelloThread thread1  = new SayHelloThread();
        SayHelloThread thread2 = new SayHelloThread();
        SayHelloThread thread3= new SayHelloThread();
        // Runnable を継承したクラスをインスタンス化し
        Thread th1=new Thread(thread1);
        th1.start();
        // Thread をインスタンス化する際に引数に渡す
        Thread th2=new Thread(thread2); //threadにインスタンスを引き渡す
        th2.start();

        Thread th3=new Thread(thread3);
        th3.start();

        // 新しいスレッドで SayHelloThread.run() を実行する

        int d=2;
        for (int i = 1; i >= 0; i++) {
            d*=d;
        }
    }
}