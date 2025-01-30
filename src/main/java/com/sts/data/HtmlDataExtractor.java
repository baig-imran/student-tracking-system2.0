package com.sts.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

 public class HtmlDataExtractor {

    public void html() {
        String url = "http://becbapatla.ac.in:8080/STUDENTINFO/StuInfofinal_R20.jsp?id=y20ait469";

        try {
            // Fetch and parse the HTML document
            Document doc = Jsoup.connect(url).get();

            // Extract table data
            Map<String, Map<String, Object>> subjectMarks = extractTableAndFontData(doc);
            System.out.println("\n--- Extracted   Data ---");
            subjectMarks.forEach((key, value) -> System.out.println(key + ":: " + value));

        } catch (IOException e) {
            System.err.println("Error fetching the URL: " + e.getMessage());
        }
    }

    /**
     * Extracts data from HTML tables.
     * @param doc Parsed HTML document.
     * @return A map containing subject codes and their respective details.
     */
    private static Map<String, Map<String, Object>> extractTableAndFontData(Document doc) {
        Elements rawTables = doc.select("table");

        Map<String, Map<String, String>> eachSubjectData = new LinkedHashMap<>();
        Map<String, List<String>> semesterWiseSubjects = new HashMap<>();
        Map<String, Map<String, Object>> semesterAbstractDetailsMap = new HashMap<>();
        

        // Iterate over all tables except the first one
        for (int tIndex = 1; tIndex < rawTables.size(); tIndex++) {
            Element table = rawTables.get(tIndex);
            Elements headers = table.select("thead th");
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cells = row.select("td");
                Map<String, String> subjectData = new HashMap<>();

                // Extract semester summary (last row) of each table/semester
                Map<String, Object> semesterDetails = new HashMap<>();
                if (i == rows.size() - 1) {
                    semesterDetails.put("SEE", cells.get(1).text());
                    semesterDetails.put("CIE", cells.get(2).text());
                    semesterDetails.put("Total Marks", cells.get(3).text());
                    semesterDetails.put("SGPA", cells.get(5).text());
                    semesterDetails.put("PDATE", cells.get(6).text());

                    String semesterKey = "semester" + tIndex;
                    semesterAbstractDetailsMap.put(semesterKey, semesterDetails); // Initialize the map entry
                    continue;
                }

                if (cells.isEmpty()) continue;

                // Extract subject code and title
                String subject = cells.get(0).text();  //first column
                String[] subjectParts = subject.split(" ", 2);

                String code = subjectParts[0];
                //System.out.println("********"+code);
                if (code.contains("/")) {
                    String[] codes = code.split("/");
                    subjectData.put("code", codes[0]);
                    subjectData.put("code2", codes[1]);
                    code=codes[0];
                } else {
                    subjectData.put("code", code);
                    
                }
                //System.out.println("########"+code);

                String title = subjectParts.length > 1 ? subjectParts[1] : "Unknown Title";
                subjectData.put("title", title);

                // Add header-specific data
                for (int j = 1; j < headers.size(); j++) {
                    String header = headers.get(j).text();
                    String cellValue = cells.get(j).text();
                    subjectData.put(header, cellValue);
                }

                eachSubjectData.put(code, subjectData);

                // Group subjects by semester
                char ch = code.charAt(code.length() - 3);
                int semesterNumber = ch - '0';
                String semesterKey = "semester" + semesterNumber;

                semesterWiseSubjects.putIfAbsent(semesterKey, new ArrayList<>());
                semesterWiseSubjects.get(semesterKey).add(code);
                
            }
        }

        // Ensure semesterDetailsMap has all required entries
//        semesterWiseSubjects.forEach((semester, subjectList) -> {
//            semesterAbstractDetailsMap.putIfAbsent(semester, new HashMap<>()); // Ensure the key exists
//            semesterAbstractDetailsMap.get(semester).put("subjects", subjectList); // Add subjects to the map
//        });

        // Log semester details
        semesterAbstractDetailsMap.forEach((key, value) -> 
		{ 
			semesterAbstractDetailsMap.get(key).put("subjects", semesterWiseSubjects.get(key));
			
			
		}
		);
        
        System.out.println("\n1--- Semester final/abstract Details --- [semesterDetailsMap]");
        semesterWiseSubjects.forEach((key, value) -> System.out.println(key + ": " + value));
        
        Map<String, Object> othersData = new HashMap<>();
        othersData.put("eachSubjectData", eachSubjectData);
        semesterAbstractDetailsMap.put("others", othersData);
        System.out.println("\n2--- 2222222222222 ---");
        
        semesterAbstractDetailsMap.forEach((key, value) -> System.out.println(key + ": " + value));
        
        
        // fonts related code 
        Elements fonts = doc.select("font");

        fonts.forEach(font -> {
            if (font.text().contains("Academic Year")) {
                System.out.println("Font with 'Academic Year': " + font.text());
            } else if (font.text().contains("Grand Total")) {
                System.out.println("Font with 'Grand Total': " + font.text());
            }
        });
        
        //////////////////////////////////////////////////////////////
     // Extract data from font elements
        fonts.forEach(font -> {
            String text = font.text();

            // Handle Academic Year and Attempt data
            if (text.contains("Academic Year")) {
                String[] parts = text.split("Semester:");
                String academicYearAndSemester = parts[0].replace("Academic Year:", "").trim();
                String[] semesterAndAttempt = parts[1].split("Attempt:");
                String semesterNumber = semesterAndAttempt[0].trim();
                String attempt = semesterAndAttempt[1].trim();

                String semesterKey = "semester" + semesterNumber;

                semesterAbstractDetailsMap.putIfAbsent(semesterKey, new HashMap<>()); // Ensure semester entry exists
                semesterAbstractDetailsMap.get(semesterKey).put("Academic Year", academicYearAndSemester);
                semesterAbstractDetailsMap.get(semesterKey).put("Attempt", attempt);
            }
            // Handle Grand Total, Percentage, and CGPA data
            else if (text.contains("Grand Total")) {
                String[] parts = text.split("Grand Total :");
                String[] details = parts[1].split("Percentage :");
                String grandTotal = details[0].trim();
                String[] percentageAndCgpa = details[1].split("CGPA :");
                String percentage = percentageAndCgpa[0].trim();
                String cgpa = percentageAndCgpa[1].trim();

                //Map<String, Object> othersData = semesterAbstractDetailsMap.computeIfAbsent("others", k -> new HashMap<>());
                othersData.put("Grand Total", grandTotal);
                othersData.put("Percentage", percentage);
                othersData.put("CGPA", cgpa);
            }
        });

        // Log the updated semesterAbstractDetailsMap for debugging
        System.out.println("\n--- Updated Semester Abstract Details ---");
        semesterAbstractDetailsMap.forEach((key, value) -> System.out.println(key + ": " + value));
        ///////////////////////////////////////////////////////////////
        
        return semesterAbstractDetailsMap;
    }


    /**
     * Extracts and logs font data from the HTML document.
     * @param doc Parsed HTML document.
     */
    private static void extractFontData(Document doc) {
        Elements fonts = doc.select("font");

        fonts.forEach(font -> {
            if (font.text().contains("Academic Year")) {
                System.out.println("Font with 'Academic Year': " + font.text());
            } else if (font.text().contains("Grand Total")) {
                System.out.println("Font with 'Grand Total': " + font.text());
            }
        });
    }
    
    
}
