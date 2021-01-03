import com.google.common.base.CaseFormat;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ColumnGenerator {
    public static void main(String[] args) {
        System.out.println("Start Generate");
        Scanner in = new Scanner(System.in);

        System.out.println("file name:");
        String fileName = in.nextLine();
        System.out.println("table name:");
        String tableName = in.nextLine();

        Generator(tableName, fileName);
    }

    public static Integer getEntityLength(String input){
        String s = input;
        try {
            s = s.substring(s.indexOf("(") + 1);
            s = s.substring(0, s.indexOf(")"));
        } catch (Exception e){
            return 0;
        }
        if(!s.isEmpty()) {
            Integer number = Integer.getInteger(s);
            if(number == null) return 0;
            if (number > 2)
                return number;
        }
        return 0;
    }

    public static String getEntityTypeName(String input){
        String s= input;
        if(s.contains("varchar")) s = "varchar";
        if(s.contains("date")) s = "date";
        String out = null;
        switch (s){
            case "int":
                out = "Integer";
                break;
            case "date":
                out = "Date";
                break;
            case "varchar":
                out = "String";
                break;
            case "bigint":
                out = "BigInteger";
                break;
            case "bool":
                out = "Boolean";
                break;
            case "float":
                out = "Float";
                break;
        }
        return  out;
    }

    public static String ChangeNameToCamel(String input){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, input);
    }

    public static void Generator(String tableName, String fileName) {
        try {
            String resultFileName = "result_"+tableName+".txt";
            FileWriter fileWriter = new FileWriter(resultFileName, false);
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
            String[] values;
            while ((values = csvReader.readNext()) != null) {

                if(values[0].equals(tableName) && values.length>=3){
                    //Get length String
                    Integer length = getEntityLength(values[2]);
                    String lengthStr = "";
                    if (length > 0) lengthStr = ", length = "+length;

                    //Get type String
                    String typeName = getEntityTypeName(values[2]);

                    //Get entity name string
                    String entityName = ChangeNameToCamel(values[1]);
                    //write 1st line
                    fileWriter.write("@Column(name = \""+ values[1] + "\"" + lengthStr+")"+String.format("%n"));
                    //write 2nd line
                    fileWriter.write("private "+typeName +" "+ entityName + ";" +String.format("%n"));
                    //write empty line
                    fileWriter.write(String.format("%n"));
                }
            }
            fileWriter.close();
            System.out.println("Finished. File name:" + resultFileName );
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}