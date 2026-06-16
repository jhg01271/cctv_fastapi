package kr.co.igns.framework.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

import org.springframework.http.HttpStatus;

import kr.co.igns.framework.config.log.LogUtil;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.unzip.UnzipUtil;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * 암호 압축
 * 
 * @author choi
 *
 */
public class Zip4jUtil {

	private static final String PASSWORD = "16611878";
	private static final int BUFFER_SIZE = 1024 * 2;

	/**
	 * 암호 압축해제
	 * 
	 * @param zipPath
	 * @param targetDir
	 */
	public static void unzipWithPassword(String zipPath, String targetDir) {
		ZipInputStream input = null;
		OutputStream output = null;
		try {

			ZipFile zipFile = new ZipFile(zipPath);

			if (zipFile.isEncrypted()) {
				zipFile.setPassword(PASSWORD);
			}

			@SuppressWarnings("unchecked")
			List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
			for (FileHeader header : fileHeaderList) {
				FileHeader fileHeader = (FileHeader) header;
				if (fileHeader != null) {
					String outFilePath = targetDir + "/" + fileHeader.getFileName();
					File outFile = new File(outFilePath);
					if (fileHeader.isDirectory()) {
						outFile.mkdirs();
						continue;
					}
					File parentDir = outFile.getParentFile();
					if (parentDir != null)
						if (!parentDir.exists()) {
							parentDir.mkdirs();
						}
					input = zipFile.getInputStream(fileHeader);
					output = new FileOutputStream(outFile);
					int readLen = -1;
					byte[] buff = new byte[4096];
					while ((readLen = input.read(buff)) != -1) {
						output.write(buff, 0, readLen);
					}
					output.close();
					input.close();
					UnzipUtil.applyFileAttributes(fileHeader, outFile);
				}
			}

		} catch (Exception e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "unzipWithPassword -> Exception", e.getMessage(), null);
		} finally {
			try {
				if (output != null) {
					output.close();
		        }
		        if (input != null) {
		        	input.close();
		        }
			}catch (IOException e) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "unzipWithPassword -> IOException", e.getMessage(), null);
			}
		}
	}

	/**
	 * 암호 압축
	 * 
	 * @param inputPath
	 * @param outputPath
	 */
	public static int zipWithPassword(String inputPath, String outputPath, boolean passwordFlag) {
		int fileCount = 0;
		InputStream input = null;
		FileOutputStream f_out = null;
		ZipOutputStream output = null;
		try {

			File sourceFile = new File(inputPath);

			ArrayList<File> filesToAdd = new ArrayList<File>();

			for (File file : sourceFile.listFiles()) {
				filesToAdd.add(file);
			}

			// 압축할 파일이 없으면 종료
			if (filesToAdd.size() == 0) {
				return fileCount;
			}
			File f = new File(outputPath);
			f_out = new FileOutputStream(f);
			output = new ZipOutputStream(f_out);

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptFiles(passwordFlag);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			parameters.setPassword(PASSWORD);
			for (File value : filesToAdd) {
				File file = (File) value;
				output.putNextEntry(file, parameters);
				if (file.isDirectory()) {
					output.closeEntry();
					continue;
				}
				input = new FileInputStream(file);
				byte[] readBuff = new byte[4096];
				int readLen = -1;
				while ((readLen = input.read(readBuff)) != -1) {
					output.write(readBuff, 0, readLen);
				}
				output.closeEntry();
				input.close();
				fileCount++;
			}

			output.finish();
			output.close();

			ZipFile zipFile = new ZipFile(outputPath);

			// ZIP파일을 분석할수 없으면 삭제
			if (!zipFile.isValidZipFile()) {
				FileDelete fileDeleteThread = new FileDelete(outputPath);
				fileDeleteThread.run();
				return 0;
			}

		} catch (FileNotFoundException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "zipWithPassword -> FileNotFoundException", e.getMessage(), null);
		} catch (Exception e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "zipWithPassword -> Exception", e.getMessage(), null);
		} finally {
			try {
				if (f_out != null) {
					f_out.close();
		        }
		        if (output != null) {
		        	output.close();
		        }
		        if (input != null) {
		        	input.close();
		        }
			}catch (IOException e) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "zipWithPassword -> IOException", e.getMessage(), null);
			}
		}
		return fileCount;
	}

	/**
	 * 하위폴더까지 압축
	 * 
	 * @param sourceFile
	 * @param sourcePath
	 * @param zos
	 * @throws Exception
	 */

	public static void zipEntry(File sourceFile, String sourcePath, java.util.zip.ZipOutputStream zos) throws Exception {
		// sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
		if (sourceFile.isDirectory()) {
			if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata 디렉토리 return
				return;
			}
			File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
			if (fileArray != null)
				for (File file : fileArray) {
					zipEntry(file, sourcePath, zos); // 재귀 호출
				}
		} else { // sourcehFile 이 디렉토리가 아닌 경우
			BufferedInputStream bis = null;
			FileInputStream f_in = null;
			try {
				String sFilePath = sourceFile.getPath();
				String zipEntryName = sFilePath.substring(sourcePath.length() - 3, sFilePath.length());
				f_in = new FileInputStream(sourceFile);
				bis = new BufferedInputStream(f_in);
				ZipEntry zentry = new ZipEntry(zipEntryName);
				zentry.setTime(sourceFile.lastModified());
				zos.putNextEntry(zentry);

				byte[] buffer = new byte[BUFFER_SIZE];
				int cnt = 0;
				while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zos.write(buffer, 0, cnt);
				}
				zos.closeEntry();
			} finally {
				if (bis != null) {
					bis.close();
				}
				if (f_in != null) {
					f_in.close();
				}
			}
		}
	}

	public static void dirCopy(File copyDir, File targetDir) {

		if (copyDir.isDirectory()) {
			targetDir.mkdir();
			String[] children = copyDir.list();
			if (children != null)
				for (String child : children) {
					dirCopy(new File(copyDir, child),
							new File(targetDir, child));
				}
		} else {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				fis = new FileInputStream(copyDir);
				bis = new BufferedInputStream(fis, 1024);
				fos = new FileOutputStream(targetDir);
				bos = new BufferedOutputStream(fos, 1024);

				int len = 0;
				byte[] buf = new byte[1024];

				while ((len = bis.read(buf)) != -1) {
					bos.write(buf, 0, len);
					bos.flush();
				}
			} catch (FileNotFoundException fn) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "dirCopy -> FileNotFoundException", fn.getMessage(), null);
			} catch (IOException ie) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "dirCopy -> IOException", ie.getMessage(), null);
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
					if (bis != null) {
						bis.close();
					}
					if (fos != null) {
						fos.close();
					}
					if (bos != null) {
						bos.close();
					}
				} catch (IOException ie) {
					LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "dirCopy -> IOException", ie.getMessage(), null);
				}
			}
		}
	}

	// 파일을 이동하는 메소드
	public static void fileMove(String inFileName, String outFileName) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(inFileName);
			fos = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			deleteFile(inFileName);

		} catch (IOException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "fileMove -> IOException", e.getMessage(), null);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "fileMove -> IOException", ie.getMessage(), null);
			}
		}
	}

	public static void deleteFile(String deleteFileName) {
		FileDelete fileDeleteThread = new FileDelete(deleteFileName);
		fileDeleteThread.run();
	}

	public static boolean deleteDir(File path) {
		if (!path.exists()) {
			return false;
		}

		File[] files = path.listFiles();
		if (files != null)
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDir(file);
				} else {
					FileDelete filesDeleteThread = new FileDelete(path);
					filesDeleteThread.run();
				}
			}

		FileDelete fileDeleteThread = new FileDelete(path);
		fileDeleteThread.run();
		return true;
	}
}
