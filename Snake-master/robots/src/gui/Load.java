package gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class Load {
    private	String fileMines = "mine.txt";
    private	String fileWall = "wall.txt";

    private String load(String fileName) {
        String content = "";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), Charset.forName("UTF-8")))) {
            String line = "";
            line = reader.readLine();
            content += line + "\n";

        }
        catch (IOException e) {
            e.getStackTrace();
        }
        return content;
    }

    private ArrayList<Mine> loadMines(String content) {
        ArrayList<Mine> mines = new ArrayList<>();
        if(content == "")
        {}
        else {
            //for(String mine: content.split(",")) {
            //mines.add(makeMine(mine));
            //}

            Arrays.stream(content.split(",")).forEach(p -> mines.add(makeMine(p)));

        }
        return mines;
    }

    private Mine makeMine(String raw) {
        String[] lines = raw.split(" ");
        String x = lines[0];
        String y = lines[1];
        if (lines.length != 2)
            return new Mine(0, 0);
        return new Mine(Double.parseDouble(x), Double.parseDouble(y));
    }

    public ArrayList<Mine> returnMines() {
        String text = load(fileMines);
        return loadMines(text);
    }

    private ArrayList<Wall> loadWalls(String content) {
        ArrayList<Wall> walls = new ArrayList<>();
        if(content == "")
        {}
        else {
            //for(String wall: content.split(","))
            //    walls.add(makeWall(wall));

            Arrays.stream(content.split(",")).forEach(p -> walls.add(makeWall(p)));
        }
        return walls;
    }


    public ArrayList<Wall> returnWall() {
        String text = load(fileWall);
        return loadWalls(text);
    }

    private Wall makeWall(String raw) {
        String[] lines = raw.split(" ");
        String x = lines[0];
        String y = lines[1];
        if (lines.length != 2)
            return new Wall(0, 0);
        return new Wall(Double.parseDouble(x), Double.parseDouble(y));
    }
}
