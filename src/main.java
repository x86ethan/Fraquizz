import extensions.File;

class Fraquizz extends Program {

    final String regionsFilename = "ressources/regions.csv";
    final String saveFilename = "ressources/save.csv";

    Game[] listSavedGames(String saveFile) {
        extensions.CSVFile csv = loadCSV(saveFile);

        Game[] savedGames = new Game[5];

        for (int i = 0; i < 5; i++) {
            if (!equals(csv.getCell(i, 0), "null")) {
                savedGames[i] = new Game();
                savedGames[i].playerName = csv.getCell(i, 0);
                savedGames[i].difficulty = stringToInt(csv.getCell(i, 1));
                savedGames[i].counter = stringToInt(csv.getCell(i, 2));
                savedGames[i].score = stringToInt(csv.getCell(i, 3));
                savedGames[i].regions = parseSavedRegionNumbers(csv.getCell(i, 4));
            }
        }

        return savedGames;
    }

    void saveGame(Game game, int index, String filename) {
        Game[] savedGames = listSavedGames(filename);
        savedGames[index] = game;

        String[][] content = new String[length(savedGames)][5];
        for (int i = 0; i < length(content); i++) {
            if (savedGames[i] != null) {
                content[i][0] = savedGames[i].playerName;
                content[i][1] = "" + savedGames[i].difficulty;
                content[i][2] = "" + savedGames[i].counter;
                content[i][3] = "" + savedGames[i].score;
                content[i][4] = getGuessedRegionNumbers(savedGames[i].regions);
            } else {
                content[i][0] = "null";
            }
        }
        saveCSV(content, filename);
    }

    String getGuessedRegionNumbers(Region[] regions) {
        String result = "{";
        for (int i = 0; i < length(regions); i++) {
            if (regions[i].guessed) {
                result += regions[i].regionNumber;
                result += ";";
            }
        }

        if (!equals(result, "{")) {
            result = substring(result, 0, length(result) - 2) + "}";
        } else {
            result = "{}";
        }
        return result;
    }

    Region[] parseSavedRegionNumbers(String rawInput) {
        int[] partialResult = parseRegionCoordinates(rawInput, ';');

        Region[] fullRegions = parseCSV(regionsFilename);
        
        // Validate regions that are already guessed
        for (int i = 0; i < length(fullRegions); i++) {
            if (inArray(partialResult, fullRegions[i].regionNumber)) {
                fullRegions[i].guessed = true;
            }
        }

        return fullRegions;

    }

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
        return parseRegionCoordinates(rawInput, ',');
    }

    int[] parseRegionCoordinates(String rawInput, char delimiter) {

        int[] result;
    
        if (length(rawInput) != 1 && length(rawInput) != 2) {
            String[] partialResult = new String[1024];
            int counter = 0;
            String nextNumber = "";

            // Remove brackets from raw input.
            rawInput = substring(rawInput, 1, length(rawInput) - 1);

            for (int i = 0; i < length(rawInput); i++) {
                if (charAt(rawInput, i) == delimiter) {
                    partialResult[counter] = nextNumber;
                    nextNumber = "";
                    counter++;
                } else {
                    nextNumber += charAt(rawInput, i);
                }
            }

            partialResult[counter] = nextNumber;
            nextNumber = "";
            counter++;

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

    void recoverFilledCharacters(int[] filledCharacters, Region[] regions) {
        for (int i = 0; i < length(regions); i++) {
            if (regions[i].guessed) {
                addToArray(filledCharacters, regions[i].characters);
            }
        }
    }

    void algorithm () {

        extensions.File asciiArtFile = newFile("ressources/ascii-art.txt");
        // Save the ASCII art in a string
        String asciiArt = "";
        while (ready(asciiArtFile)) {
            asciiArt += readLine(asciiArtFile) + "\n";
        }

        int[] filledCharacters = new int[10000];

        // Parsing de Regions
        // Pisses me to do that before the game loop, but I have to.
        Game game = new Game();
        // Region[] regions = parseCSV(regionsFilename);

        clearScreen();

        cursor(0, 0);

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

        int choice = 4;

        while (choice < 1 || choice >= 4) {
            clearScreen();
            cursor(18, 0);

            println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            println();
            println("  __  __                ___     _         _            _ ");
            println(" |  \\/  |___ _ _ _  _  | _ \\_ _(_)_ _  __(_)_ __  __ _| |");
            println(" | |\\/| / -_) ' \\ || | |  _/ '_| | ' \\/ _| | '_ \\/ _` | |");
            println(" |_|  |_\\___|_||_\\_,_| |_| |_| |_|_||_\\__|_| .__/\\__,_|_|");
            println("                                           |_|           ");

            println();

            printline("[1] Commencer une nouvelle partie"); delay(100);
            printline("[2] Charger une partie existante"); delay(100);
            printline("[3] Quitter"); delay(100);

            println("");
            print("-> ");
            choice = readInt();

            if (choice == 1) {
                
                game =  new Game();
                game.regions = parseCSV(regionsFilename);

                printnoreturn("Entrez votre nom : ");
                game.playerName = readString();

                game.score = 0;
                game.counter = 1;

                int diff;
                int difficulty = -1;
                do {

                    clearScreen();
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
                    game.difficulty = 0;
                } else if (diff == 2) {
                    game.difficulty = 1;
                } else if (diff == 1) {
                    game.difficulty = 2;
                }

                

            } else if (choice == 2) {
                clearScreen();
                cursor(18, 0);

                println(" ___          _   _          ___                                     _  __        ");
                println("| _ \\__ _ _ _| |_(_)___ ___ / __| __ _ _  ___ _____ __ _ __ _ _ _ __| |/_/ ___ ___");
                println("|  _/ _` | '_|  _| / -_|_-< \\__ \\/ _` | || \\ V / -_) _` / _` | '_/ _` / -_) -_|_-<");
                println("|_| \\__,_|_|  \\__|_\\___/__/ |___/\\__,_|\\_,_|\\_/\\___\\__, \\__,_|_| \\__,_\\___\\___/__/");
                println("                                                    |___/                         "); 
                println("==================================================================================");

                println();

                Game[] savedGames = listSavedGames(saveFilename);
 
                for (int i = 0; i < length(savedGames); i++) {
                    if (savedGames[i] != null) {
                        printline("[" + (i+1) + "] " + savedGames[i].playerName + " | Score : " + savedGames[i].score + " | Département : " + savedGames[i].counter + "/100");
                    } else {
                        printline("[" + (i+1) + "] Néant");
                    }
                }   
                printline("[6] Retour au menu principal."); 
                delay(200);

                println();
                printnoreturn("Entrez un numéro : ");

                int saveGameChoice;
                boolean exitMenu = false;

                do {
                    saveGameChoice = readInt();

                    if (saveGameChoice == 6) {
                        exitMenu = true;
                        choice = 4;
                    } else if (saveGameChoice < 1 || saveGameChoice > 5) {
                        printline(ANSI_RED + "Ce choix n'est pas disponible." + ANSI_RESET);
                        delay(500);
                        up();
                        clearLine();
                        up();
                        clearLine();
                        printnoreturn("Entrez un numéro : ");
                    } else if (savedGames[saveGameChoice - 1] == null) {
                        printline(ANSI_RED + "Cette sauvegarde est vide." + ANSI_RESET);
                        delay(500);
                        up();
                        clearLine();
                        up();
                        clearLine();
                        printnoreturn("Entrez un numéro : ");
                    } else {
                        game = savedGames[saveGameChoice - 1];
                        exitMenu = true;
                    }
                } while (!exitMenu);

            } 
        }
        

        



        int errorCount;
        String input;
        Region region;
        String answer;
        boolean stop = false;

        recoverFilledCharacters(filledCharacters, game.regions);

        String[] falsePropositions = new String[3];

        do {

            region = pickRegion(game.regions);
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
            println("[SCORE]       " + game.score);
            cursor(42, 140);
            print("[VIE] ");
            for (int i = 0; i < 2 - errorCount; i++) {
                print(ANSI_RED + " ♡" + ANSI_RESET);
            } for (int i = 0; i < errorCount; i++) {
                print("♡");
            }

            cursor(43, 0);
            println("[DEPARTEMENT] " + game.counter + "/100");
            println();


            generateFalsePropositions(falsePropositions, game.regions, region);
            
            // Ask the question
            printline(region.getQuestion(game.difficulty));
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

                if (equals(normalize(answer), "stop")) {
                    stop = true;
                    break;
                }

                if (equals(normalize(answer), normalize(region.regionName)) || ((charAt(answer, 0) >= '1' && charAt(answer, 0) <= '3') && falsePropositions[stringToInt(answer) - 1] == region.regionName)) {
                    region.guessed = true;
                } else {  
                    errorCount++;

                    cursor(42, 140);
                    print("[VIE] ");
                    for (int i = 0; i < 2 - errorCount; i++) {
                        print(ANSI_RED + " ♡" + ANSI_RESET);
                    } for (int i = 0; i < errorCount; i++) {
                        print(" ♡");
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
                game.score++;

                printnoreturn("Score : ");
                print(game.score - 1);
                delay(300);
                if (game.score < 10) {
                    backward();
                } else {
                    backward(2);
                }
                print(ANSI_RED + game.score + ANSI_RESET);

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

            } else if (!stop) {

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

            game.counter++;
            delay(2000);

        } while(areThereAnyUnguessedRegions(game.regions) && !stop);

        clearScreen();
        cursor(19, 0);

        println("  ___ _           _          _          ");
        println(" | __(_)_ _    __| |_  _    (_)___ _  _ ");
        println(" | _|| | ' \\  / _` | || |   | / -_) || |");
        println(" |_| |_|_||_| \\__,_|\\_,_|  _/ \\___|\\_,_|");
        println("                          |__/          ");

        println();

        printline("Tu as fait un score de " + ANSI_RED + game.score + ANSI_RESET + " sur 100 !");
        println();

        printline("[1] " + ANSI_GREEN + "Sauvegarder la partie " + ANSI_RESET + "et quitter");
        printline("[2] Quitter " + ANSI_RED + "sans sauvegarder " + ANSI_RESET + "la partie");
        println();

        printnoreturn("-> ");
        choice = readInt();

        while (choice != 1 && choice != 2) {
            printline(ANSI_RED + "Ce choix n'est pas disponible." + ANSI_RESET);
            delay(500);
            up();
            clearLine();
            up();
            clearLine();
            printnoreturn("-> ");
            choice = readInt();
        }

        if (choice == 1) {

            boolean saved = false;

            while (!saved) {

                clearScreen();
                cursor(18, 0);
                println("  ___                                     _           _                       _   _     ");
                println(" / __| __ _ _  ___ _____ __ _ __ _ _ _ __| |___ _ _  | |__ _   _ __  __ _ _ _| |_(_)___ ");
                println(" \\__ \\/ _` | || \\ V / -_) _` / _` | '_/ _` / -_) '_| | / _` | | '_ \\/ _` | '_|  _| / -_)");
                println(" |___/\\__,_|\\_,_|\\_/\\___\\__, \\__,_|_| \\__,_\\___|_|   |_\\__,_| | .__/\\__,_|_|  \\__|_\\___|");
                println("                        |___/                                 |_|                       ");
                println();

                Game[] savedGames = listSavedGames(saveFilename);

                printline("Dans quel emplacement veux-tu sauvegarder ta partie ?");
                for (int i = 0; i < length(savedGames); i++) {
                    if (savedGames[i] != null) {
                        printline("[" + (i+1) + "] " + savedGames[i].playerName + " | Score : " + savedGames[i].score + " | Département : " + savedGames[i].counter + "/100");
                    } else {
                        printline("[" + (i+1) + "] Néant");
                    }
                }

                println();
                printnoreturn("-> ");

                int saveGameChoice = readInt();

                while (saveGameChoice < 1 || saveGameChoice > 5) {
                    printline(ANSI_RED + "Ce choix n'est pas disponible." + ANSI_RESET);
                    delay(500);
                    up();
                    clearLine();
                    up();
                    clearLine();
                    printnoreturn("-> ");
                    saveGameChoice = readInt();
                }

                if (savedGames[saveGameChoice - 1] != null) {
                    printline("Cet emplacement est déjà utilisé. Veux-tu vraiment sauvegarder ici ?");
                    printline("[1] Oui");
                    printline("[2] Non");
                    println();
                    printnoreturn("-> ");
                    int decision = readInt();

                    while (decision != 1 && decision != 2) {
                        printline(ANSI_RED + "Ce choix n'est pas disponible." + ANSI_RESET);
                        delay(500);
                        up();
                        clearLine();
                        up();
                        clearLine();
                        printnoreturn("-> ");
                        decision = readInt();
                    }

                    if (decision == 1) {
                        saveGame(game, saveGameChoice - 1, saveFilename);
                        saved = true;
                    }
                } else {
                    saveGame(game, saveGameChoice - 1, saveFilename);
                    saved = true;
                }
            }
        }

        else {
            clearScreen();
            cursor(21, 0);
            println("  ___ _           _          _          ");
            println(" | __(_)_ _    __| |_  _    (_)___ _  _ ");
            println(" | _|| | ' \\\\  / _` | || |   | / -_) || |");
            println(" |_| |_|_||_| \\__,_|\\_,_|  _/ \\___|\\_,_|");
            println("                          |__/          ");
            println();
            printline("Merci d'avoir joué à Fraquizz !");
            delay(1000);
        }


    }

}