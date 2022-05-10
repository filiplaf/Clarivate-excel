package tests;

import static io.restassured.RestAssured.given;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sun.xml.xsom.XmlString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.path.xml.XmlPath;
import org.json.JSONObject;
import org.json.XML;

public class SmartExcel {
public static Logger log = LogManager.getLogger();

//For upload excel, absolute path
public static String excel() {
    String filename = "media/API Call Spreadsheet V2.xlsx";
    File file = new File(filename);
    String path = file.getAbsolutePath();
    return path;
}


//public static List<SmartRow> getRows() throws Exception {
//	FileInputStream fis = new FileInputStream(excel());
//	XSSFWorkbook workbook = new XSSFWorkbook(fis);
//	List<SmartRow> smartRows = new ArrayList<SmartRow>();
//	int sheets = workbook.getNumberOfSheets();
//	for(int i=0;i<sheets;i++) {
//		if(workbook.getSheetName(i).equalsIgnoreCase("Sheet1")){
//			XSSFSheet sheet = workbook.getSheetAt(i);
//			
//			Iterator<Row> rows = sheet.iterator();
//			
//			Row firstrow = rows.next();
//			Iterator<Cell> cell=firstrow.cellIterator();
//
//			while(rows.hasNext()){
//			//for (int column =  1; column < 15; column++) 
//				{
//					int column = 0;
//				Row r = rows.next();
//				r.getCell(column).setCellType(CellType.STRING);
//				//columnData.add(r.getCell(column).getStringCellValue());
//				smartRows.add(new SmartRow (r.getCell(0).getStringCellValue(),
//								(r.getCell(1).getStringCellValue())));
//			}
//			
//			}			
//		}
//	}
//	return smartRows;
//	
//}
@Test (priority = 1)
public static ArrayList<String> getData(String columnName) throws IOException {
	
	ArrayList<String> columnData = new ArrayList<String>();
	
	FileInputStream fis = new FileInputStream(excel());
	XSSFWorkbook workbook = new XSSFWorkbook(fis);
	
	int sheets = workbook.getNumberOfSheets();
	for(int i=0;i<sheets;i++) {
		if(workbook.getSheetName(i).equalsIgnoreCase("Sheet1")){
			XSSFSheet sheet = workbook.getSheetAt(i);
			
			Iterator<Row> rows = sheet.iterator();
			Row firstrow = rows.next();
			Iterator<Cell> cell=firstrow.cellIterator();
			int k=0;
			int column = 0;
			while(cell.hasNext()) {
				Cell value = cell.next();
				if(value.getStringCellValue().equalsIgnoreCase(columnName)) {
					column=k;
				}
				k++;
			}
			while(rows.hasNext()){
				Row r = rows.next();
				r.getCell(column).setCellType(CellType.STRING);
				columnData.add(r.getCell(column).getStringCellValue());
			}			
		}
	}
	return columnData;
}
	
	@Test
	public static void smartCom() throws Exception {

	//izvlacim podatke iz kolone u nizove
	ArrayList<String> data = getData("Input");
	ArrayList<String> data1 = getData("Template Selector ID");
	ArrayList<String> data2 = getData("Letter No");
		//for(int i=0;i<data.size();i++) {
		for(int i=0;i<1;i++) {
		HashMap<String, Object>  map = new HashMap<>();
		map.put("queue", "TM1");
		map.put("type", "TRANSACTION_FILE");
		map.put("input", data.get(i));
		map.put("config", data1.get(i));
		map.put("name", data2.get(i));
					
		//Postavljamo default URI
		 RestAssured.baseURI ="https://eu4.smartcommunications.cloud/one/oauth1/bulkServices/api/v2";
		 Response res = given().
				 	accept(ContentType.JSON).
				 	header("Content-Type","application/json").
				 	auth().
				 	oauth("50c26977-f996-4d54-9492-d5026cba0ee4!appliance@cpaglobal.com.dev", "be6e1665-5e54-440e-b71d-7ef9421bf92e", "", "").
				 	contentType(ContentType.JSON).
				 	body(map).
					when().
					post("/jobs").
					then().assertThat().statusCode(201). //cekiramo da li je status 201
					//log().all().
					//Uzimamo response
					extract().response();
		 
		    //Response izdvajam kao string i ispisujem ga u konzolu kao JSON
			String responseString = res.asString();
			log.info(responseString +"Passed");
			//JSONObject xmlJSONObj = XML.toJSONObject(responseString);
            //System.out.println(xmlJSONObj);
		}}
	
	
			@Test(priority = 2)
			public static void smartCom2() throws Exception {

			//izvlacim podatke iz kolone u nizove
			ArrayList<String> data = getData("Input");
			ArrayList<String> data1 = getData("Template Selector ID");
			ArrayList<String> data2 = getData("Letter No");
				//for(int i=0;i<data.size();i++) {
				for(int i=0;i<1;i++) {
				HashMap<String, Object>  map = new HashMap<>();
				map.put("queue", "TM1");
				map.put("type", "TRANSACTION_FILE");
				map.put("input", "/mnt/autofs/input/135_20170322_121833_0.xml");
				map.put("config", "713000116");
				map.put("name", "Filip111");
							
				//Postavljamo default URI
				String json = "{\r\n" + 
						"\"queue\": \"TM1\"\r\n" + 
						"\"type\": \"TRANSACTION_FILE\"\r\n" +
						"\"input\": \"/mnt/autofs/input/135_20170322_121833_1.xml\"\r\n" + 
						"\"config\": \"713000116\"\r\n" + 
						"\"name\": \"Filip11\"\r\n" + 
						"} ";
		
				 RestAssured.baseURI ="https://eu4.smartcommunications.cloud/one/oauth1/bulkServices/api/v2";
				 Response res = given().log().all().
						 	header("Content-Type","application/json").
						 	auth().
						 	oauth("50c26977-f996-4d54-9492-d5026cba0ee4!appliance@cpaglobal.com.dev", "be6e1665-5e54-440e-b71d-7ef9421bf92e", "", "").
						 	contentType(ContentType.JSON).
						 	body(json).
							when().
							get("/jobs").
							then().assertThat().statusCode(200).log().all(). //cekiramo da li je status 201
							//log().all().
							//Uzimamo response
							extract().response();
				 
				    //Response izdvajam kao string i ispisujem ga u konzolu kao JSON
				 			System.out.println(res);
				 			String responseString = res.asString();
					
					//log.info(responseString +"Passed");
					
					XmlPath xmlPath = new XmlPath(responseString);
//					System.out.println(xmlPath);
					List<String> list = xmlPath.getList("Jobs.job.status");
					System.out.println(list.size());
					for(String str : list) {
						System.out.println(str);
					}
					Assert.assertTrue(xmlPath.getList("Jobs.job.status").contains("FINISHED"));
//					log.info(responseString +"Passed");
					//JSONObject xmlJSONObj = XML.toJSONObject(responseString);
		            //System.out.println(xmlJSONObj);
//				 System.out.println(res);
//				 JSONObject res1 = XML.toJSONObject(res);
//				 System.out.println(res1);
				 
			}
				}
			
//			@Test(priority = 3)
//				public void testResponce() {
//					Response resp = RestAssured.get("https://eu4.smartcommunications.cloud/one/oauth1/bulkServices/api/v2/jobs");
//					String respo = resp.asString();
//					int code = resp.getStatusCode();
//					System.out.println("Status code is " +code);
//					System.out.println(respo);
//				}
			}
	