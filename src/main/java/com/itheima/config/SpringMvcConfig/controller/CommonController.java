package com.itheima.config.SpringMvcConfig.controller;

import com.itheima.config.SpringMvcConfig.common.R;
import com.itheima.config.SpringMvcConfig.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/*文件上传和下载*/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private  String  basePath;


    @PostMapping("/upload")
    public R<String > upload(MultipartFile file){
        //此处file为临时文件，我们在断点调试的时候可以看到，但是执行完整个方法之后就消失了
        log.info(file.toString());

        //创建目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //创建目录
            dir.mkdirs();
        }

        //获取原始文件名
        String originalFileName = file.getOriginalFilename();
        //获取原始文件名后缀
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        //为了防止出现重复的文件名，我们需要使用UUID
        String fileName = UUID.randomUUID() + suffix;//需要将原始文件名称后缀取过来组合起来

        //方法会抛异常，我们这里用try/catch处理一下
        try {
            //我们将其转存为E盘下的test.jpg,需要将图片转存的位置进行动态存储
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);//返回文件名到页面
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse) throws IOException {
        FileInputStream fis = new FileInputStream(basePath + name);
        ServletOutputStream os = httpServletResponse.getOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while((len = fis.read(buffer)) != -1){
            os.write(buffer,0,len);
        }
        fis.close();
        os.close();

    }
}
