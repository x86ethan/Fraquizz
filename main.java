import extensions.File;


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

    Region pickRegion(Region[] regions) {
        Region region;
        do {
            region = regions[(int) (random() * (length(regions)))];
        } while (region.guessed == true);
        return region;
    }

    boolean areThereAnyUnguessedRegions(Region[] regions) {
        boolean result = false;
        for (int i = 0; i < length(regions); i++) {
            if (regions[i].guessed == false) {
                result = true;
            }
        }
        return result;
    }


    void algorithm () {

        // Parsing de Regions
        Region[] regions = parseCSV("config/regions.csv");

        println("Bienvenue sur Fraquizz.");
        println("Tu vas devoir deviner tous les départements Français !");
        println("Pour cela, nous allons jouer aux devinettes !");
        println();

        println("Tu as 2 essais par réponse. Si tu échoues, tu ne gagnes aucun point, mais si tu réussis, tu gagnes un point.");

        println("-------------------------------------------------------");
        println("Appuye sur [Entrée] pour commencer à jouer.");

        readString();

        String diff;
        int difficulty = -1;
        do {
            println("Quelle difficulté veux-tu [facile/normal/difficile] ? ");
            diff = toLowerCase(readString());
        } while (!(equals(diff, "facile") || equals(diff, "normal") || equals(diff, "difficile")));

        // Can't use switch case ;(
        if (equals(diff, "difficile")) {
            difficulty = 0;
        } else if (equals(diff, "normal")) {
            difficulty = 1;
        } else if (equals(diff, "facile")) {
            difficulty = 2;
        }

        int score = 0;
        int count = 1;
        String input;
        Region region;
        String answer;

        do {
            println("Score: " + score);
            println("Département n°" + count);
            println();

            region = pickRegion(regions);

            // Ask the question
            println(region.getQuestion(difficulty));
            println("Tu peux aussi écrire STOP pour arrêter de jouer !");
            answer = readString();

            if (equals(answer, "STOP")) {
                println("Merci d'avoir joué !");
                break;
            }

            if (equals(toLowerCase(answer), toLowerCase(region.regionName))) {
                println("Bravo ! C'était bien le département " + region.regionName + ". Tu as gagné un point !");
                score++;
            } else {
                println("Dommage, c'était le département " + region.regionName + ". Tu n'as pas gagné de point ce coup-ci.");
            }

            println("\n\n\n\n");

        } while(areThereAnyUnguessedRegions(regions));

        println("Merci d'avoir joué ! Tu as gagné " + score + " points.");
        println("Nous enregistrons ton score pour la prochaine fois !");


    }

}