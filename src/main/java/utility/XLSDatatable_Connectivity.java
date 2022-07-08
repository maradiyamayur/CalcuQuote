package utility;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSDatatable_Connectivity {
	
	public XSSFSheet WorkSheet = null;
	  
	  public XSSFWorkbook XLSbook = null;
	  
	  public XSSFRow rows = null;
	  
	  public FileOutputStream fileOS = null;
	  
	  public String pathValue;
	  
	  public FileInputStream fileInputS = null;
	  
	  public XSSFCell cellNum = null;
	  
	  public XLSDatatable_Connectivity(String pathValue) {
	    this.pathValue = pathValue;
	    try {
	      this.fileInputS = new FileInputStream(pathValue);
	      this.XLSbook = new XSSFWorkbook(this.fileInputS);
	      this.WorkSheet = this.XLSbook.getSheetAt(0);
	      this.fileInputS.close();
	    } catch (Throwable t) {
	      t.printStackTrace();
	    } 
	  }
	  
	  public int totalRow(String sheet) {
	    int sheetIndexValue = this.XLSbook.getSheetIndex(sheet);
	    if (sheetIndexValue == -1)
	      return 0; 
	    this.WorkSheet = this.XLSbook.getSheetAt(sheetIndexValue);
	    int number = this.WorkSheet.getLastRowNum() + 1;
	    return number;
	  }
	  
	  public boolean sheetExistance(String sheetName) {
	    int index = this.XLSbook.getSheetIndex(sheetName);
	    if (index == -1) {
	      index = this.XLSbook.getSheetIndex(sheetName.toUpperCase());
	      if (index == -1)
	        return false; 
	      return true;
	    } 
	    return true;
	  }
	  
	  public int totalColumn(String sheet) {
	    int sheetIndexValue = this.XLSbook.getSheetIndex(sheet);
	    if (sheetIndexValue == -1)
	      return 0; 
	    this.WorkSheet = this.XLSbook.getSheet(sheet);
	    this.rows = this.WorkSheet.getRow(0);
	    int totalRow = this.rows.getLastCellNum();
	    return totalRow;
	  }
	  
	  public String getData(String sheet, String column, int row) {
	    try {
	      if (row <= 0)
	        return ""; 
	      int SheetIndexValue = this.XLSbook.getSheetIndex(sheet);
	      int col_Num = -1;
	      if (SheetIndexValue == -1)
	        return ""; 
	      this.WorkSheet = this.XLSbook.getSheetAt(SheetIndexValue);
	      this.rows = this.WorkSheet.getRow(0);
	      for (int i = 0; i < this.rows.getLastCellNum(); i++) {
	        if (this.rows.getCell(i).getStringCellValue().trim().equals(column.trim()))
	          col_Num = i; 
	      } 
	      if (col_Num == -1)
	        return ""; 
	      this.WorkSheet = this.XLSbook.getSheetAt(SheetIndexValue);
	      this.rows = this.WorkSheet.getRow(row - 1);
	      if (this.rows == null)
	        return ""; 
	      this.cellNum = this.rows.getCell(col_Num);
	      if (this.cellNum == null)
	        return ""; 
	      if (this.cellNum.getCellType() == 1)
	        return this.cellNum.getStringCellValue(); 
	      if (this.cellNum.getCellType() == 0 || this.cellNum.getCellType() == 2) {
	        String cellText = String.valueOf(this.cellNum.getNumericCellValue());
	        if (HSSFDateUtil.isCellDateFormatted((Cell)this.cellNum)) {
	          double d = this.cellNum.getNumericCellValue();
	          Calendar cal = Calendar.getInstance();
	          cal.setTime(HSSFDateUtil.getJavaDate(d));
	          cellText = 
	            String.valueOf(cal.get(1)).substring(2);
	          cellText = String.valueOf(cal.get(5)) + "/" + 
	            cal.get(2) + '\001' + "/" + 
	            cellText;
	        } 
	        return cellText;
	      } 
	      if (this.cellNum.getCellType() == 3)
	        return ""; 
	      return String.valueOf(this.cellNum.getBooleanCellValue());
	    } catch (Exception e) {
	      e.printStackTrace();
	      return "row " + row + " or column " + column + " does not exist in xls";
	    } 
	  }
	  
	  public boolean setData(String sheetName, String colName, int rowNum, String data) {
	    try {
	      this.fileInputS = new FileInputStream(this.pathValue);
	      this.XLSbook = new XSSFWorkbook(this.fileInputS);
	      if (rowNum <= 0)
	        return false; 
	      int index = this.XLSbook.getSheetIndex(sheetName);
	      int colNum = -1;
	      if (index == -1)
	        return false; 
	      this.WorkSheet = this.XLSbook.getSheetAt(index);
	      this.rows = this.WorkSheet.getRow(0);
	      for (int i = 0; i < this.rows.getLastCellNum(); i++) {
	        if (this.rows.getCell(i).getStringCellValue().trim().equals(colName))
	          colNum = i; 
	      } 
	      if (colNum == -1)
	        return false; 
	      this.WorkSheet.autoSizeColumn(colNum);
	      this.rows = this.WorkSheet.getRow(rowNum - 1);
	      if (this.rows == null)
	        this.rows = this.WorkSheet.createRow(rowNum - 1); 
	      this.cellNum = this.rows.getCell(colNum);
	      if (this.cellNum == null)
	        this.cellNum = this.rows.createCell(colNum); 
	      this.cellNum.setCellValue(data);
	      this.fileOS = new FileOutputStream(this.pathValue);
	      this.XLSbook.write(this.fileOS);
	      this.fileOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    } 
	    return true;
	  }
	  
	  public boolean setData(String sheetName, String colName, int rowNum, String data, String url) {
	    try {
	      this.fileInputS = new FileInputStream(this.pathValue);
	      this.XLSbook = new XSSFWorkbook(this.fileInputS);
	      if (rowNum <= 0)
	        return false; 
	      int index = this.XLSbook.getSheetIndex(sheetName);
	      int colNum = -1;
	      if (index == -1)
	        return false; 
	      this.WorkSheet = this.XLSbook.getSheetAt(index);
	      this.rows = this.WorkSheet.getRow(0);
	      for (int i = 0; i < this.rows.getLastCellNum(); i++) {
	        if (this.rows.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
	          colNum = i; 
	      } 
	      if (colNum == -1)
	        return false; 
	      this.WorkSheet.autoSizeColumn(colNum);
	      this.rows = this.WorkSheet.getRow(rowNum - 1);
	      if (this.rows == null)
	        this.rows = this.WorkSheet.createRow(rowNum - 1); 
	      this.cellNum = this.rows.getCell(colNum);
	      if (this.cellNum == null)
	        this.cellNum = this.rows.createCell(colNum); 
	      this.cellNum.setCellValue(data);
	      XSSFCreationHelper createHelper = this.XLSbook.getCreationHelper();
	      XSSFCellStyle xSSFCellStyle = this.XLSbook.createCellStyle();
	      XSSFFont hlink_font = this.XLSbook.createFont();
	      hlink_font.setUnderline((byte)1);
	      hlink_font.setColor(IndexedColors.BLUE.getIndex());
	      xSSFCellStyle.setFont((Font)hlink_font);
	      XSSFHyperlink link = createHelper.createHyperlink(4);
	      link.setAddress(url);
	      this.cellNum.setHyperlink((Hyperlink)link);
	      this.cellNum.setCellStyle((CellStyle)xSSFCellStyle);
	      this.fileOS = new FileOutputStream(this.pathValue);
	      this.XLSbook.write(this.fileOS);
	      this.fileOS.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    } 
	    return true;
	  }
	  
	  public String getData(String sheet, int column, int row) {
	    try {
	      if (row <= 0)
	        return ""; 
	      int index = this.XLSbook.getSheetIndex(sheet);
	      if (index == -1)
	        return ""; 
	      this.WorkSheet = this.XLSbook.getSheetAt(index);
	      this.rows = this.WorkSheet.getRow(row - 1);
	      if (this.rows == null)
	        return ""; 
	      this.cellNum = this.rows.getCell(column);
	      if (this.cellNum == null)
	        return ""; 
	      if (this.cellNum.getCellType() == 1)
	        return this.cellNum.getStringCellValue(); 
	      if (this.cellNum.getCellType() == 0 || this.cellNum.getCellType() == 2) {
	        String cellText = String.valueOf(this.cellNum.getNumericCellValue());
	        if (HSSFDateUtil.isCellDateFormatted((Cell)this.cellNum)) {
	          double d = this.cellNum.getNumericCellValue();
	          Calendar cal = Calendar.getInstance();
	          cal.setTime(HSSFDateUtil.getJavaDate(d));
	          cellText = 
	            String.valueOf(cal.get(1)).substring(2);
	          cellText = String.valueOf(cal.get(2) + 1) + "/" + 
	            cal.get(5) + "/" + 
	            cellText;
	        } 
	        return cellText;
	      } 
	      if (this.cellNum.getCellType() == 3)
	        return ""; 
	      return String.valueOf(this.cellNum.getBooleanCellValue());
	    } catch (Exception e) {
	      e.printStackTrace();
	      return "row " + row + " or column " + column + " does not exist  in xls";
	    } 
	  }

}
