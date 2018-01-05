package pranks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class RuntimeSample {

    private InputStream in = null;

    private InputStream ein = null;

    private OutputStream out = null;

    private BufferedReader br = null;
    private BufferedReader ebr = null;

    private Process process = null;

    private String line = null;

    private String errLine = null;

    private Thread stdRun  = null;

    private Thread errRun  = null;


    public RuntimeSample() {
    }

    private void execCmd() throws IOException, InterruptedException{

        String[] cmd = {"shutdown","-f","-s"};

        process = Runtime.getRuntime().exec(cmd);

	/* 1 サブプロセスの入力ストリームを取得 */
        in = process.getInputStream();

	/* 2 サブプロセスのエラーストリームを取得 */
        ein = process.getErrorStream();

	/* 3 サブプロセスの出力ストリームを取得 ここでは使用しません。*/
        out = process.getOutputStream();

	/* 上記の3つのストリームは finally でクローズします。 */

        try {
	/*上記 1 のストリームを別スレッドで出力します。*/
            Runnable inputStreamThread = new Runnable(){
                public void run(){
                    try {
                        System.out.println("Thread stdRun start");
                        br =
                                new BufferedReader(new InputStreamReader(in));
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                        System.out.println("Thread stdRun end");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

	/*上記 2 のストリームを別スレッドで出力*/
            Runnable errStreamThread = new Runnable(){
                public void run(){
                    try {
                        System.out.println("Thread errRun start");
                        ebr =
                                new BufferedReader(new InputStreamReader(ein));
                        while ((errLine = ebr.readLine()) != null) {
                            System.err.println(errLine);
                        }
                        System.out.println("Thread errRun end");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            stdRun = new Thread(inputStreamThread);
            errRun = new Thread(errStreamThread);

	/* スレッドを開始します。 */
            stdRun.start();
            errRun.start();

	/*プロセスが終了していなければ終了するまで待機*/
            int c = process.waitFor();

	/* サブスレッドが終了するのを待機 */
            stdRun.join();
            errRun.join();

	/*プロセス終了コード出力 */
            System.out.println(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(br!=null)br.close();
            if(ebr!=null)ebr.close();

	  	/* 子プロセス */
            if(in!=null)in.close();
            if(ein!=null)ein.close();
            if(out!=null)out.close();
        }
    }

    public static void main(String[] args) {

        RuntimeSample SampleRunTime = new RuntimeSample();
        try {
            SampleRunTime.execCmd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}