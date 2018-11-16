package cn.com.sinofaith.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\47435\\Desktop\\wordTest\\";
        String zipFilePath = filePath+"test.zip";
        List<String> fileNames = new ArrayList<>();
        fileNames.add(filePath+"协助查询财产通知书.docx");
        fileNames.add(filePath+"image2.docx");
        File zip = new File(zipFilePath);
        File srcfile[] = new File[fileNames.size()];
        for (int j = 0, n1 = fileNames.size(); j < n1; j++) {
            srcfile[j] = new File(fileNames.get(j));
        }
        ZipFiles(srcfile, zip);
    }

    public  static void ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
