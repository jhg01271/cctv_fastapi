package kr.co.igns.framework.api.file.util;

import kr.co.igns.system.setting.dao.FemsFileRepository;
import kr.co.igns.framework.utils.file.UploadParameters;
import kr.co.igns.framework.utils.type.Types;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class FileUtil {
    private final FemsFileRepository femsFileRepository;

    /*
     * // * 브라우저 별 한글 인코딩 //
     */
    public String encodingByBrowser(HttpServletRequest request, HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException {
        String header = request.getHeader("User-Agent");
        String name = "";
        String safeName = "";
        if (header.contains("Edge")) {
            name = URLEncoder.encode(fileName, "UTF-8");
            safeName = name.replace("\n", "").replace("\r", "");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + safeName);
        } else if (header.contains("MSIE") || header.contains("Trident")) { // IE 11버전부터 Trident로 변경되었기때문에 추가해준다.
            name = URLEncoder.encode(fileName, "UTF-8");
            safeName = name.replace("\n", "").replace("\r", "");
            response.setHeader("Content-Disposition", "attachment;filename=" + safeName);
        } else if (header.contains("Chrome")) {
            name = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            safeName = name.replace("\n", "").replace("\r", "");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + safeName);
        } else if (header.contains("Opera")) {
            name = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            safeName = name.replace("\n", "").replace("\r", "");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + safeName);
        } else if (header.contains("Firefox")) {
            name = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            safeName = name.replace("\n", "").replace("\r", "");
            response.setHeader("Content-Disposition", "attachment; filename=" + safeName);
        } else {
            name = URLEncoder.encode(fileName, "UTF-8");
            safeName = name.replace("\n", "").replace("\r", "");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + safeName);
        }
        return name;

    }
    /*
     * 파일타입
     */
    public String getFileType(String extension) {
        switch (extension.toUpperCase()) {
            case Types.FileExtensions.PNG:
            case Types.FileExtensions.JPG:
            case Types.FileExtensions.JPEG:
            case Types.FileExtensions.GIF:
            case Types.FileExtensions.BMP:
            case Types.FileExtensions.TIFF:
            case Types.FileExtensions.TIF:
                return Types.FileType.IMAGE;
            case Types.FileExtensions.PDF:
                return Types.FileType.PDF;
            default:
                return Types.FileType.ETC;
        }
    }
    /*
     * 파일사이즈 변환
     */
    public static String getReadableSize(long size) {
        if (size <= 0)
            return "0";
        double kbyte = size / (double) 1024;
        double mbyte = kbyte / 1024;
        DecimalFormat df = new DecimalFormat("##0.00#");
        String CalcuSize = df.format(mbyte) + "MB";

        return CalcuSize;
    }
    /*
     * 저장파일명 처리
     */
    public HashMap<String, String> getFileName(String fileName, UploadParameters parameters) {
        String realFile = "";
        HashMap<String, String> list = new HashMap<String, String>();

        String nameRemoveExtension = FilenameUtils.removeExtension(fileName);
        int iCnt = femsFileRepository.countByCompIdAndTargetTypeAndTargetIdAndFileNm(parameters.getCompId(), parameters.getTargetType(), parameters.getTargetId(), nameRemoveExtension);

        if (iCnt == 0) {
            realFile = nameRemoveExtension;
            list.put("filename", realFile);
            list.put("sort", "0");
        } else {
            realFile = nameRemoveExtension + "_" + iCnt;
            list.put("filename", realFile);
            list.put("sort", String.valueOf(iCnt));
        }
        System.out.println("iCnt >> " + list);
        return list;
    }
}
