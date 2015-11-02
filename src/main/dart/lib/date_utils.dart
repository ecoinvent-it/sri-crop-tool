import 'package:intl/intl.dart';

class DateUtils
{
  static DateFormat _dateTimeReader = new DateFormat("yyyy-MM-dd'T'HH:mm", "en_US");
  static DateFormat _dateTimeWriter;
  static DateFormat get dateTimeWriter => _dateTimeWriter == null ? 
                                          _dateTimeWriter = new DateFormat("yMd", Intl.getCurrentLocale()).add_Hm() : 
                                          _dateTimeWriter;
  
  static DateFormat _dateReader = new DateFormat("yyyy-MM-dd", "en_US");
  static DateFormat _dateWriter;
  static DateFormat get dateWriter => _dateWriter == null ? 
                        _dateWriter = new DateFormat("yMd", Intl.getCurrentLocale()) : 
                        _dateWriter;
  
  static DateTime parseDateTime(String dateTime)
  {
    return _dateTimeReader.parse(dateTime, true).toLocal();
  }
  
  static String displayDateTime(String dateTime)
  {
    return dateTimeWriter.format(parseDateTime(dateTime));
  }
  
  static DateTime parseDate(String date)
  {
    return _dateReader.parse(date, true).toLocal();
  }
  
  static String displayDate(String date)
  {
    return dateWriter.format(parseDate(date));
  }
}