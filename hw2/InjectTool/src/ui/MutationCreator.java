package ui;

import common.Common;
import common.Utils;
import model.MutationModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangtao on 2017/11/13.
 */
public class MutationCreator {

    private String programName;
    private String muIndex;

    public MutationCreator(String programName, String muIndex) {
        this.programName = programName;
        File root = new File(Common.DIR_MUTATION);
        if (!root.exists()) {
            root.mkdir();
        }
        // 用时间来标识变异
        this.muIndex = muIndex;
    }

    public boolean createMutation() {
        // 读入变异说明文件
        List<MutationModel> mutationModelList = new ArrayList<>();
        File muContent = new File(Common.DIR_MUTATION_CONTENT + programName + "/" + muIndex);
        try {
            BufferedReader muContentReader = new BufferedReader(new FileReader(muContent));
            String muItem;
            while ((muItem = muContentReader.readLine()) != null) {
                mutationModelList.add(new MutationModel(muItem, Integer.parseInt(muContentReader.readLine()), muContentReader.readLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // copy源代码，注入变异
        inject("", mutationModelList);

        return true;
    }

    private void inject(String path, List<MutationModel> mutationModelList) {
        File file = new File(Common.DIR_ORIGIN_PROGRAM + programName + "/" + path);
        if (file.isDirectory()) {
            // 遍历文件夹内所有文件
            File[] files = file.listFiles();
            for (File subFile : files) {
                inject(path + "/" + subFile.getName(), mutationModelList);
            }
        } else {
            // 查找该文件是否该变异
            boolean flag = false;
            List<MutationModel> subMutationModelList = new ArrayList<>();
            for (MutationModel mutationModel : mutationModelList) {
                if (mutationModel.path.equals(path)) {
                    flag = true;
                    subMutationModelList.add(mutationModel);
                }
            }
            mutationModelList.removeAll(subMutationModelList);
            Collections.sort(subMutationModelList);
            String cpPath = Common.DIR_MUTATION + programName + "/" + muIndex + "/" + path;

            Utils.checkDirExist(cpPath.substring(0, cpPath.lastIndexOf("/")));
            try {
                if (flag) {
                    File newFile = new File(cpPath);
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    FileWriter writer = new FileWriter(newFile);
                    int index = 1;
                    String originCode;
                    while ((originCode = reader.readLine()) != null) {
                        if (subMutationModelList.size() > 0 && index == subMutationModelList.get(0).line) {
                            writer.write(subMutationModelList.get(0).replaceText + "\n");
                            subMutationModelList.remove(0);
                        } else {
                            writer.write(originCode + "\n");
                        }
                        index++;
                    }
                    reader.close();
                    writer.close();
                } else {
                    // 直接copy
                    Utils.copy(file, new File(cpPath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inject(String path, List<MutationModel> mutationModelList, String muIndex){
        this.muIndex = muIndex;
        inject(path, mutationModelList);
    }


}
