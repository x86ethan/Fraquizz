import extensions.File;

class Fraquizz extends Program {

    void algorithm () {

        Parser parser = new Parser();
        parser.filepath = "config/regions.csv";
        parser.file = loadCSV(parser.filepath);
        parser.rows = rowCount(parser.file);

        
    }

}