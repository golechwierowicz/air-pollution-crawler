package service;

import model.WebContent;

import java.util.List;
import java.util.stream.Collectors;

public class FilterWordServiceImpl {
    private FilterWordServiceImpl() {};
    public static List<WebContent> filterWithWord(List<WebContent> webContents, String word) {
        return webContents.stream().filter(wc -> wc.containsWord(word)).collect(Collectors.toList());
    }
}