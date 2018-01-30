package com.sreyas.cnstapmonitor.Models;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sreyas on 1/25/2018.
 */

public final class TapData {
    private static ArrayList<TapRecord> tapData;

    public static ArrayList<TapRecord> getTapData(Context context) {
        if(tapData == null){
            try{
                FileInputStream fileInputStream = context.openFileInput("TapData");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                tapData = (ArrayList<TapRecord>) objectInputStream.readObject();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if(tapData == null){
                    tapData = new ArrayList<>();
                }
            }
        }
        return tapData;
    }

    public static void addTapRecord(TapRecord tapRecord, Context context) {
        getTapData(context);
        tapData.add(tapRecord);
        Collections.sort(tapData, new Comparator<TapRecord>() {
            @Override
            public int compare(TapRecord o1, TapRecord o2) {
                return (int) (o1.getTimeStamp() - o2.getTimeStamp());
            }
        });
        saveTapData(context);
    }

    public static void deleteTapRecord(int position, Context context){
        getTapData(context);
        tapData.remove(position);
        saveTapData(context);
    }

    private static void saveTapData(Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput("TapData", Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tapData);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getMax(Context context){
        int maxTapCount = 0;
        for(TapRecord tapRecord: getTapData(context)){
            maxTapCount = Math.max(maxTapCount, tapRecord.getNumTaps());
        }
        return maxTapCount;
    }

    public static String printString(Context context) {
        getTapData(context);
        StringBuilder stringBuilder = new StringBuilder();
        for(TapRecord tapRecord:tapData){
            stringBuilder.append(tapRecord.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    public static void setFakeData(Context context){
        tapData = new ArrayList<>();
        for(int i = 2; i < 15; i++){
            tapData.add(new TapRecord(System.currentTimeMillis() / 60000 - (720 * i),(int )(Math.random() * 10 + 30)));
        }
        Collections.sort(tapData, new Comparator<TapRecord>() {
            @Override
            public int compare(TapRecord o1, TapRecord o2) {
                return (int) (o1.getTimeStamp() - o2.getTimeStamp());
            }
        });
        saveTapData(context);
    }
}
