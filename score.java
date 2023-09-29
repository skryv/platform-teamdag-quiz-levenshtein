///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.debatty:java-string-similarity:2.0.0
//DEPS info.picocli:picocli:4.2.0

import static java.lang.System.*;
import java.util.concurrent.Callable;

import info.debatty.java.stringsimilarity.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name="score")
public class score implements Callable<Integer> {

    enum QuestionType {worldometer, historypics}

    @Parameters(index="0", description="answer")
    private String answer;

    @CommandLine.Option(names={"-q"},
            description="question type/name, valid values: ${COMPLETION-CANDIDATES}",
            required=true)
    QuestionType qType;

    @CommandLine.Option(names={"-m", "--max-score"},
            description="max score for that question",
            defaultValue="10",
            required=true)
    int maxValue;

    public Integer call() throws Exception {
        NormalizedLevenshtein l = new NormalizedLevenshtein();
        String rightAnswer = switch (qType) {
            case worldometer -> "LEJAFCHGBKDI";
            case historypics -> "LFCKAJGBEHID";
        };
        System.out.printf("%.2f\n", (1 - l.distance(rightAnswer, answer.toUpperCase()))*maxValue);
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new score()).execute(args);
        System.exit(exitCode);
    }
}
