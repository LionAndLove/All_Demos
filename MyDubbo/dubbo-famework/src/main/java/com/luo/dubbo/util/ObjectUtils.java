package com.luo.dubbo.util;

public class ObjectUtils {
    
    public static boolean isEqual(Object a1 , Object a2){
        if(a1 == null && a2 == null){
            return true ;
        }
        if(a1 == null && a2 != null){
            return false ;
        }
        if(a1 != null && a2 == null){
            return false ;
        }
        return a1.equals(a2) ;
    }

    public static int toInt(Object object) {
        Assert.isNull(object, "null不能转换成int");
        
        Class<?> type = object.getClass();
        if(type.equals(int.class) || type.equals(Integer.class)){
            return (Integer) object; 
        }
        if(type.equals(double.class) || type.equals(Double.class)){
            return ((Double) object).intValue();
        }
        if(type.equals(float.class) || type.equals(Float.class)){
            return ((Float) object).intValue();
        }
        if(type.equals(long.class) || type.equals(Long.class)){
            return ((Long) object).intValue();
        }
        if(type.equals(short.class) || type.equals(Short.class)){
            return ((Short) object).intValue();
        }
        return (Integer) object ;
    }
    
    public static long toLong(Object object) {
        Assert.isNull(object, "null不能转换成long");
        
        Class<?> type = object.getClass();
        if(type.equals(int.class) || type.equals(Integer.class)){
            return ((Integer) object).longValue(); 
        }
        if(type.equals(double.class) || type.equals(Double.class)){
            return ((Double) object).longValue();
        }
        if(type.equals(float.class) || type.equals(Float.class)){
            return ((Float) object).longValue();
        }
        if(type.equals(long.class) || type.equals(Long.class)){
            return (Long) object ;
        }
        if(type.equals(short.class) || type.equals(Short.class)){
            return ((Short) object).longValue();
        }
        return (Long) object ;
    }
    
    public static short toShort(Object object) {
        Assert.isNull(object, "null不能转换成short");
        
        Class<?> type = object.getClass();
        if(type.equals(int.class) || type.equals(Integer.class)){
            return ((Integer) object).shortValue();
        }
        if(type.equals(double.class) || type.equals(Double.class)){
            return ((Double) object).shortValue();
        }
        if(type.equals(float.class) || type.equals(Float.class)){
            return ((Float) object).shortValue();
        }
        if(type.equals(long.class) || type.equals(Long.class)){
            return ((Long) object).shortValue();
        }
        if(type.equals(short.class) || type.equals(Short.class)){
            return (Short) object ;
        }
        return (Short) object ;
    }
    
    public static double toDouble(Object object) {
        Assert.isNull(object, "null不能转换成double");
        
        Class<?> type = object.getClass();
        if(type.equals(int.class) || type.equals(Integer.class)){
            return ((Integer) object).doubleValue(); 
        }
        if(type.equals(double.class) || type.equals(Double.class)){
            return (Double) object;
        }
        if(type.equals(float.class) || type.equals(Float.class)){
            return ((Float) object).doubleValue();
        }
        if(type.equals(long.class) || type.equals(Long.class)){
            return ((Long) object).doubleValue();
        }
        if(type.equals(short.class) || type.equals(Short.class)){
            return ((Short) object).doubleValue();
        }
        return (Double) object ;
    }
    
    public static float toFloat(Object object) {
        Assert.isNull(object, "null不能转换成float");
        
        Class<?> type = object.getClass();
        if(type.equals(int.class) || type.equals(Integer.class)){
            return ((Integer) object).floatValue();
        }
        if(type.equals(double.class) || type.equals(Double.class)){
            return ((Double) object).floatValue();
        }
        if(type.equals(float.class) || type.equals(Float.class)){
            return (Float) object ;
        }
        if(type.equals(long.class) || type.equals(Long.class)){
            return ((Long) object).floatValue();
        }
        if(type.equals(short.class) || type.equals(Short.class)){
            return ((Short) object).floatValue();
        }
        return (Float) object ;
    }
    
    public static boolean toBoolean(Object object) {
        Class<?> type = object.getClass();
        if(type.equals(boolean.class) || type.equals(Boolean.class)){
            return (Boolean) object; 
        }
        if(type.equals(int.class) || type.equals(Integer.class)){
            return ((Integer) object).equals(0)?false:true; 
        }
        return (Boolean) object ;
    }
    
    public static byte toByte(Object object) {
        Class<?> type = object.getClass();
        if(type.equals(byte.class) || type.equals(Byte.class)){
            return (Byte) object; 
        }
        return (Byte) object ;
    }
    
    public static char toChar(Object object) {
        Class<?> type = object.getClass();
        if(type.equals(char.class) || type.equals(Character.class)){
            return (Character) object; 
        }
        return (Character) object ;
    }
}
