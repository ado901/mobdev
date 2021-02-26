package Utility;

public class Utility {
    public static String concatenateDigits(int... digits) {
        StringBuilder sb = new StringBuilder(digits.length);
        for (int digit : digits) {
            sb.append(digit);
        }
        return sb.toString();
    }

    public static String intToStringDate(Integer date) {
        //20180905
        String result;
        String stringdate = date.toString();
        if (stringdate.length() == 8) {

            result = stringdate.substring(0, 4) + "/" + stringdate.substring(4, 6) + "/" + stringdate.substring(6, 8);


            return result;

        } else {
            return null;
        }

    }

    public static Integer stringToIntDate(String date) {
        //2018/09/05
        Integer result;

        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);

        String resultString = year + month + day;
        result = Integer.parseInt(resultString);


        return result;
    }

}
