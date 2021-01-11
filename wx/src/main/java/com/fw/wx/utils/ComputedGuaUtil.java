package com.fw.wx.utils;

import com.fw.wx.domain.ComputeGua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yqf
 * @date 2020/11/11 下午8:33
 */
public class ComputedGuaUtil {

    private static Integer count;

    private static Map<List<Integer>,String> baGua = new HashMap<>(10);

    public static ComputeGua computedYao(ComputeGua computeGua){
        List<Boolean> changeGua = computeGua.getChangeGua();
        count = 0;
        System.out.println(changeGua);
        for (Boolean aBoolean : changeGua) {
            if(aBoolean){
                count++;
            }
        }
        System.out.println(count);
        switch (count){
            case 1:
                for (int i = 0; i <changeGua.size() ; i++) {
                    if(changeGua.get(i)){
                        computeGua.setPos(i+1);
                        return computeGua;
                    }
                }
            case 2:
                for (int i = 0; i <changeGua.size() ; i++) {
                    if(changeGua.get(i)){
                        computeGua.setPos(i+1);
                        return computeGua;
                    }
                }
            case 3:
                computeGua.setChange(true);
                return changeGua(computeGua);
            case 4:
                for (int i = changeGua.size()-1; i >0 ; i--) {
                    if(!changeGua.get(i)){
                        computeGua.setPos(i+1);
                        return computeGua;
                    }
                }
            case 5:
                ComputeGua gua = changeGua(computeGua);
                for (int i = 0; i < changeGua.size(); i++) {
                    if(!changeGua.get(i)){
                        gua.setPos(i+1);
                        gua.setChange(true);
                        return gua;
                    }
                }
            case 6:
                int num = 0;
                List<Integer> top = computeGua.getTop();
                List<Integer> bottom = computeGua.getBottom();
                for (Integer integer : top) {
                    num+=integer;
                }

                for (Integer integer : bottom) {
                    num+=integer;
                }

                if(num==6||num==0){
                    computeGua.setPos(7);
                    return computeGua;
                }else {
                    computeGua.setChange(true);
                    return changeGua(computeGua);
                }
            default:
                return computeGua;

        }


    }
    private static ComputeGua changeGua(ComputeGua computeGua){
        List<Integer> top = computeGua.getTop();
        List<Integer> bottom = computeGua.getBottom();
        List<Integer> newTop = new ArrayList<>();
        List<Integer> newBottom = new ArrayList<>();
        for (int i = 0; i < top.size() ; i++) {
            if(top.get(i)==1){
                newTop.add(i,0);
            }else {
                newTop.add(i,1);
            }
        }

        for (int i = 0; i < bottom.size() ; i++) {
            if(bottom.get(i)==1){
                newBottom.add(i,0);
            }else {
                newBottom.add(i,1);
            }
        }
        computeGua.setTop(newTop);
        computeGua.setBottom(newBottom);
        return computeGua;
    }
}
