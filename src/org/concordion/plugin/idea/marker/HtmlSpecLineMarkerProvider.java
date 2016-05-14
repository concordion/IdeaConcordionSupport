package org.concordion.plugin.idea.marker;

import com.intellij.psi.PsiFile;

import static org.concordion.plugin.idea.ConcordionContextKeys.SPEC;
import static org.concordion.plugin.idea.ConcordionSpecType.HTML;
import static org.concordion.plugin.idea.patterns.ConcordionPatterns.concordionElement;

public class HtmlSpecLineMarkerProvider extends AbstractLineMarkerProvider {

    public HtmlSpecLineMarkerProvider() {
        super(
                concordionElement(PsiFile.class)
                        .withSpecOfType(HTML)
                        .withFoundTestFixture(),
                SPEC
        );
    }
}
