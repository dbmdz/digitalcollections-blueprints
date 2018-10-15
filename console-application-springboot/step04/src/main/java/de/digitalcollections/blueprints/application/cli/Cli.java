package de.digitalcollections.blueprints.application.cli;

import java.io.PrintWriter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {

  private final PrintWriter out;

  private int exitStatus = -1;

  private String springProfiles = null;

  public Cli(PrintWriter out, String... args) throws ParseException, CliException {
    this.out = out;
    Options options = new Options();
    options.addOption("h", "help", false, "Show help");
    options.addOption("p", "spring.profiles.active", true, "The active spring profile");

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    if (cmd.hasOption("help")) {
      showHelp(options);
      exitStatus = ExitStatus.OK;
      return;
    }

    if (cmd.hasOption("spring.profiles.active")) {
      springProfiles = cmd.getOptionValue("spring.profiles.active");
    }
    
    if (cmd.getArgList().isEmpty() || cmd.getArgList().size() != 2) {
      showHelp(options);
      exitStatus = ExitStatus.OK;
      return;
    }

  }

  public boolean hasExitStatus() {
    return exitStatus > -1;
  }

  public int getExitStatus() {
    return exitStatus;
  }

  private void showHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(out, 120, "java -jar transform-xml.jar <xml file> <xsl file>", null, options, 2, 8, null, true);
    out.flush();
  }

  public boolean hasSpringProfiles() {
    return springProfiles != null;
  }

  public String getSpringProfiles() {
    return springProfiles;
  }

}
