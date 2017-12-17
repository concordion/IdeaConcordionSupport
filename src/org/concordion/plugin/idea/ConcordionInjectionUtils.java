package org.concordion.plugin.idea;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFileBase;
import org.concordion.plugin.idea.lang.ConcordionFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ConcordionInjectionUtils {

    private ConcordionInjectionUtils() {
    }

    @Nullable
    public static PsiElement findElementInHostWithManyInjections(@NotNull PsiLanguageInjectionHost host, int offset) {
        final Ref<PsiElement> ref = Ref.create();
        InjectedLanguageManager.getInstance(host.getProject()).enumerate(host, (injected, places) -> {
            PsiLanguageInjectionHost.Shred shred = places.get(0);
            Segment segment = shred.getHostRangeMarker();
            if (segment != null && TextRange.create(segment).contains(offset)) {
                ref.set(injected.findElementAt(offset - shred.getRangeInsideHost().getStartOffset() - host.getTextRange().getStartOffset()));
            }
        });
        return ref.get();
    }

    @Nullable
    public static PsiFile getContainingFile(@NotNull PsiElement element) {
        PsiFile file = element.getContainingFile();
        return ConcordionFileType.INSTANCE.equals(file.getFileType())
                ? getTopLevelFile(element)
                : file;
    }

    @Nullable
    public static PsiFile getTopLevelFile(@NotNull PsiElement element) {
        PsiFile topLevel = InjectedLanguageManager.getInstance(element.getProject()).getTopLevelFile(element);
        if (topLevel == null) {
            return null;
        }
        if (topLevel.getParent() != null) {
            return topLevel;
        }
        VirtualFile original = getOriginalVirtualFile(topLevel.getViewProvider().getVirtualFile());
        if (original == null) {
            return null;
        }
        return PsiManager.getInstance(element.getProject()).findFile(original);
    }

    @Nullable
    private static VirtualFile getOriginalVirtualFile(@Nullable VirtualFile virtualFile) {
        VirtualFile current = virtualFile;
        while (current instanceof LightVirtualFileBase) {
            current = ((LightVirtualFileBase) current).getOriginalFile();
        }
        return current;
    }

    @Nullable
    public static PsiLanguageInjectionHost findInjectionHost(@Nullable PsiElement element) {
        if (element == null) {
            return null;
        }
        return InjectedLanguageManager.getInstance(element.getProject()).getInjectionHost(element);
    }
}
