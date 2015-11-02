package org.concordion.plugin.idea.autocomplete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static org.concordion.plugin.idea.ConcordionPsiUtils.*;
import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class ConcordionCommandsCompletionContributor extends CompletionContributor {

    public static final List<String> ALL_COMMANDS = unmodifiableList(asList(
            "assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "execute",
            "set",
            "echo",
            "verifyRows", "verify-rows",
            "matchStrategy", "match-strategy",
            "matchingRole", "matching-role",
            "run",
            "example",
            "status"
    ));

    public ConcordionCommandsCompletionContributor() {
        extend(
                CompletionType.BASIC,
                concordionElement().withParent(XmlAttribute.class).withConcordionHtmlSpec(),
                new ConcordionCommandCompletionProvider()
        );
    }

    private static final class ConcordionCommandCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {

            result.addAllElements(forCommands(
                    concordionSchemaPrefixOf(parameters.getOriginalFile()),
                    ALL_COMMANDS
            ));
        }
    }

    private static Iterable<LookupElement> forCommands(String prefix, Collection<String> commands) {
        return commands.stream()
                .map(c -> prefix + ':' + c)
                .map(c -> LookupElementBuilder.create(c).withInsertHandler(ConcordionCommandInsertionHandler.INSTANCE))
                .collect(toList());
    }
}
