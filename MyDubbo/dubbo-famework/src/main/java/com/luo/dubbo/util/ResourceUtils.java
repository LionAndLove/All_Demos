package com.luo.dubbo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceUtils {
    
    
    public static List<String> listSpringXml(String dir){
        List<String> result = new ArrayList<String>();
        File rootFile = new File(dir);
        if(rootFile.isFile() && rootFile.getName().endsWith(".xml")){
            result.add(rootFile.getAbsolutePath());
            return result ;
        }
        if(rootFile.isDirectory()){
            for(File f : rootFile.listFiles()){
                if(f.isFile() && f.getName().endsWith(".xml")){
                    result.add(f.getAbsolutePath());
                }
            }
        }
        return result ;
    }
}
