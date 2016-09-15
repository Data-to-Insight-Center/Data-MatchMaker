package edu.indiana.d2i.matchmaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.indiana.d2i.matchmaker.drivers.MetaDriver;
import edu.indiana.d2i.matchmaker.drivers.Query;
import edu.indiana.d2i.matchmaker.util.MatchmakerENV;
import edu.indiana.d2i.matchmaker.util.PropertyReader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SampleExecutor {

    private static final Logger log = Logger.getLogger(MMRestService.class);

    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("ERROR: Invalid parameters..");
            return;
        }

        // initialize Matchmaker Environment
        PropertyReader propertyReader = PropertyReader.getInstance(args[0]);
        PropertyConfigurator.configure(propertyReader.getProperty("log4j.properties.path"));
        if (log.isDebugEnabled()) log.debug("Matchmaker started");
        MatchmakerENV env = new MatchmakerENV(propertyReader);

        String roString = new String(Files.readAllBytes(Paths.get(args[1])));

        log.info("[Matchmaker: RO Request] " + roString);
        try {
            MetaDriver md = new Query(env, roString, null);
            String result = md.getResults();
            // pretty print json result
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(result, Object.class);
            String prettyPrintedResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

            System.out.println("\n======================= Matchmaker Result =====================");
            System.out.println(prettyPrintedResult);
            System.out.println("===============================================================\n");
        } catch (Exception e) {
            log.error("[Matchmaker server: Processing Error]", e);
        }
    }

}
