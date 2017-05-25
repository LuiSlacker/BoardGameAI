package de.htw.lenz.main;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class CustomFormatter extends SimpleFormatter{

  public static void main(String[] args) {
  }
  
  @Override
  public String format(LogRecord record){
    if(record.getLevel() == Level.INFO){
      return record.getMessage() + "\r\n";
    }else{
      return super.format(record);
    }
  }

}
