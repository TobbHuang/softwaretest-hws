package common;

import model.CodeSegment;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.*;

/**
 * Created by huangtao on 2017/11/13.
 */
public class Utils {

    public static boolean compile(List<File> files, String outPath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        Iterable<String> options = Arrays.asList("-d", outPath);
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
        return compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
    }

    public static Result runJUnitTestcase(Class clazz) {
        Result result = JUnitCore.runClasses(clazz);
        return result;
    }

    public static void copy(File file, File toFile) throws Exception {
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis;
        FileOutputStream fos;
        if (file.isDirectory()) {
            String filepath = file.getAbsolutePath();
            filepath = filepath.replaceAll("\\\\", "/");
            String toFilepath = toFile.getAbsolutePath();
            toFilepath = toFilepath.replaceAll("\\\\", "/");
//            int lastIndexOf = filepath.lastIndexOf("/");
//            toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());
            File copy = new File(toFilepath);
            //复制文件夹
            if (!copy.exists()) {
                copy.mkdir();
            }
            //遍历文件夹
            for (File f : file.listFiles()) {
                copy(f, copy);
            }
        } else {
            if (toFile.isDirectory()) {
                String filepath = file.getAbsolutePath();
                filepath = filepath.replaceAll("\\\\", "/");
                String toFilepath = toFile.getAbsolutePath();
                toFilepath = toFilepath.replaceAll("\\\\", "/");
                int lastIndexOf = filepath.lastIndexOf("/");
                toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());

                //写文件
                File newFile = new File(toFilepath);
                fis = new FileInputStream(file);
                fos = new FileOutputStream(newFile);
                while ((a = fis.read(b)) != -1) {
                    fos.write(b, 0, a);
                }
            } else {
                //写文件
                fis = new FileInputStream(file);
                fos = new FileOutputStream(toFile);
                while ((a = fis.read(b)) != -1) {
                    fos.write(b, 0, a);
                }
            }

        }
    }

    public static void checkDirExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static Map<String, String> searchAllClassName(String programName){
        //TODO 现在没考虑源代码有多级目录，以及现在只认为一个文件内只有一个类
        Map<String, String> classNames = new HashMap();
        File srcDir = new File(Common.DIR_ORIGIN_PROGRAM + programName);
        File[] allFiles = srcDir.listFiles();
        for (File file : allFiles) {
            if (file.getName().endsWith(".java")) {
                classNames.put(file.getName().replaceAll(".java", ""), Common.DIR_ORIGIN_PROGRAM + programName);
            }
        }
        return classNames;
    }

    public static CodeSegment searchCodeSegmentByFunctionName(String programName, String className, String
            functionName) {
        //TODO 现在没考虑源代码有多级目录，以及现在只认为一个文件内只有一个类
        File file = new File(Common.DIR_ORIGIN_PROGRAM + programName + "/" + className + ".java");
        //TODO 找代码段的方法也很弱智，有空的话优化一下
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str;
            int startIndex = 0;
            int endIndex = 0;
            int flag = 0; // 0还在找函数头部，1正在找结尾
            String code = "";
            Stack<String> stack = new Stack<>();
            while ((str = reader.readLine()) != null) {
                if (flag == 0) {
                    if (str.contains(" " + functionName) && str.endsWith("{")) {
                        flag = 1;
                        endIndex = startIndex + 1;
                        stack.push("{");
                        code = str;
                        continue;
                    }
                    startIndex++;
                } else if (flag == 1) {
                    code += "\n";
                    code += str;
                    for (int i = 0; i < str.length(); i++) {
                        if (str.substring(i, i + 1).equals("{")) {
                            stack.push("{");
                        } else if (str.substring(i, i + 1).equals("}")) {
                            stack.pop();
                            if (stack.isEmpty()) {
                                return new CodeSegment(className, file, startIndex, endIndex, code);
                            }
                        }
                    }
                    endIndex++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isFetchingVal(String codeLine, String keyword, int index) {
        // 往后搜索
        for (int i = index + keyword.length(); i < codeLine.length(); i++) {
            if (codeLine.substring(i, i + 1).equals(" ")) {
                continue;
            }
            if (i == codeLine.length() - 1) {
                return true;
            } else {
                if (codeLine.substring(i, i + 1).equals("=") && !codeLine.substring(i, i + 2).equals("==")) {
                    return false;
                }
                if (codeLine.substring(i, i + 2).equals("+=") || codeLine.substring(i, i + 2).equals("-=") ||
                        codeLine.substring(i, i + 2).equals("*=") || codeLine.substring(i, i + 2).equals("/=") ||
                        codeLine.substring(i, i + 2).equals("%=") || codeLine.substring(i, i + 2).equals("++")) {
                    return false;
                }
            }
        }

        // 向前搜索
        for (int i = index - 1; i >= 0; i--) {
            if (codeLine.substring(i, i + 1).equals(" ")) {
                continue;
            }
            if (i == 0) {
                return true;
            }
            if (codeLine.substring(i - 1, i + 1).equals("++")) {
                return false;
            }
        }

        return true;
    }


    public static void main(String[] args) {
//        System.out.println("Compile result: " + common.Utils.compile("", "src/"));
    }

}
