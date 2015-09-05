// This is a generated file. Not intended for manual editing.
package com.gman.idea.plugin.concordion.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gman.idea.plugin.concordion.lang.psi.ConcordionTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

public class ConcordionArgumentsImpl extends ASTWrapperPsiElement implements ConcordionArguments {

  public ConcordionArgumentsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) ((ConcordionVisitor)visitor).visitArguments(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ConcordionOgnlExpression> getOgnlExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionOgnlExpression.class);
  }

}