package firstTest;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.tree.java.PsiLocalVariableImpl;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Created by alotfi on 10/1/2016.
 */
public class CreateSetterObject extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiClass sourcePsiClass = getSourcePsiClassFromContext(e);
        PsiClass targetPsiClass = getTargetPsiClassFromContext(e);
        String objectName = getObjectName(e);
        GenerateProperty generateProperty = new GenerateProperty(sourcePsiClass, targetPsiClass, objectName);
        generateProperty.generateComparable();
    }

    public void update(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
    }

    private PsiClass getSourcePsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        return psiClass;
    }

    private PsiClass getTargetPsiClassFromContext(AnActionEvent e) {
        PsiElement f = e.getData(LangDataKeys.PASTE_TARGET_PSI_ELEMENT);
        return ((PsiClassReferenceType) ((PsiLocalVariableImpl) f).getTypeElement().getType()).resolve();
    }

    private String getObjectName(AnActionEvent e) {
        PsiElement f = e.getData(LangDataKeys.PASTE_TARGET_PSI_ELEMENT);
        return ((PsiLocalVariableImpl) f).getName();
    }
}
