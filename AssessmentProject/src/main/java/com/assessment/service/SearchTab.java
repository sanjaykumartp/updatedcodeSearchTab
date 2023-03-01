package com.assessment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

@Component
public class SearchTab {
	
    public String search(String directoryPath, String searchString) throws IOException {
        StringBuilder result = new StringBuilder();

        File directory = new File(directoryPath);
        //if the directory is not present it will print invalid directory path
        if (!directory.exists() || !directory.isDirectory()) {
            result.append("Invalid directory path: " + directoryPath);
            return result.toString();
        }
        File[] files = directory.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String name) {
                return name.toLowerCase().endsWith(".docx");
            }
        });
        result.append("Number of files present in the directory " + files.length + "<br>");
        if (files.length == 0) {
            result.append("No .docx files found in " + directoryPath);
            return result.toString();
        }
        if (!directory.isDirectory()) {
            result.append(directoryPath + " is not a directory.");
            return result.toString();
        }
        String[] searchWords = searchString.split(",");
        for (String searchWord : searchWords) {
            result.append("<br>" + "Results for Keyword: " + searchWord + "<br>");

            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".docx")) {
                    int count = searchFile(file, searchWord.trim());
                    if (count > 0) {
                        File subDir = new File(directoryPath + File.separator + searchWord);
                        if (!subDir.exists()) {
                            subDir.mkdirs();
                        }
                        result.append("File Name is : " + file.getName() + "    " + " Keyword : "
                                + searchWord.trim() + "    " + " No of Occurence : " + count + "    "
                                + " Directory : " + directoryPath + "<br>");
                        Path sourcePath = Paths.get(file.getAbsolutePath());
                        Path targetPath = Paths.get(subDir.getAbsolutePath() + File.separator + file.getName());
                        Files.copy(sourcePath, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        result.append(file.getName() + " document copied from the source path " + directoryPath
                                + " to the target path " + subDir + "<br>");
                    } else {
                        result.append(searchWord.trim() + " keyword is not present in word document "
                                + file.getName() + "<br>");
                    }
                }
            }
        }
        return result.toString();
    }

    private static int searchFile(File file, String searchString) throws IOException {
        int count = 0;
        try (FileInputStream fis = new FileInputStream(file); XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                if (paragraph.getText().toLowerCase().contains(searchString.toLowerCase())) {
                    count++;
                }
            }
        }
        return count;
    }
}

