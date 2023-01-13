package com.auro.application.util;

import com.auro.application.core.common.AppConstant;
import com.auro.application.teacher.data.model.common.MonthDataModel;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    private DateUtil() {
    }


    public static Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    public static String getCurrentDate(String returnFormat) {

        String formatDate = null;
        SimpleDateFormat returnSdf = new SimpleDateFormat(returnFormat, Locale.ENGLISH);

        switch (returnFormat) {

            case AppConstant.DateFormats.YYYY_MM_DD:

                formatDate = getCurrentFormatDate(returnSdf);
                break;

            default:
                formatDate = "N/A";
                break;
        }

        return formatDate;
    }

    public static String getcurrentYearMothsNumber() {
        Date a = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(a);
        int year = calendar.get(Calendar.YEAR);
        int months = calendar.get(Calendar.MONTH);
        String yearandmonth = String.valueOf(year) + (months + 1);
        return yearandmonth;
    }


    private static String getCurrentFormatDate(SimpleDateFormat returnSdf) {

        String formatDate = null;
        Date cominDate = new Date();
        try {
            formatDate = returnSdf.format(cominDate);

        } catch (Exception e) {
            //Do nothing here
        }
        return formatDate;
    }

    public static String convertDateFormat(String returnFormat, String comingFormat, String date) {

        String formatDate = null;
        SimpleDateFormat comingSdf = new SimpleDateFormat(comingFormat, Locale.ENGLISH);
        SimpleDateFormat returnSdf = new SimpleDateFormat(returnFormat, Locale.ENGLISH);

        switch (returnFormat) {

            case AppConstant.DateFormats.YYYY_MM_DD:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;

            case AppConstant.DateFormats.DD_MM_YYYY:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;

            case AppConstant.DateFormats.DD_MM_YY:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;
            case AppConstant.DateFormats.DD_MMM:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;

            case AppConstant.DateFormats.DD_MMMM_YYYY:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;

            case AppConstant.DateFormats.dd_MMM_yyyy:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;
            case AppConstant.DateFormats.DD_MMM_HH_MM_AA:

                formatDate = getStringFormatDate(date, comingSdf, returnSdf);

                break;

            default:
                formatDate = "N/A";
                break;
        }


        return formatDate;
    }

    private static String getStringFormatDate(String date, SimpleDateFormat comingSdf, SimpleDateFormat returnSdf) {

        String formatDate = null;
        Date cominDate;
        try {
            cominDate = comingSdf.parse(date);
            formatDate = returnSdf.format(cominDate);

        } catch (ParseException e) {
            //Do nothing here
        }
        return formatDate;
    }


    public static long getLongFromyyyyMMdd(String time) {
        SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrTime() {
        SimpleDateFormat dd = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return dd.format(new Date());
    }


    public static String getTodayDayName() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        return sdf.format(d);
    }

    public static String getNameOfDay(String date) {
        String dayName = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
            Date dt1 = format1.parse(date);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            dayName = format2.format(dt1);
            AppLogger.e("getNameOfDay-", "-" + dayName);

        } catch (Exception e) {
            AppLogger.e("getNameOfDay exception-", "-" + e.getMessage());
        }
        return dayName;
    }

    public static String getDate(String date) {
        String dateName = "";
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");
            Date dt1 = format1.parse(date);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            dateName = format2.format(dt1);

            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            String day = dayFormat.format(Date.parse(date));

            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            String month = monthFormat .format(Date.parse(date));
            dateName=day+" "+month;

            AppLogger.e("getDate dateName-", "-" + dateName);
        } catch (Exception e) {
            AppLogger.e("getDate exception-", "-" + e.getMessage());
        }
        return dateName;
    }

    public static int getCurrentDateNumber() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(date);
        return ConversionUtil.INSTANCE.convertStringToInteger(formattedDate);
    }


    public static int getNumberOfDays(int year, int month) {
        GregorianCalendar mycal = new GregorianCalendar(year, month, 1);
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

   /* public static CalenderModel getMonthYear() {
        CalenderModel calenderModel = new CalenderModel();
        Calendar c = Calendar.getInstance();
        calenderModel.setMonth(c.get(Calendar.MONTH));
        calenderModel.setYear(c.get(Calendar.YEAR));
        return calenderModel;
    }
*/


    public static String get12HourFormatTime(String time) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date dateObj = sdf.parse(time);

            return new SimpleDateFormat("K:mm a").format(dateObj);
            // AppLogger.e("get12HourFormatTime",new SimpleDateFormat("K:mm a").format(dateObj));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static boolean checkTimeBetween(String timeToCheck, String firstTime, String lastTime) {
        try {

            long currentTime = System.currentTimeMillis();
            long opentime = calulateStoreTimeStamp(firstTime);
            long closetime = calulateStoreTimeStamp(lastTime);
            if (currentTime <= opentime) {
                return false;
            } else return currentTime >= opentime && currentTime <= closetime;
        } catch (Exception e) {
            // do nothing
        }
        return false;

    }


    private static long calulateStoreTimeStamp(String storeTime) {
        try {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            String formattedDate = df.format(c);
            final SimpleDateFormat sdfrr = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH);
            final Date datetObj = sdfrr.parse(formattedDate + " " + storeTime);
            return datetObj.getTime();

        } catch (final ParseException e) {
            AppLogger.e("Exception", e.getMessage());
        }
        return 0;
    }

    public static int getcurrentMonthNumber() {
        Date a = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(a);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return month;
    }
    public static int getcurrentMonthYear() {
        Date a = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(a);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        String monthDATE = String.valueOf(month < 10 ? "0"+month : ""+month);




        AppLogger.v("Date_pradeep",""+monthDATE);

        return Integer.parseInt(year+""+monthDATE);
    }

    public static int getcurrentYearNumber() {
        Date a = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(a);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static String getMonthName() {
        String lang = ViewUtil.getLanguage();
        // Locale locale = new Locale(lang);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", new Locale(ViewUtil.getLanguage()));   //chnage gor language not display in ui  in hindi language
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }
    public static String getMonthNameForPayment() {
        String lang = ViewUtil.getLanguage();
        // Locale locale = new Locale(lang);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", new Locale("en"));   //chnage gor language not display in ui  in hindi language
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }



    public static String getMonthNameSpinner() {
        String lang = ViewUtil.getLanguage();
        // Locale locale = new Locale(lang);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", new Locale("en"));
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }

    public static List<MonthDataModel> monthDataModelList(String start) {
        int count = 0;
        AppLogger.e("Date DateUtil-", start);
        HashMap<Integer, String> monthHashmap = new HashMap<>();
        monthHashmap.put(1, "January");
        monthHashmap.put(2, "February");
        monthHashmap.put(3, "March");
        monthHashmap.put(4, "April");
        monthHashmap.put(5, "May");
        monthHashmap.put(6, "June");
        monthHashmap.put(7, "July");
        monthHashmap.put(8, "August");
        monthHashmap.put(9, "September");
        monthHashmap.put(10, "October");
        monthHashmap.put(11, "November");
        monthHashmap.put(12, "December");
        List<MonthDataModel> list = new ArrayList<>();
        try {
            // start="2019-06-15 18:21:18";
            Date b = new Date();
          //  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date a = dateFormat.parse(start);
            Calendar calenda1r = Calendar.getInstance();
            calenda1r.setTime(a);
            int year = calenda1r.get(Calendar.YEAR);
            int month = calenda1r.get(Calendar.MONTH);
            AppLogger.e("Date month name-", year + "--" + month);
            int currentYear = DateUtil.getcurrentYearNumber();
            int currentMonth = DateUtil.getcurrentMonthNumber();
            count = month + 1;
            AppLogger.e("DateUtil count-", count + "--" + month);
            while (year < currentYear) {
                MonthDataModel model = new MonthDataModel();
                model.setMonth(monthHashmap.get(count) + " " + year);
                model.setMonthNumber(count);
                model.setYear(year);
                list.add(model);
                AppLogger.e("DateUtil", year + "--" + monthHashmap.get(count));
                if (count == 12) {
                    year++;
                    count = 1;
                } else {
                    count++;
                }
            }

            while (year == currentYear && count <= (currentMonth + 1)) {
                MonthDataModel model = new MonthDataModel();
                model.setMonth(monthHashmap.get(count) + " " + year);
                model.setMonthNumber(count);
                model.setYear(year);
                list.add(model);
                AppLogger.e("DateUtil", year + "--" + monthHashmap.get(count));
                if (count == 12) {
                    year++;
                    count = 1;
                } else {
                    count++;
                }
            }

        } catch (Exception e) {
            AppLogger.e("Date exception", "months--" + e.getMessage());
        }
        return list;

    }


    private static List<Date> datesBetween(Date d1, Date d2) {
        List<Date> ret = new ArrayList<Date>();
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        while (c.getTimeInMillis() < d2.getTime()) {
            ret.add(c.getTime());
            c.add(Calendar.MONTH, 1);
        }
        return ret;
    }
}
