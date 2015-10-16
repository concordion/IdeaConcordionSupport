package org.concordion.plugin.idea.inspection;

import org.concordion.plugin.idea.ConcordionElementPattern;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.concordion.internal.SimpleEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.concordion.plugin.idea.ConcordionPatterns.concordionElement;
import static org.concordion.plugin.idea.ConcordionPsiUtils.setOf;

public class FullOgnlRequired extends LocalInspectionTool {

    private static final Set<String> SET_COMMANDS = setOf("set");
    private static final Set<String> VERIFY_ROW_COMMANDS = setOf("verifyRows", "verify-rows");
    private static final Set<String> EVALUATE_COMMANDS = setOf("assertEquals", "assert-equals",
            "assertTrue", "assert-true",
            "assertFalse", "assert-false",
            "echo",
            "execute");


    private static final ConcordionElementPattern.Capture<XmlAttributeValue> TOO_COMPLEX_CONCORDION_EXPRESSION =
            concordionElement(XmlAttributeValue.class)
                    .withParent(XmlAttribute.class)
                    .withConcordionHtmlSpec()
                    .withFoundTestFixture()
                    .withFullOgnl(false)
                    .withConcordionSchemaAttribute()
                    .andOr(
                            concordionElement(XmlAttributeValue.class)
                                    .withConcordionCommand(SET_COMMANDS)
                                    .with(new ComplicatedExpression(
                                            SimpleEvaluator::validateSetVariableExpression
                                    )),
                            concordionElement(XmlAttributeValue.class)
                                    .withConcordionCommand(EVALUATE_COMMANDS)
                                    .with(new ComplicatedExpression(
                                            SimpleEvaluator::validateEvaluationExpression
                                    )),
                            concordionElement(XmlAttributeValue.class)
                                    .withConcordionCommand(VERIFY_ROW_COMMANDS)
                                    .with(new ComplicatedExpression(
                                            SimpleEvaluator::validateEvaluationExpression,
                                            FullOgnlRequired::extractEvaluationExpressionFromVerifyRows
                                    ))
                    );

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new ConcordionInspectionVisitor<>(
                TOO_COMPLEX_CONCORDION_EXPRESSION,
                holder,
                "Complex expression with fixture not annotated with @FullOGNL"
        );
    }

    private static String extractEvaluationExpressionFromVerifyRows(String evaluateRowsExpression) {
        int delimiterPosition = evaluateRowsExpression.indexOf(':');

        if (delimiterPosition == -1) {
            return "";
        }

        return evaluateRowsExpression.substring(delimiterPosition + 1).trim();
    }

    public static final class ComplicatedExpression extends PatternCondition<XmlAttributeValue> {

        private final Consumer<String> validator;
        private final Function<String, String> transformer;

        public ComplicatedExpression(Consumer<String> validator) {
            this(validator, null);
        }

        public ComplicatedExpression(Consumer<String> validator, Function<String, String> transformer) {
            super("ComplicatedExpression");
            this.validator = validator;
            this.transformer = transformer;
        }

        @Override
        public boolean accepts(@NotNull XmlAttributeValue attributeValue, ProcessingContext context) {
            try {
                String expression = attributeValue.getValue();
                if (transformer != null) {
                    expression = transformer.apply(expression);
                }
                validator.accept(expression);
                return false;//simple
            } catch (RuntimeException e){
                return true;//complex
            }
        }
    }
}