import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/7/4.
 */
public class Test implements Runnable{

    // 初始化5先线程
    private static ExecutorService pool = Executors.newFixedThreadPool(20);

    // 错误信息
    public static List<String> errorMsg = new ArrayList<>();

    private File oldFIle;
    private File newFile;
    public Test(File oldFIle, File newFile) {
        super();
        this.oldFIle = oldFIle;
        this.newFile = newFile;
    }


    /**
     * @param old 原始文件夹
     * @param target 目标文件夹
     * copy file
     */
    public static void copyFile4UP(String old,String target) throws  Exception{

        File oldFile = new File(old);
        File targetFile = new File(target);
        // 文件存在
        if(oldFile.exists() && oldFile.listFiles().length > 0){
            //创建转移文件夹
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            File[] subFiles=oldFile.listFiles();
            if(null == subFiles || subFiles.length == 0){
                errorMsg.add("文件夹无数据");
            }else{
                for(File subfile:subFiles){
                    String prefix = subfile.getName().substring(subfile.getName().lastIndexOf(".") + 1);
                    if("jpg".equalsIgnoreCase(prefix)){
                        Test dto = new Test(subfile,new File(target+File.separator+subfile.getName()));
                        if (!pool.isShutdown()) {
                            Thread.sleep(1000);
                            pool.execute(dto);
                        }
                    }
                }
            }
            System.out.println("8888888888888888888888888888888888");
            pool.shutdown();
            System.out.println("999999999999999999999999999999999");
        }

    }


    @Override
    public void run() {
        copyFile();
    }

    public void copyFile() {
        InputStream bis=null;
        OutputStream bos=null;
        try {
            System.out.println("=====================================");
            bis=new BufferedInputStream(new FileInputStream(this.oldFIle), 1024*1024);
            bos=new BufferedOutputStream(new FileOutputStream(this.newFile), 1024*1024);
            byte[] buff=new byte[1024];
            int len;
            while ((len=bis.read(buff))!=-1) {
                bos.write(buff, 0, len);
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            errorMsg.add("[ "+ this.oldFIle.getName() +  " ]文件不存在!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg.add("[ "+ this.oldFIle.getName() +  " ]文件读取错误，请核实图片信息！");
        }finally{
            close(bos,bis);
        }
    }

    /**
     * 关闭流对象
     * @param clsObjs
     */
    public  void close(Closeable ...clsObjs){
        for (Closeable clsObj : clsObjs) {
            if(clsObj!=null){
                try {
                    clsObj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.err.println("关闭的对象："+clsObj);
        }
    }


    public static void main(String[] args) throws Exception{
        String path1 = ("C:\\Users\\Administrator\\Desktop\\work");
        String paht2 = ("C:\\Users\\Administrator\\Desktop\\work3");
        Test.copyFile4UP(path1,paht2);
    }


}
