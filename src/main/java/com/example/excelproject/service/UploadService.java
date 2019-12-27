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
	
	private final UploadUtil uploadUtil;

	public UploadService(UploadUtil uploadUtil) {
		this.uploadUtil = uploadUtil;
	}
	
	public void upload(MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		
		Path tempDir = Files.createTempDirectory("");
		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
		
		file.transferTo(tempFile);
		
		Workbook workbook = WorkbookFactory.create(tempFile);
		
		Sheet sheet = workbook.getSheetAt(0);
		 
		Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);
		Row headerRow = rowStreamSupplier.get().findFirst().get();
		
		List<String> headerCells = StreamSupport.stream(headerRow.spliterator(),false)
					.map(Cell::getStringCellValue)
					.collect(Collectors.toList());
		
		int colCount = headerCells.size();
		
		
		System.out.println(headerCells);
		
		
		rowStreamSupplier.get()
		.map(row -> {
			
			List<String> cellList = StreamSupport.stream(row.spliterator(),false)
					.map(Cell::getStringCellValue)
					.collect(Collectors.toList());
			
			return IntStream.range(0, colCount)
				.boxed()
				.collect(Collectors.toMap(
						index -> headerCells.get(index),
						index -> cellList.get(index)));
			
		})
		.collect(Collectors.toList());
		
	}
	
}
