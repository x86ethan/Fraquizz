import extensions.File;

// Parsing functions
/* Region parseRegion (int regionNumber) {
    Region region = new Region();
    region.regionNumber = regionNumber;
    region.regionName = getName(regionNumber);
    region.questions = getQuestions(regionNumber);
    region.guessed = false;
    return region;
}

Region[] parseRegions (int rows) {
    Region[] regions = new Region[rows - 1];
    for (int i = 1; i < rows; i++) {
        regions[i] = parseRegion(i);
    }
    return regions;
}


// Get region name from a number 
String getName(int regionNumber) {
    extensions.CSVFile configFile = ;
    return configFile.getCell(regionNumber + 1, 1);
}

// Get questions for a region number
String[] getQuestions(int regionNumber) {
    String[] questions = new String[3];
    extensions.CSVFile configFile = file;
    for (int i = 0; i < 3; i++) {
        questions[i] = configFile.getCell(regionNumber + 1, 2 + i);
    }
    return questions;
} */



class Fraquizz extends Program {

    Region[] parseCSV(String filename) {

        // Load the CSV file
        extensions.CSVFile csv = loadCSV(filename);
        int regionCount = rowCount(csv) - 1;

        // Allocate the ouptut array
        Region[] regions = new Region[regionCount];

        for (int i = 0; i < regionCount; i++) {
            regions[i] = new Region();
            regions[i].regionNumber = stringToInt(csv.getCell(i+1, 0));
            regions[i].regionName = csv.getCell(i+1, 1);
            regions[i].questions = new String[3];

            for (int j = 0; j < 3; j++) {
                regions[i].questions[j] = csv.getCell(i+1, 2+j);
            }
        }

        return regions;
    }

    void algorithm () {

        Region[] regions = parseCSV("config/regions.csv");


    }

}