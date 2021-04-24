package service;

import entity.File;

import java.util.List;

public interface FileService {
    public boolean uploadFile(String uuid, String filename, String filesize, String uploader, String uptime);
    public List<File> showFile();
    public List<String> getMacList() throws Exception;
}
