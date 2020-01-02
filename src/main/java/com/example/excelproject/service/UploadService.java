package com.example.excelproject.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.excelproject.util.UploadUtil;

@Service
public class UploadService {
	
	ppublic Map<String, List<Map<String, String>>> upload(MultipartFile file) throws Exception {
		Path tempDir= Files.createTempDirectory("");
		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
		file.transferTo(tempFile);
		
		Workbook workbook = WorkbookFactory.create(tempFile);
		Stream<Sheet> sheets = StreamSupport.stream(workbook.spliterator(), false);
		Map<String, List<Map<String, String>>> rowStreampSupplier  = new HashMap<>();
		
		int leaves= workbook.getNumberOfSheets();
		System.out.println(leaves);
		sheets.forEach(sheet ->{ 
			Stream<Row> rows = StreamSupport.stream(sheet.spliterator(), false);
			List <List<String>> value = new ArrayList<>();
			rows.forEach(row ->{
				//obtenemos todas las celdas y se crea una lista para guardarlos 
				Stream<Cell> cells = StreamSupport.stream(row.spliterator(), false);
				List<String> rowValues = new ArrayList<>();
				cells.forEach(cell ->{
					//obtenemos todas las celdas y verificamos si esta vacia
					String valueCell = cell.toString();
					if (valueCell  != "") {
						rowValues.add(valueCell);
					}
				});
				if (!rowValues.isEmpty()) {
					
					value.add(rowValues);
				}
			});
			List<Map<String, String>> listRows = new ArrayList<>();
			List<String> headersCell = value.get(0);
			value.remove(0);
			for (List<String> sheetValue : value) {
				Map<String, String> mapCells = IntStream.range(0, headersCell.size())
						.boxed()
						.collect(Collectors
						.toMap(headersCell::get, sheetValue::get));
				listRows.add(mapCells);
			}
			rowStreampSupplier.put(sheet.getSheetName(), listRows);
	});
		return rowStreampSupplier;
		}
	
}
