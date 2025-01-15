package com.example.CarDealerShip.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
public enum FileExtension {

    ANY("any"),
    PDF(".pdf"),
    JPG(".jpg"),
    PNG(".png"),
    JPEG(".jpeg"),
    DOCX(".docx"),
    DOC(".doc"),
    DOCM(".docm"),
    PPTX(".pptx"),  
    PPT(".ppt"),
    XLSX(".xlsx"),
    XLS(".xls"),
    TXT(".txt"),
    PUB(".pub"),
    ZIP(".zip"),
    ICS(".ics"),
    MP3(".mp3"),
    OGG(".ogg"),
    WAV(".wav"),
    MOV(".mov"),
    MP4(".mp4"),
    AVI(".avi");

    private String ex;

    private FileExtension(String s) {
        ex = s;
    }
}
