package backend.UnitTests;

import backend.CsvHandling.CsvReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {
    private String csvPath = "example_test_files\\short_csv.csv";
    String expectedList = new String("[main_folder;second_folder;third_folder;name;description, geo;cities;pl;warsaw;pl, geo;cities;rus;moscow;rus, geo;cities;uk;london;uk, stuff;not useful;weird;anything;who knows, stuff;useful;food;potato;fries?, stuff;useful;food;tomato;i like ketchup, stuff;useful;weird;foo;bar]");
    String expectedSortedString = new String("main_folder;second_folder;third_folder;name;description\n" +
            "geo;cities;pl;warsaw;pl\n" +
            "geo;cities;rus;moscow;rus\n" +
            "geo;cities;uk;london;uk\n" +
            "stuff;not useful;weird;anything;who knows\n" +
            "stuff;useful;food;potato;fries?\n" +
            "stuff;useful;food;tomato;i like ketchup\n" +
            "stuff;useful;weird;foo;bar\n");

    CsvReader csvReader;
    private BufferedReader reader;

    CsvReaderTest() throws IOException {
        this.csvReader = new CsvReader(csvPath);
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), StandardCharsets.UTF_8));

        csvReader.getSortedCsvReadyString(); //we need to run it to make getLineListTest() work
    }

    @org.junit.jupiter.api.Test
    void getSortedCsvReadyStringTest() throws IOException {
        assertEquals(expectedSortedString, csvReader.getSortedCsvReadyString(), "Either a broken input or broken string from list creation");
    }

    @org.junit.jupiter.api.Test
    void getLineListTest() throws IOException{
        assertEquals(expectedList, csvReader.getLineList().toString(), "Either a broken input or broken string from list creation");
    }


}
