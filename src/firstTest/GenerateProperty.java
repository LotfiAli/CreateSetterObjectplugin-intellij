package firstTest;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by alotfi on 10/2/2016.
 */
public class GenerateProperty {
    private final PsiClass sourcePsiClass;
    private final PsiClass targetPsiClass;
    private final List<PsiField> fields;
    private final String objectName;

    public GenerateProperty(PsiClass source, PsiClass target, String objectName) {
        this.sourcePsiClass = source;
        this.targetPsiClass = target;
        this.fields = Arrays.asList(targetPsiClass.getFields());
        this.objectName = objectName;
    }

    private void generate() {

    }

    public void generateComparable() {
        new WriteCommandAction.Simple(sourcePsiClass.getProject(), sourcePsiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                generateCode();
            }
        }.execute();
    }

    private void generateCode() {


        String targetName = targetPsiClass.getName();
        StringBuilder builder = new StringBuilder("public " + targetName + " create" + targetName + "(){\n");
//        builder.append(sourcePsiClass.getName()).append(" that) {\n");
        builder.append(targetName + " item=new " + targetName + "();");
        for (PsiField field : fields) {
            String fieldName = createPropertyName(field.getName());
            builder.append("item.set" + fieldName + "();\n");
        }
        builder.append("return item; \n}");
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(sourcePsiClass.getProject());
        PsiMethod compareTo = elementFactory.createMethodFromText(builder.toString(), sourcePsiClass);
        PsiElement method = sourcePsiClass.add(compareTo);
        JavaCodeStyleManager.getInstance(sourcePsiClass.getProject()).shortenClassReferences(method);
    }

    private String createPropertyName(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
    }
}
