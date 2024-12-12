import extensions.File;
import extensions.CSVFile;

class Parser {

    String filepath;
    extensions.CSVFile file;
    int rows;
 
    // Create a region class from the config file
    Region parseRegion (int regionNumber) {
        Region region = new Region();
        region.regionNumber = regionNumber;
        region.regionName = getName(regionNumber);
        region.questions = getQuestions(regionNumber);
        region.guessed = false;
        return region;
    }

    Region[] parseRegions () {
        Region[] regions = new Region[rows - 1];
        for (int i = 1; i < rows; i++) {
            regions[i] = parseRegion(i);
        }
        return regions;
    }
    

    // Get region name from a number 
    String getName(int regionNumber) {
        extensions.CSVFile configFile = file;
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
    }

}