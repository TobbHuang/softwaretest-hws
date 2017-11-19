package model;

/**
 * Created by huangtao on 2017/11/15.
 */
public class MutationModel implements Comparable<MutationModel> {

    public String path;
    public Integer line;
    public String replaceText;

    public MutationModel(String path, int line, String replaceText) {
        this.path = path;
        this.line = line;
        this.replaceText = replaceText;
    }


    @Override
    public int compareTo(MutationModel o) {
        return line.compareTo(o.line);
    }
}
