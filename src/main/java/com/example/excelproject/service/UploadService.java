package com.example.excelproject.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

	public void upload(MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		
		Path tempDir = Files.createTempDirectory("");
		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
		
		file.transferTo(tempFile);
		
		Workbook workbook = WorkbookFactory.create(tempFile);
		
		Sheet sheet = workbook.getSheetAt(0);
		 
		Stream <Row> rowStream = StreamSupport.stream(sheet.spliterator(), false);
		
		Row headerRow = rowStream.findFirst().get();
		
		List<String> headerCells = StreamSupport.stream(headerRow.spliterator(),false)
					.map(Cell::getStringCellValue)
					.collect(Collectors.toList());
		
		System.out.println(headerCells);
		
		
		rowStream.forEach(row -> {
			Stream<Cell> cellStream = StreamSupport.stream(row.spliterator(),false);
			
			List<String>cellVals =cellStream.map(cell->{
				String cellVal =cell.getStringCellValue();
				return cellVal;
			})
			.collect(Collectors.toList()); 
			
			System.out.println(cellVals);
		});
		
	}
	
}
