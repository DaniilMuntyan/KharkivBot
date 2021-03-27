package com.example.demo.common_part.service;

import com.example.demo.common_part.constants.ProgramVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public final class SaveToFileService {
    private final ProgramVariables programVariables;

    @Autowired
    public SaveToFileService(ProgramVariables programVariables) {
        this.programVariables = programVariables;
    }

    public synchronized void writeTime(String path, String s, double time) {
        File csvFile = new File(path);
        try {
            csvFile.getParentFile().mkdirs();
            csvFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            String sb = "\n" + s + "," + time;
            writer.write(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
