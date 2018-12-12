import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 文件拷贝
 * Created by Administrator on 2018/12/4.
 */
public class ThreadCopyFile{

    // 初始化10先线程
    private static ExecutorService pool = Executors.newFixedThreadPool(10);


    /**
     * @param old 原始文件夹
     * @param target 目标文件夹
     * copy file
     */
    public static List<String> copyFile4UP(String old,String target) throws Exception {

        List<String> errorMsg = new ArrayList<>();

        File oldFile = new File(old);
        File targetFile = new File(target);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<>();
        // 文件存在
        if(oldFile.exists() && oldFile.listFiles().length > 0){
            //创建转移文件夹
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            for(File subfile:oldFile.listFiles()){
                String prefix = subfile.getName().substring(subfile.getName().lastIndexOf(".") + 1);
                if("jpg".equalsIgnoreCase(prefix)){
                    if (!pool.isShutdown()) {
                        Callable c = new MyCallable(subfile,new File(target+File.separator+subfile.getName()));
                        Future f = pool.submit(c);
                        list.add(f);
                    }
                }else{
                    errorMsg.add("文件[" + subfile.getName()+  "]格式不正确，请核实");
                }
            }
            pool.shutdown();

            // 获取所有并发任务的运行结果
            for (Future f : list) {
                try {
                    String resultMsg = f.get().toString();
                    if(StringUtils.isNotBlank(resultMsg)){
                        errorMsg.add(resultMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("================================文件转移完成================");
        }else{
            if(oldFile.exists()){
                errorMsg.add("文件夹无数据");
            }else{
                errorMsg.add("文件不存在");
            }
        }
        return errorMsg;
    }


    public static String copyFile(File oldFIle, File newFile) {
        String msg = "";
        InputStream bis=null;
        OutputStream bos=null;
        try {
            bis=new BufferedInputStream(new FileInputStream(oldFIle), 1024*1024);
            bos=new BufferedOutputStream(new FileOutputStream(newFile), 1024*1024);
            byte[] buff=new byte[1024];
            int len;
            while ((len=bis.read(buff))!=-1) {
                bos.write(buff, 0, len);
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            msg = "["+ oldFIle.getName() + "] 文件不存在!";
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            msg = "["+ oldFIle.getName() + "] 文件读取错误，请核实图片信息！";
        }finally{
            close(bos,bis);
        }
        return msg;
    }

    /**
     * 关闭流对象
     * @param clsObjs
     */
    public static void close(Closeable... clsObjs){
        for (Closeable clsObj : clsObjs) {
            if(clsObj!=null){
                try {
                    clsObj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

     static class MyCallable implements Callable<Object> {

         private File oldFIle;      // 源文件
         private File newFile;      // 目标文件

        public MyCallable(File old,File newFile) {
            this.oldFIle = old;
            this.newFile = newFile;
        }

        public Object call() throws Exception {
            return copyFile(oldFIle,newFile);
        }

    }


//    public static void main(String[] args) throws  Exception{
//        String path1 = ("C:\\Users\\Administrator\\Desktop\\work");
//        String paht2 = ("C:\\Users\\Administrator\\Desktop\\work3");
//        System.out.println(System.currentTimeMillis());
//        List<String> list = ThreadCopyFile.copyFile4UP(path1,paht2);
//        if(null != list && list.size()>0){
//            for(String a :list){
//                System.out.println(a);
//            }
//        }
//        System.out.println(System.currentTimeMillis());
//    }

}
