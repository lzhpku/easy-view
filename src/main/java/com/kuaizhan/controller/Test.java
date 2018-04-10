package com.kuaizhan.controller;

/**
 * Created by lanzheng on 2018/4/9.
 */
public class Test {

    public static void main(String [] args) {

        Runtime rt = Runtime.getRuntime();
        Process proc;
        try{
            if(System.getProperty("os.name").startsWith("Mac OS X")){
                proc = rt.exec("ls");
            }else{
                proc = rt.exec("ls");
            }
            System.out.println("Before calling waitFor() method.");
            proc.waitFor(); //  try removing this line
            System.out.println("After calling waitFor() method.");
        }catch(Exception e){
            System.out.println("Contacts is an unknown command.");
        }

//        Runtime runtime = Runtime.getRuntime();
//        try {
//            String[] command = new String[]{"/bin/bash", "-c","ls"};
//            Process p = Runtime.getRuntime().exec(command);
////            String command = "sudo /Applications/Google\\ Chrome.app/Contents/MacOS/Google\\ Chrome -kiosk -fullscreen http://wikipedia.org";
////            Process p = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", command});
////            p.waitFor();
////            p.destroy();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
