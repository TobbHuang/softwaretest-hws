package model;

import java.io.File;

/**
 * Created by huangtao on 2017/11/17.
 */
public class CodeSegment {

    public String className;

    public File file;

    public int startLineIndex;

    public int endLineIndex;

    public String code;

    public CodeSegment(String className, File file, int startLineIndex, int endLineIndex, String code) {
        this.className = className;
        this.file = file;
        this.startLineIndex = startLineIndex;
        this.endLineIndex = endLineIndex;
        this.code = code;
    }

    @Override
    public String toString() {
        return "CodeSegment{" + "className='" + className + '\'' + ", file=" + file + ", startLineIndex=" +
                startLineIndex + ", endLineIndex=" + endLineIndex + ", code='" + code + '\'' + '}';
    }
}
