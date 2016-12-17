// This is a generated file. Not intended for manual editing.
package org.concordion.plugin.idea.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.concordion.plugin.idea.lang.psi.ConcordionTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

public class ConcordionOgnlExpressionNextImpl extends ASTWrapperPsiElement implements ConcordionOgnlExpressionNext {

  public ConcordionOgnlExpressionNextImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ConcordionVisitor visitor) {
    visitor.visitOgnlExpressionNext(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ConcordionVisitor) accept((ConcordionVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ConcordionField getField() {
    return findChildByClass(ConcordionField.class);
  }

  @Override
  @NotNull
  public List<ConcordionIndex> getIndexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionIndex.class);
  }

  @Override
  @Nullable
  public ConcordionMethod getMethod() {
    return findChildByClass(ConcordionMethod.class);
  }

  @Override
  @NotNull
  public List<ConcordionOgnlExpressionNext> getOgnlExpressionNextList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ConcordionOgnlExpressionNext.class);
  }

}
