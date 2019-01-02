package com.hots.common.component.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import com.hots.common.exception.BasicException;
import com.hots.common.util.StreamUtil;
import com.hots.common.util.StringUtil;

public class PackageFileComponent{
    /**
     * 文件打包
     *
     * @param out         目标文件流
     * @param source      需要压缩的文件/文件夹
     * @param packageFile 需要生成压缩文件
     * @param mode        压缩文件格式：1-zip,2-jar
     */
    public void packageFile(OutputStream out, File source, File packageFile, int mode) throws BasicException {

        FileInputStream inputStream = null;
        try {

            if (source.exists()) {

                if (source.isDirectory()) {

                    File[] listFiles = source.listFiles();

                    if (listFiles != null && listFiles.length > 0) {

                        for (File file : listFiles) {
                            packageFile(out, file, packageFile, mode);
                        }

                    }
                } else if (!packageFile.equals(source)) {

                    String location = packageFile.getParent();
                    String path = getRelativePath(location, source);

                    switch (mode) {
                    case 1:
                        ZipEntry zipEntry = new ZipEntry(path);
                        ZipOutputStream outZipTmp = (ZipOutputStream) out;
                        outZipTmp.putNextEntry(zipEntry);
                        break;
                    case 2:
                        JarEntry jarEntry = new JarEntry(path);
                        JarOutputStream outJarTmp = (JarOutputStream) out;
                        outJarTmp.putNextEntry(jarEntry);
                        break;
                    default:
                        break;
                    }

                    // 文件输入流
                    inputStream = new FileInputStream(source);

                    int j = 0;
                    byte[] buffer = new byte[1024];
                    while ((j = inputStream.read(buffer)) > 0) {
                        out.write(buffer, 0, j);
                    }

                }

            }

        } catch (Exception e) {

            throw new BasicException("文件压缩异常。", "需要压缩的文件：" + (source != null ? source.getAbsolutePath() : ""), e);

        } finally {
            StreamUtil.closeInputStream(inputStream);
        }

    }

    /**
     * 获取文件相对路径
     *
     * @param targetPath 压缩源路径
     * @param sourceFile 目标文件
     */
    private String getRelativePath(String targetPath, File sourceFile) {

        String relativePath = "";

        if (sourceFile.exists()) {

            File targetFile = new File(targetPath);
            relativePath = sourceFile.getName();

            while (true) {
                sourceFile = sourceFile.getParentFile();

                if (sourceFile == null) {
                    break;
                }

                if (sourceFile.equals(targetFile)) {
                    break;
                } else {
                    relativePath = sourceFile.getName() + File.separator + relativePath;
                }
            }
        }

        return relativePath;

    }

    /**
     * 文件压缩
     *
     * @param sourceArr       需要压缩的文件列表
     * @param location        目标文件路径
     * @param packageFileName 目标文件名
     * @param mode            压缩文件格式：1-zip,2-jar
     */
    public boolean packageFile(String[] sourceArr, String location, String packageFileName, int mode) {
        if (StringUtil.isEmpty(location)) {
            return false;
        }

        // 查看目录是否存在，不存在则新建
        File targetDir = new File(location);
        if (!(targetDir).exists()) {
            targetDir.mkdirs();
        }

        OutputStream outputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File packageFile = null;
            switch (mode) {
            case 1:
                packageFile = new File(location + File.separator + packageFileName + ".zip");
                fileOutputStream = new FileOutputStream(packageFile);
                outputStream = new ZipOutputStream(fileOutputStream);
                break;
            case 2:
                packageFile = new File(location + File.separator + packageFileName + ".jar");
                fileOutputStream = new FileOutputStream(packageFile);
                outputStream = new JarOutputStream(fileOutputStream);
                break;
            default:
                packageFile = new File(location + File.separator + packageFileName + ".zip");
                fileOutputStream = new FileOutputStream(packageFile);
                outputStream = new ZipOutputStream(fileOutputStream);
                break;
            }

            if (sourceArr != null && sourceArr.length > 0) {

                for (String path : sourceArr) {

                    packageFile(outputStream, new File(path), packageFile, mode);

                }

            }

            return true;

        } catch (Exception e) {

            new BasicException("文件压缩异常", sourceArr, e);
            return false;

        } finally {

            StreamUtil.closeOutputStream(outputStream);
            StreamUtil.closeOutputStream(fileOutputStream);
        }

    }

    /**
     * 解压文件
     *
     * @param sourceFileStr 压缩文件路径
     * @param targetDirStr  解压路径
     * @return
     */
    public boolean unPackageFile(String sourceFileStr, String targetDirStr) {

        if (StringUtil.isEmpty(sourceFileStr) || StringUtil.isEmpty(targetDirStr)) {
            return false;
        }

        File sourceFile = new File(sourceFileStr);
        if (sourceFile.exists()) {

            String[] sourceFileInfo = sourceFile.getName().trim().split("\\.");

            if (sourceFileInfo != null && sourceFileInfo.length > 1) {

                FileOutputStream fileOutputStream = null;
                InputStream inputStream = null;

                try {

                    File targetDir = new File(targetDirStr);
                    targetDirStr = targetDir.getAbsolutePath() + File.separator + sourceFileInfo[0];

                    switch (sourceFileInfo[1].toLowerCase()) {
                    case "zip":

                        ZipFile zipFile = new ZipFile(sourceFileStr);
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            File source = new File(targetDirStr + File.separator + entry.getName());
                            if (!entry.isDirectory() && !source.getParentFile().exists()) {
                                source.getParentFile().mkdirs();
                            }
                            fileOutputStream = new FileOutputStream(source);
                            inputStream = zipFile.getInputStream(entry);

                            byte[] b = new byte[1024];
                            int length = 0;
                            while ((length = inputStream.read(b)) != -1) {
                                fileOutputStream.write(b, 0, length);
                            }

                        }

                        break;
                    case "jar":

                        JarFile jarFile = new JarFile(sourceFileStr);
                        Enumeration<? extends JarEntry> entriesJar = jarFile.entries();
                        while (entriesJar.hasMoreElements()) {
                            JarEntry entry = entriesJar.nextElement();
                            File source = new File(targetDirStr + File.separator + entry.getName());
                            if (!entry.isDirectory() && !source.getParentFile().exists()) {
                                source.getParentFile().mkdirs();
                            }
                            fileOutputStream = new FileOutputStream(source);
                            inputStream = jarFile.getInputStream(entry);

                            byte[] b = new byte[1024];
                            int length = 0;
                            while ((length = inputStream.read(b)) != -1) {
                                fileOutputStream.write(b, 0, length);
                            }

                        }

                        break;
                    default:
                        break;

                    }

                } catch (Exception e) {
                    new BasicException("解压文件异常", sourceFileStr, e);
                } finally {
                    StreamUtil.closeInputStream(inputStream);
                    StreamUtil.closeOutputStream(fileOutputStream);
                }

                return true;
            }

        }

        return false;

    }

}
