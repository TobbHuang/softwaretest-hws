import common.Common;
import common.Utils;
import ui.MutationOperatorSelector;
import ui.PathSelector;
import ui.SelectorCallback;

import javax.swing.*;
import java.io.File;

/**
 * Created by huangtao on 2017/11/17.
 */
public class ToolMain {

    private String programName;

    public void start(){
        // 复制源代码
        PathSelector srcPathSelector = new PathSelector("请选择源代码", JFileChooser.FILES_AND_DIRECTORIES);
        srcPathSelector.start(new SelectorCallback() {
            @Override
            public void call(File[] files) {
                String path = files[0].getAbsolutePath();
                String str = path.substring(0, path.lastIndexOf("/")).replace("/", "-");
                programName = str.length() > 20 ? str.substring(str.length() - 20) : str;
                for(File file : files) {
                    copySrc(file.getAbsolutePath());
                }

                MutationOperatorSelector mutationOperatorSelector = new MutationOperatorSelector(programName);
                mutationOperatorSelector.init();
            }
        });
    }

    private boolean copySrc(String srcPath) {
        Utils.checkDirExist(Common.DIR_ORIGIN_PROGRAM + programName);
        try {
            Utils.copy(new File(srcPath), new File(Common.DIR_ORIGIN_PROGRAM + programName));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



}
