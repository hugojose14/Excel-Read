package com.example.excelproject.util;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

@Component
public class UploadUtil {

	public Supplier<Stream<Row>> getRowStreamSupplier(Sheet sheet) {
		// TODO Auto-generated method stub
		return null;
	}

	public Stream<Row> getStream(Row headerRow) {
		// TODO Auto-generated method stub
		return null;
	}

}
