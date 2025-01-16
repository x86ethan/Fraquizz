import extensions.File;

class Fraquizz extends Program {

    String normalize(String input) {
        final char[][] replacements = new char[][]{
            {'ê', 'e'}, {'â', 'a'}, {'é', 'e'}, {'è', 'e'}, {'à', 'à'}, {'û', 'u'},
            {'ç', 'c'}, {'ù', 'ù'}, {'ô', 'o'}, {'-', ' '}
        };
        String output = "";
        boolean found;
        for (int i = 0; i < length(input); i++) {
            found = false;
            for (int j = 0; j < length(replacements); j++) {
                if (replacements[j][0] == charAt(toLowerCase(input), i)) {
                    found = true;
                    output += replacements[j][1];
                }
            }
            if (!found) {
                output += charAt(toLowerCase(input), i);
            }
        }

        return output;
    }

    int[] parseRegionCoordinates(String rawInput) {

        int[] result;
    
        if (length(rawInput) != 1) {
            String[] partialResult = new String[1024];
            int counter = 0;
            String nextNumber = "";

            // Remove brackets from raw input.
            rawInput = substring(rawInput, 1, length(rawInput) - 2);

            for (int i = 0; i < length(rawInput); i++) {
                if (charAt(rawInput, i) == ',') {
                    partialResult[counter] = nextNumber;
                    nextNumber = "";
                    counter++;
                } else {
                    nextNumber += charAt(rawInput, i);
                }
            }

            result = new int[counter];

            for (int i = 0; i < counter; i++) {
                result[i] = stringToInt(partialResult[i]);
            }
        } else {
            result = new int[0];
        }

        return result;

    }

    Region[] parseCSV(String filename) {

        // Load the CSV file
        extensions.CSVFile csv = loadCSV(filename, ';');
        int regionCount = rowCount(csv) - 1;

        // Allocate the ouptut array
        Region[] regions = new Region[regionCount];

        for (int i = 0; i < regionCount; i++) {
            regions[i] = new Region();
            regions[i].regionNumber = stringToInt(csv.getCell(i+1, 0));
            regions[i].regionName = csv.getCell(i+1, 1);
            regions[i].stateName = csv.getCell(i+1, 2);
            regions[i].capitalCity = csv.getCell(i+1, 3);
            regions[i].characters = parseRegionCoordinates(csv.getCell(i+1, 4));

            regions[i].questions = new String[3];
            for (int j = 0; j < 3; j++) {
                regions[i].questions[j] = csv.getCell(i+1, 5+j);
            }
        }

        return regions;
    }

    void removeRegion(Region[] regions, Region region) {
        for (int i = 0; i < length(regions); i++) {
            if (region == regions[i]) {
                regions[i] = null;
            }
        }
    }

    Region getRandomRegion(Region[] regions) {
        Region region;
        do {
            region = regions[(int) (random() * length(regions))];
        } while (region == null || region.guessed == true);

        return region;
    }

    Region pickRegion(Region[] regions) {
        Region region = getRandomRegion(regions);
        return region;
    }

    boolean areThereAnyUnguessedRegions(Region[] regions) {
        boolean result = false;
        for (int i = 0; i < length(regions); i++) {
            if (regions[i] != null && regions[i].guessed == false) {
                result = true;
            }
        }
        return result;
    }

    // Check if a value is in an array
    boolean inArray(int[] array, int value) {
        boolean result = false;
        for (int i = 0; i < length(array); i++) {
            if (array[i] == value) {
                result = true;
            }
        }
        return result;
    }

    // Get the minimum value of an array
    int getMinValue(int[] array) {
        int min;
        if (length(array) > 0) {
            min = array[0];
            for (int i = 1; i < length(array); i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
        } else {
            min = 0;
        }
        return min;
    }

    // Add an array to another array
    void addToArray(int[] array, int[] values) {
        int realArrayLength = 0;
        for (int i = 0; i < length(array); i++) {
            if (array[i] != 0) {
                realArrayLength++;
            }
        }

        for (int i = 0; i < length(values); i++) {
            array[realArrayLength + i] = values[i];
        }
    }

    void printnoreturn(String input) {
        for (int i = 0; i < length(input); i++) {
            print(charAt(input, i));
            delay(10);
        }
        delay(100);
    }

    void printline(String input) {
        printnoreturn(input);
        println();
    }

    void printline() {
        println();
    }

    void generateFalsePropositions(String[] falsePropositions, Region[] regions, Region actualRegion) {
        int randomPos = (int) (random() * 3);
        falsePropositions[randomPos] = actualRegion.regionName;
        for (int i = 0; i < 3; i++) {
            if (i != randomPos) {
                falsePropositions[i] = getRandomRegion(regions).regionName;
            }
        }
    }

    void algorithm () {

        extensions.File asciiArtFile = newFile("rsc/ascii-art.txt");
        // Save the ASCII art in a string
        String asciiArt = "";
        while (ready(asciiArtFile)) {
            asciiArt += readLine(asciiArtFile) + "\n";
        }

        int[] filledCharacters = new int[10000];

        // Parsing de Regions
        Region[] regions = parseCSV("rsc/regions.csv");

        clearScreen();

        text("green");                                                                                                
                                                                                                                                         
        println("FFFFFFFFFFFFFFFFFFFFFF                                                                           iiii                                    ");
        println("F::::::::::::::::::::F                                                                          i::::i                                   ");
        println("F::::::::::::::::::::F                                                                           iiii                                    ");
        println("FF::::::FFFFFFFFF::::F                                                                                                                   ");
        println("F:::::F       FFFFFFrrrrr   rrrrrrrrr   aaaaaaaaaaaaa     qqqqqqqqq   qqqqquuuuuu    uuuuuu  iiiiiii zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        println("F:::::F             r::::rrr:::::::::r  a::::::::::::a   q:::::::::qqq::::qu::::u    u::::u  i:::::i z:::::::::::::::zz:::::::::::::::z");
        println("F::::::FFFFFFFFFF   r:::::::::::::::::r aaaaaaaaa:::::a q:::::::::::::::::qu::::u    u::::u   i::::i z::::::::::::::z z::::::::::::::z ");
        println("F:::::::::::::::F   rr::::::rrrrr::::::r         a::::aq::::::qqqqq::::::qqu::::u    u::::u   i::::i zzzzzzzz::::::z  zzzzzzzz::::::z  ");
        println("F:::::::::::::::F    r:::::r     r:::::r  aaaaaaa:::::aq:::::q     q:::::q u::::u    u::::u   i::::i       z::::::z         z::::::z   ");
        println("F::::::FFFFFFFFFF    r:::::r     rrrrrrraa::::::::::::aq:::::q     q:::::q u::::u    u::::u   i::::i      z::::::z         z::::::z    ");
        println("F:::::F              r:::::r           a::::aaaa::::::aq:::::q     q:::::q u::::u    u::::u   i::::i     z::::::z         z::::::z     ");
        println("F:::::F              r:::::r          a::::a    a:::::aq::::::q    q:::::q u:::::uuuu:::::u   i::::i    z::::::z         z::::::z      ");
        println("FF:::::::FF            r:::::r          a::::a    a:::::aq:::::::qqqqq:::::q u:::::::::::::::uui::::::i  z::::::zzzzzzzz  z::::::zzzzzzzz");
        println("F::::::::FF            r:::::r          a:::::aaaa::::::a q::::::::::::::::q  u:::::::::::::::ui::::::i z::::::::::::::z z::::::::::::::z");
        println("F::::::::FF            r:::::r           a::::::::::aa:::a qq::::::::::::::q   uu::::::::uu:::ui::::::iz:::::::::::::::zz:::::::::::::::z");
        println("FFFFFFFFFFF            rrrrrrr            aaaaaaaaaa  aaaa   qqqqqqqq::::::q     uuuuuuuu  uuuuiiiiiiiizzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        println("                                                                    q:::::q                                                             ");
        println("=================================================================   q:::::q   ==========================================================");
        println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::   q:::::::q  :::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        println("=================================================================   q:::::::q  ========================================================= ");
        println("                                                                    q:::::::q                                                            ");
        println("                                                                    qqqqqqqqq                                                            ");
                                                                                                                                         

        text("black");

        printline("Bienvenue sur" + ANSI_RED + " Fraquizz " + ANSI_RESET + "!");
        println();

        printline("Le but ici est de deviner tous les départements Français !");
        printline("Je vais te poser des questions de culture générale, qui t'aideront normalement à retrouver le département correspondant.");

        delay(1000);

        println();

        printline("Tu as" + ANSI_RED + " 3 essais " + ANSI_RESET + "par réponse. Si tu échoues, tu ne gagnes aucun point, mais si tu réussis, tu gagnes un point.");

        delay(1000);

        println("------------------------------------------------------------------------------------------------------------------------------------------------------");
        printline("Appuye sur [⏎ Entrée] pour commencer à jouer.");

        readString();

        clearScreen();
        

        int diff;
        int difficulty = -1;
        do {

            cursor(20, 0);

            println("  ____    _    __    __   _                  _   _       __ ");
            println(" |  _ \\  (_)  / _|  / _| (_)   ___   _   _  | | | |_    /_/ ");
            println(" | | | | | | | |_  | |_  | |  / __| | | | | | | | __|  / _ \\");
            println(" | |_| | | | |  _| |  _| | | | (__  | |_| | | | | |_  |  __/");
            println(" |____/  |_| |_|   |_|   |_|  \\___|  \\__,_| |_|  \\__| \\___|");

            println();

            printline("Quelle difficulté souhaîtes-tu ?");
            println();
            delay(1000);

            printline("1. Facile");
            delay(500);
            printline("2. Normal");
            delay(500);
            printline("3. Difficile");

            println();
            println();

            delay(1500);
            cursor(50, 0);
            printnoreturn("Entre ta réponse [1/2/3] : ");

            diff = readInt();
        } while (diff != 1 && diff != 2 && diff != 3);

        // Can't use switch case ;(
        if (diff == 3) {
            difficulty = 0;
        } else if (diff == 2) {
            difficulty = 1;
        } else if (diff == 1) {
            difficulty = 2;
        }

        clearScreen();

        printnoreturn("Départ dans 3...");
        delay(1000);
        printnoreturn("2...");
        delay(1000);
        printnoreturn("1...");
        delay(1000);

        int score = 0;
        int count = 1;
        int errorCount;
        String input;
        Region region;
        String answer;

        String[] falsePropositions = new String[3];

        do {

            region = pickRegion(regions);
            errorCount = 0;

            clearScreen();
            cursor(0, 0);

            println("======================================================================= FRAQUIZZ =======================================================================");
            

            // Print the ASCII art, but replace the characters index in the filledCharacters array with "#"
            if (getMinValue(region.characters) < 5700) {
                for (int i = 0; i < length(asciiArt) / 2; i++) {
                    if (inArray(filledCharacters, i)) {
                        print("#");
                    } else {
                        print(charAt(asciiArt, i));
                    }
                }
            } else {
                for (int i = length(asciiArt) / 2; i < length(asciiArt); i++) {
                    if (inArray(filledCharacters, i)) {
                        print("#");
                    } else {
                        print(charAt(asciiArt, i));
                    }
                }
            }

        
            println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            println("[SCORE]       " + score);
            cursor(42, 140);
            print("[VIE] ");
            for (int i = 0; i < 2 - errorCount; i++) {
                print(ANSI_RED + " ♡" + ANSI_RESET);
            } for (int i = 0; i < errorCount; i++) {
                print("♡");
            }

            cursor(43, 0);
            println("[DEPARTEMENT] " + count + "/100");
            println();


            generateFalsePropositions(falsePropositions, regions, region);
            
            // Ask the question
            printline(region.getQuestion(difficulty));
            printline(ANSI_CYAN + "[STOP] pour arrêter le jeu." + ANSI_RESET);
            println();

            while (errorCount < 2 && !region.guessed) {
                println();
                for (int i = 0; i < 3; i++) {
                    print(ANSI_CYAN + (i+1) + ANSI_RESET);
                    printnoreturn(". " + falsePropositions[i]);
                    delay(500);
                    if (i < 2) {
                        print(" | ");
                    }
                }
                println();
                println("------------------------------------------------------------------------------------------------------------------------------------------------------");
                print("|-> ");

                answer = readString();

                if (equals(answer, "STOP")) {
                    printline("Merci d'avoir joué !");
                    break;
                }

                if (equals(normalize(answer), normalize(region.regionName))) {
                    region.guessed = true;
                } else {  
                    errorCount++;

                    cursor(42, 140);
                    print("[VIE] ");
                    for (int i = 0; i < 2 - errorCount; i++) {
                        print(ANSI_RED + " ♡" + ANSI_RESET);
                    } for (int i = 0; i < errorCount; i++) {
                        print("♡");
                    }

                    cursor(47, 0);
                    printline("Préfecture : " + ANSI_GREEN + region.capitalCity + ANSI_RESET + " | Région : " + ANSI_GREEN + region.stateName + ANSI_RESET);

                    for (int i = 0; i < 4; i++) {
                        clearLine();
                        println();
                    } 

                    up(4);
                
                }
            };

            if (region.guessed) {

                cursor(42, 0);

                for (int i = 0; i < 12; i++) {
                    clearLine();
                    cursor(42 + i, 0);
                }

                cursor(42, 0);

                print(ANSI_GREEN);

                println("  ___                        _ ");
                println(" | _ ) _ _  __ _ __ __ ___  | |");
                println(" | _ \\| '_|/ _` |\\ V // _ \\ |_|");
                println(" |___/|_|  \\__,_| \\_/ \\___/ (_)");
                println();

                print(ANSI_RESET);

                
                            
                printline("Bravo ! C'était bien le département " + ANSI_RED + region.regionName + ANSI_RESET + " !");
                delay(200);
                score++;

                printnoreturn("Score : ");
                print(score - 1);
                delay(300);
                if (score < 10) {
                    backward();
                } else {
                    backward(2);
                }
                print(ANSI_RED + score + ANSI_RESET);

                addToArray(filledCharacters, region.characters);
                cursor(2, 0);
                
                if (getMinValue(region.characters) < 5700) {
                    for (int i = 0; i < length(asciiArt) / 2; i++) {
                        if (inArray(filledCharacters, i)) {
                            print("#");
                        } else {
                            print(charAt(asciiArt, i));
                        }
                    }
                } else {
                    for (int i = length(asciiArt) / 2; i < length(asciiArt); i++) {
                        if (inArray(filledCharacters, i)) {
                            print("#");
                        } else {
                            print(charAt(asciiArt, i));
                        }
                    }
                }

            } else {

                cursor(42, 0);

                for (int i = 0; i < 12; i++) {
                    clearLine();
                    cursor(42 + i, 0);
                }

                cursor(42, 0);

                print(ANSI_RED);

                println("  ___ _                                  __   _  ");
                println(" | _ |_)___ _ _    ___ ______ __ _ _  _ /_/  | | ");
                println(" | _ \\ / -_) ' \\  / -_|_-<_-</ _` | || / -_) |_| ");
                println(" |___/_\\___|_||_| \\___/__/__/\\__,_|\\_, \\___| (_) ");
                println("                                   |__/          ");

                print(ANSI_RESET);
          
                printline("Et non, c'était en fait le département " + ANSI_RED + region.regionName + ANSI_RESET + " !");
                printline("Tu feras mieux la prochaine fois !");
               
            }

            count++;
            delay(2000);

        } while(areThereAnyUnguessedRegions(regions));

        printline("Merci d'avoir joué ! Tu as gagné " + score + " points.");


    }

}