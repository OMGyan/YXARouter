package com.yx.annotation_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.yx.annotations.BindPath;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Author by YX, Date on 2019/8/2.
 * 注解处理器(用来生成能装载activity的类文件)
 */
@AutoService(Processor.class) //注册注解处理器
public class AnnotationCompiler extends AbstractProcessor{

    //生成文件的对象
    Filer filer;
    private MethodSpec methodSpec;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    /**
     * 声明所有要处理的注解
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    /**
     * 声明当前注解处理器支持的JDK版本
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 注解处理器核心方法(用来写文件)
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //收集模块中所有用到BindPath注解的节点
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        //初始化数据
        Map<String,String> map = new HashMap<>();
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            //获取到装载activity容器的key
            String key = typeElement.getAnnotation(BindPath.class).value();
            String value = typeElement.getQualifiedName().toString();
            map.put(key,value);
        }
        //有了数据就开始写文件
        if(map.size()>0){
           //通过JavaPoet来写文件
           goWriteByJavaPoet(map);
           //传统方式
            /*  Writer writer = null;
            //创建文件类名
            String utilName = "ActivityUtil_"+System.currentTimeMillis();
            //创建文件
            try {
                JavaFileObject sourceFile = filer.createSourceFile("com.yx.util." + utilName);
                writer = sourceFile.openWriter();
                writer.write("package com.yx.util;\n" +
                        "\n" +
                        "import com.yx.arouterx.ARouter;\n" +
                        "import com.yx.arouterx.IRouter;\n" +
                        "public class "+utilName+" implements IRouter{\n" +
                        "    @Override\n" +
                        "    public void putActivity() {\n");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    String value = map.get(key);
                    writer.write("      ARouter.getInstance().putActivity(\""+key+"\","+value+".class);\n");
                }
                writer.write("    }\n}");
                // ARouter.getInstance().putActivity("login/login",LoginActivity.class);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(writer!=null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }
        return false;
    }
    private void goWriteByJavaPoet(Map<String,String> map) {
        //导包
        ClassName  ARouterClass = ClassName.bestGuess("com.yx.arouterx.ARouter");
        ClassName  IRouterInterface = ClassName.bestGuess("com.yx.arouterx.IRouter");
        //创建方法
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("putActivity")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(TypeName.VOID);
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = map.get(key);
            methodBuilder.addStatement("$T.getInstance().putActivity($S,$N.class)",ARouterClass,key,value);
        }
        //创建类
        String utilName = "ActivityUtil_"+System.currentTimeMillis();
        TypeSpec typeSpec = TypeSpec.classBuilder(utilName)
                .addSuperinterface(IRouterInterface)
                .addModifiers(PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        //开始写
        try {
            JavaFile.builder("com.yx.util",typeSpec).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
