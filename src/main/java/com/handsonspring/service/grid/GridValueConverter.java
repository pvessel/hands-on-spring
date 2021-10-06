package com.handsonspring.service.grid;

public class GridValueConverter {


    /**
     * Converts cell value to boolean
     * @param value cell value
     * @return boolean
     */
    public static Boolean convertToBoolean(Object value) {
        if(value instanceof String) {
            return Boolean.valueOf((String) value);
        } else if (null == value) {
            return false;
        }
        return (boolean) value;
    }

    /**
     * Converts cell value to float
     * @param value cell value
     * @return boolean
     */
    public static float convertToFloat(Object value) {
        if(value instanceof Integer) {
            int primitiveValue = (int) value;
            return (float) primitiveValue;
        } else if (value instanceof Double){
            double primitiveValue = (double) value;
            return (float) primitiveValue;
        } else if (value instanceof String && !((String) value).isEmpty()){
            float primitiveValue = Float.parseFloat((String) value);
            return primitiveValue;
        } else if (value instanceof Float){
            return (float) value;
        } else {
            return 0f;
        }
    }
}
