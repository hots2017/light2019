package com.hots.common.component.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hots.common.exception.BasicException;
import com.hots.common.util.StreamUtil;
import com.hots.common.util.StringUtil;
import com.hots.common.util.TimeUtil;

@Component
public class FileSystemComponent {
    /**
     * 创建文件对象
     *
     * @param filePath  保存路径
     * @param existDeal true：文件存在，自动追加后缀；false：文件存在，会被覆盖
     * @return File
     */
    public File createFile(String filePath, boolean existDeal) {
        File file = null;

        if (!StringUtil.isEmpty(filePath)) {
            filePath = filePath.replaceAll("\\\\", "/");
        } else {
            return null;
        }

        // 创建文件目录
        file = new File(filePath.substring(0, filePath.lastIndexOf("/")));
        if (!file.exists()) {
            file.mkdirs();
        }
        // 创建文件路径
        file = new File(filePath);
        if (file.exists() && existDeal) {
            return createFile(getFilePathIfExist(filePath), true);
        }

        return file;
    }

    /**
     * 文件夹和其内部重命名
     *
     * @param location:；路径
     * @param filter:过滤/替换的文字
     */
    public void renameFileAll(String location, Map<String, String> filter) {
        File target = new File(location);

        if (filter != null && filter.size() > 0 && target.exists()) {
            File newTarget = null;
            for (String filterName : filter.keySet()) {
                String fileName = target.getName();
                newTarget = new File(
                        target.getParent() + File.separator + fileName.replaceAll(filterName, filter.get(filterName)));
                target.renameTo(newTarget);
                target = newTarget;
            }
        }

        if (target.isDirectory()) {
            for (File fileSub : target.listFiles()) {
                renameFileAll(fileSub.getAbsolutePath(), filter);
            }
        }

    }

    /**
     * 文件复制
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @throws BasicException
     */
    private void copyFile(File sourceFile, File targetFile) throws BasicException {

        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {

            // 新建文件输入流并对它进缓冲
            input = new FileInputStream(sourceFile);
            inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            output = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(output);

            int len;
            while ((len = inBuff.read()) != -1) {
                outBuff.write(len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

        } catch (Exception e) {

            throw new BasicException("文件复制异常。", "需要复制的文件：" + (sourceFile != null ? sourceFile.getAbsolutePath() : ""),
                    e);

        } finally {
            StreamUtil.closeInputStream(inBuff);
            StreamUtil.closeOutputStream(outBuff);
            StreamUtil.closeInputStream(input);
            StreamUtil.closeOutputStream(output);
        }

    }

    /**
     * 创建文件对象: 文件存在，文件名称自动追加当前时间
     *
     * @param filePath 保存路径
     * @return File
     */
    private String getFilePathIfExist(String filePath) {

        // 创建文件路径
        File file = new File(filePath);
        if (file.exists()) {
            String fileName = file.getName();
            String timeCreate = TimeUtil.format(TimeUtil.FORMAT_DATE_NUM_FULL, new Date());

            if (fileName.lastIndexOf(".") > 0) {

                int index = fileName.lastIndexOf(".");
                String fileNameTmp = fileName.substring(0, index) + "_" + timeCreate;
                String fileNameType = fileName.substring(index, fileName.length());

                fileNameTmp = fileNameTmp + fileNameType;

                return getFilePathIfExist(filePath.replace(fileName, fileNameTmp));

            } else {
                return getFilePathIfExist(filePath.replace(fileName, fileName + "_" + timeCreate));
            }
        } else {
            return filePath;
        }

    }

    /**
     * 文件/文件夹拷贝
     *
     * @param              sourceDirStr:
     * @param targetDirStr
     * @return
     */
    public boolean directorCopy(String sourceDirStr, String targetDirStr) {

        try {
            // 获取源文件夹下的文件或目录
            File sourceDir = new File(sourceDirStr);

            if (sourceDir.exists()) {

                // 查看目录是否存在，不存在则新建
                File targetDir = new File(targetDirStr);
                if (!(targetDir).exists()) {
                    targetDir.mkdirs();
                }

                if (sourceDir.isDirectory()) {

                    File[] fileArr = sourceDir.listFiles();
                    if (fileArr != null && fileArr.length > 0) {

                        for (File file : fileArr) {

                            String filePath = file.getAbsolutePath().replaceAll("\\\\", "/");
                            String sourcePath = sourceDir.getAbsolutePath().replaceAll("\\\\", "/");
                            String targetPath = targetDir.getAbsolutePath().replaceAll("\\\\", "/");
                            String location = filePath.replaceAll(sourcePath, targetPath);

                            if (file.isFile()) {
                                copyFile(file, new File(location));
                            } else {
                                directorCopy(filePath, location);
                            }

                        }

                    }

                } else {
                    copyFile(sourceDir, targetDir);
                }
            }

            return true;
        } catch (Exception e) {

            return false;

        }

    }
}