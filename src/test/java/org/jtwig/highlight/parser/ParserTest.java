package org.jtwig.highlight.parser;

import org.jtwig.highlight.format.EchoFormatter;
import org.jtwig.highlight.parser.context.ParserContextFactory;
import org.jtwig.highlight.parser.factory.TracingParserRunnerFactory;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.jtwig.highlight.config.HighlightConfigurationBuilder.highlightConfiguration;

public class ParserTest {

    @Test
    public void parseFor() throws Exception {
        HighlightParser highlightParser = HighlightParser.defaultParser();

        String result = highlightParser.parse("{% for var , test in test %}blah{% endfor %}");

        System.out.println(result);
    }

    @Test
    public void parseSet() throws Exception {
        HighlightParser highlightParser = HighlightParser.defaultParser();

        String result = highlightParser.parse("{% set a = 1 + 2 %}");

        System.out.println(result);
    }

    @Test
    public void parseMethodCalls() throws Exception {
        HighlightParser highlightParser = HighlightParser.defaultParser();

        String result = highlightParser.parse("{% do test.method() %}");

        System.out.println(result);
    }

    @Test
    public void parseMethodCallsWithArguments() throws Exception {
        HighlightParser highlightParser = HighlightParser.tracingParser();

        String result = highlightParser.parse("{% do test.method('hello') %}");

        System.out.println(result);
    }

    @Test
    public void parseMethodCallsWithMultipleArguments() throws Exception {
        HighlightParser highlightParser = HighlightParser.tracingParser();

        String result = highlightParser.parse("{% do test.method('hello', 1) %}");

        System.out.println(result);
    }

    @Test
    public void parseList() throws Exception {
        HighlightParser highlightParser = HighlightParser.tracingParser();

        String result = highlightParser.parse("{{ [1, 2, 3] }}");

        System.out.println(result);
    }

    @Test
    public void parseMap() throws Exception {
        HighlightParser highlightParser = HighlightParser.tracingParser();

        String result = highlightParser.parse("{{ {hello: 'one'} }}");

        System.out.println(result);
    }

    @Test
    public void parseMapString() throws Exception {
        HighlightParser highlightParser = HighlightParser.tracingParser();

        String result = highlightParser.parse("{{ {hello: 'one', 'two':'three'} }}");

        System.out.println(result);
    }

    @Test
    public void parseMapStringMultiple() throws Exception {
        HighlightParser highlightParser = echoParser();

        String result = highlightParser.parse("{{ {hello: 'one', 'two':'three', 'nana':eheh} }}");

        assertThat(result, is("{{ {hello: 'one', 'two':'three', 'nana':eheh} }}"));
    }

    @Test
    public void parseInclude() throws Exception {
        HighlightParser highlightParser = echoParser();

        String result = highlightParser.parse("{% include 'template.twig' ignore missing with {} only %}");

        assertThat(result, is("{% include 'template.twig' ignore missing with {} only %}"));
    }

    @Test
    public void parseMacro() throws Exception {
        HighlightParser highlightParser = echoParser();

        String result = highlightParser.parse("{% macro macroName (firstArgument, secondArgument, nArgument) %}");

        assertThat(result, is("{% macro macroName (firstArgument, secondArgument, nArgument) %}"));
    }

    @Test
    public void parseAccressExpression() throws Exception {
        HighlightParser highlightParser = echoParser();

        String result = highlightParser.parse("{% if (defined([1, 2][5])) %}KO{% else %}OK{% endif %}");

        assertThat(result, is("{% if (defined([1, 2][5])) %}KO{% else %}OK{% endif %}"));
    }

    @Test
    public void parseTranslate() throws Exception {
        HighlightParser highlightParser = echoParser();

        String result = highlightParser.parse("{% trans into 'pt' %}Hello world{% endtrans %}");

        assertThat(result, is("{% trans into 'pt' %}Hello world{% endtrans %}"));
    }

    @Test
    public void parseTranslateWith() throws Exception {
        HighlightParser highlightParser = echoParser();

        String result = highlightParser.parse("{% trans into 'pt' with {} %}Hello world{% endtrans %}");

        assertThat(result, is("{% trans into 'pt' with {} %}Hello world{% endtrans %}"));
    }

    private HighlightParser echoParser() {
        return new HighlightParser(new ParserContextFactory().create(highlightConfiguration()
                .withFormatter(new EchoFormatter())
                .build()),
                new TracingParserRunnerFactory());
    }
}