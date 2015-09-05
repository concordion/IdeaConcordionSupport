package com.gman.idea.plugin.concordion.annotator;

import com.gman.idea.plugin.concordion.Concordion;
import com.gman.idea.plugin.concordion.ConcordionSetup;
import com.gman.idea.plugin.concordion.PsiUtils;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import static com.gman.idea.plugin.concordion.Concordion.*;
import static com.gman.idea.plugin.concordion.ConcordionGutterRenderer.rendererForRunnerClass;
import static com.gman.idea.plugin.concordion.ConcordionSetup.from;
import static com.gman.idea.plugin.concordion.annotator.ConcordionSetupAnnotator.annotateSetUpIssues;

public class ConcordionJavaRunnerAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (!(element instanceof PsiAnnotation)) {
            return;
        }
        PsiAnnotation annotation = (PsiAnnotation) element;
        if (!Concordion.JUNIT_RUN_WITH_ANNOTATION.equals(annotation.getQualifiedName())
                || !isRunWithAnnotationUsesConcordionRunner(annotation)) {
            return;
        }

        PsiClass javaRunner = PsiUtils.findParent(annotation, PsiClass.class);
        PsiFile htmlSpec = correspondingHtmlSpec(javaRunner);

        ConcordionSetup setup = from(javaRunner, htmlSpec);

        annotateSetUpIssues(element, holder, setup);

        if (htmlSpec != null) {
            holder
                    .createInfoAnnotation(element.getTextRange(), "")
                    .setGutterIconRenderer(rendererForRunnerClass(javaRunner, htmlSpec));
        }
    }
}