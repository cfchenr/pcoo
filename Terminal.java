import pt.ua.concurrent.*;
import log.*;
import java.util.*;
import java.io.*;

public class Terminal extends Infrastructure {

    public Terminal(String name, int x, CCO cco) {

        super(name, x, cco);
        cco.aInfrastructure(this);

    }

}