package com.mobigen.datalake.metaData.standard.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtils {

    public static String getStringCellValue(Cell cell) {
        return getStringCellValue(cell, false);
    }

    public static Long stringToLong(String longString){
        Long longValue = null;
        if(longString != null || !longString.isEmpty()){
            try{
                longValue = Long.parseLong(longString);
            }catch (NumberFormatException e){
                return null;
            }
        }
        return longValue;
    }

    public static String getStringCellValue(Cell cell, boolean required) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BLANK:
                return "";
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                if (required) {
                    return "undefined";
                } else {
                    return "";
                }

        }

    }

    public static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
