package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.Concordion;
import com.gman.idea.plugin.concordion.ConcordionGutterRenderer;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionGutterRenderer.rendererForHtmlSpec;

public class ConcordionHtmlSpecAnnotator implements Annotator {

    private static final String MISSING_RUNNER_CLASS_MESSAGE = "Missing runner class";
    private static final String RUNNER_CLASS_IS_NOT_ANNOTATED_MESSAGE = "Runner class is not annotated";
    private static final String CONCORDION_CONFIGURED_MESSAGE = "In concordion html spec";

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof XmlAttributeValue)) {
            return;
        }
        XmlAttributeValue value = (XmlAttributeValue) element;
        if (!Concordion.NAMESPACE.equalsIgnoreCase(value.getValue())) {
            return;
        }

        PsiFile htmlSpec = value.getContainingFile();
        PsiClass correspondingJavaRunner = correspondingJavaRunner(htmlSpec);
        if (correspondingJavaRunner == null) {
            Annotation errorAnnotation = holder.createErrorAnnotation(element, MISSING_RUNNER_CLASS_MESSAGE);
            errorAnnotation.setTooltip(MISSING_RUNNER_CLASS_MESSAGE);
            //TODO provide quick fix to the user
        } else if (!classAnnotatedWithConcordionRunner(correspondingJavaRunner)) {
            Annotation errorAnnotation = holder.createErrorAnnotation(element, RUNNER_CLASS_IS_NOT_ANNOTATED_MESSAGE);
            errorAnnotation.setTooltip(RUNNER_CLASS_IS_NOT_ANNOTATED_MESSAGE);
            //TODO provide quick fix to the user
        } else {
            Annotation infoAnnotation = holder.createInfoAnnotation(element.getTextRange(), CONCORDION_CONFIGURED_MESSAGE);
            infoAnnotation.setTooltip(CONCORDION_CONFIGURED_MESSAGE);
            infoAnnotation.setGutterIconRenderer(rendererForHtmlSpec(htmlSpec, correspondingJavaRunner));
        }
    }
}
