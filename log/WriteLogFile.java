package log;
import java.io.*;
import java.util.Date;
import java.sql.Timestamp;

public class WriteLogFile {

    private PrintWriter log;

    public WriteLogFile (String dir) throws IOException {

        log = new PrintWriter(new File(dir));

        writeHead();      

    }

    public void writeHead () {

        log.printf("%-30s%-20s%-60s%-10s%s","[timestamp]", "[action]","[details]","[vertex]"," [observations]");
        log.println();

    }

    public void write (String string1) {

        log.print(" " + string1 + " ");

    }

    public void write (String string1, String string2) {

        write(string1, string2, "");

    }

    public void write (String string1, String string2, String string3) {

        write(string1, string2, string3, "");   

    }

    public void write (String string1, String string2, String string3, String string4) {

        log.printf("%-30s%-20s%-60s%-10s%s","["+new Timestamp(new Date().getTime())+"]", string1, string2, string3, string4);

    }


    public void writef (String string1, String string2) {

        writef(string1, string2, "");

    }

    public void writef (String string1, String string2, String string3) {

        writef(string1, string2, string3, "");

    }

    public void writef (String string1, String string2, String string3, String string4) {

        log.printf("%-30s%-20s%-60s%-10s%s","["+new Timestamp(new Date().getTime())+"]", string1, string2, string3, string4);
        log.println();

    }

    public void writeln (String string1) {

        log.println(" " + string1);

    }

    public void writeln () {

        log.println();

    }

    public void close () {

        log.close();

    }

}