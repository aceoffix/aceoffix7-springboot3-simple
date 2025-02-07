package com.acesoft.aceoffix7springbootsimple.controller;

import com.acesoftcorp.aceoffix.AceoffixCtrl;
import com.acesoftcorp.aceoffix.FileSaver;
import com.acesoftcorp.aceoffix.OpenModeType;
import com.acesoft.aceoffix7springbootsimple.util.GetDirPathUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/doc")
public class DocumentController {
    //Get the disk path of the doc directory
    private final String dir = GetDirPathUtil.getDirPath() + "/static/doc/";

    @RequestMapping(value = "/openFile")
    public ModelAndView openFile(HttpServletRequest request) {
        AceoffixCtrl aceCtrl = new AceoffixCtrl(request);
        aceCtrl.webOpen("/doc/editword.docx", OpenModeType.docNormalEdit, "Luna");
        request.setAttribute("aceoffix", aceCtrl.getHtml());
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @RequestMapping("/saveFile")
    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile(dir + fs.getFileName());
        fs.close();
    }
}



