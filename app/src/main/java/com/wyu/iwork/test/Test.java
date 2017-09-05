package com.wyu.iwork.test;

import com.wyu.iwork.R;
import com.wyu.iwork.model.TestModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jhj_Plus on 2016/10/26.
 */
public class Test {
    //------------
    private static final String TAG = "Test";
    private static Random sRandom = new Random();
    public static List<TestModel> getTestData(int count) {
        List<TestModel> testModels = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TestModel testModel = new TestModel();
            testModel.setIcon(R.drawable.email);
            testModel.setName("email");
            testModels.add(testModel);
        }
        return testModels;
    }

    public static List<TestModel> getRandomTestData(int realCount,int totalCount) {
        List<TestModel> testModels = new ArrayList<>();
        for (int i = 0; i < realCount; i++) {
            TestModel testModel = new TestModel();
            testModel.setIcon(R.drawable.email);
            testModel.setName("email");
            testModels.add(testModel);
        }
        for (int i = 0; i < totalCount-realCount; i++) {
            testModels.add(null);
        }
        return testModels;
    }


//    public static List<OrgnzParent> get(boolean hasRv,int count) {
//        List<OrgnzParent> parentItems = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            OrgnzParent parent = new OrgnzParent();
//            parent.setName("技术部");
//            List<OrgnzChild> childItems = new ArrayList<>();
//            for (int j = 0; j < 3; j++) {
//                OrgnzChild child = new OrgnzChild();
//                childItems.add(child);
//
//                child.setType(CommuOrgnzAdapter.CHILD_COMMON_TYPE);
//                if (hasRv) {
//                    child.setType(sRandom.nextInt(2));
//                    if (child.getType() == CommuOrgnzAdapter.CHILD_RV_TYPE) continue;
//                }
//
//                child.setName("阿斯顿");
//                child.setHead(R.drawable.ic_beauty);
//                child.setTel("1239990899");
//
//            }
//            parent.setChildItems(childItems);
//            parentItems.add(parent);
//        }
//        return parentItems;
//    }

//    public static List<OrganzManageParent> get2() {
//        List<OrganzManageParent> parents = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            OrganzManageParent parent = new OrganzManageParent();
//            parent.setName("技术部");
//            List<OrganzManageChild> children = new ArrayList<>();
//            for (int j = 0; j < 3; j++) {
//                OrganzManageChild child = new OrganzManageChild();
//                child.setName("设计部");
//                children.add(child);
//            }
//            parent.setChildren(children);
//            parents.add(parent);
//        }
//        return parents;
//    }

}
