
with open("ascii-art copy.txt", "r") as asciiArtFile:
    asciiArt = asciiArtFile.read();

with open("ascii-art.txt", "r") as blankAsciiArtFile:
    blankAsciiArt = blankAsciiArtFile.read()

coordsToInt = lambda coords : (coords[0] * 151 + coords[1]) - 1;

def supprimer_doublons(liste):
    resultat = []
    for element in liste:
        if element not in resultat:
            resultat.append(element)
    return resultat

def regionChecker(coords: tuple, depth = 0):
    if blankAsciiArt[coordsToInt(coords)] != " " or depth > 12:
        result = [];
    else:
        result = [coords];
        result += regionChecker((coords[0] + 1, coords[1]), depth + 1)
        result += regionChecker((coords[0] - 1, coords[1]), depth + 1)
        result += regionChecker((coords[0], coords[1] - 1), depth + 1)
        result += regionChecker((coords[0], coords[1] + 1), depth + 1)
    return result;

with open("rsc/new-regions.csv", "r") as csvFile: 
    csv = csvFile.readlines();

csvData = [];
for i in csv:
    csvData.append(i.split(";"));

outFile = "";

def normalizeCoords(coords: list):
    result = []
    for i in coords:
        result.append(coordsToInt(i))
    return result

def coordsConvert(coords: list):
    output = "{";
    for i in coords:
        output += str(coordsToInt(i)) + ",";
    output = output[:-1];
    output += "}";

    return output; 

def ParseCoords(inputString: str):
    coordsList =  inputString.split(",");
    for i in coordsList:
        coordsList[coordsList.index(i)] = int(i);
    return tuple(coordsList);

outfile = "";

for i in csvData[1:]:
    print("Looking for {} ({})".format(i[0], i[1]))
    
    splitted = asciiArt.split("\n")

    for l in range(len(splitted)):
        for c in range(len(splitted[l])):
            if splitted[l][c] == i[0][0] and splitted[l][c+1] == i[0][1]:
                coords = regionChecker((l, c))

    coords = supprimer_doublons(coords)
    i.insert(2, coordsConvert(coords))

    print(coordsConvert(coords))

    for c in range(len(asciiArt)):
        if c in normalizeCoords(coords):
            print("#", end="")
        else:
            print(asciiArt[c], end="")
    
    outfile += ";".join(i)


with open("rsc/regions.csv", "w") as csvFile:
    csvFile.write(outfile);


