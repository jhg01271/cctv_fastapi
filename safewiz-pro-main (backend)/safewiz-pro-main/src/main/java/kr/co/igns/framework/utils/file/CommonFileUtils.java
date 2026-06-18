package kr.co.igns.framework.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.utils.type.DateUtils;

public class CommonFileUtils {
	private static List<String> fileExtensions = new ArrayList<String>();

	public static boolean isFileExtension(String fileName, List<String> extensions) {
		String[] fileSplit = fileName.split("\\.");
		return extensions.contains(fileSplit[fileSplit.length - 1].toLowerCase());
	}

	public static boolean checkFileExt(HttpServletRequest request, List<String> extensions) {
		boolean flag = true;
		Object files = new HashMap<Object, Object>();

		try {
			MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) request;
			files = multipartReq.getFileMap();
		} catch (Exception var7) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Check File Ext -> Exception", var7.getMessage(), null);
		}
		for (Object o : ((Map<?, ?>) files).values()) {
			MultipartFile file = (MultipartFile) o;
			if (file != null) {
				String realFileNm = file.getOriginalFilename();
				if (!realFileNm.equals("") && !isFileExtension(realFileNm, extensions)) {
					return false;
				}
			}
		}

		return flag;
	}

	public static List<String> getFileExtensions() {
		return fileExtensions;
	}

	public static void setFileExtensions(List<String> fileEx) {
		fileExtensions = fileEx;
	}

	public static ArrayList<String[]> uploadMutilpleFile(HttpServletRequest request, String fileUploadLocation,
			long fileSize) {
		ArrayList<String[]> fileList = new ArrayList<String[]>();
		Object files = new HashMap<Object, Object>();

		try {
			MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) request;
			files = multipartReq.getFileMap();
		} catch (Exception var10) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Upload Mutilple File -> Exception", var10.getMessage(), null);
		}

		fileUploadLocation = DateUtils.timeStamp("yyyy/MM/dd");
		for (Object o : ((Map<?, ?>) files).values()) {
			MultipartFile file = (MultipartFile) o;
			if (file != null) {
				String realFileNm = file.getOriginalFilename();
				if (!realFileNm.equals("")) {
					String saveFileNm = changeToSaveFileName(realFileNm);
					fileList.add(uploadToOneFile(request, fileUploadLocation, file, saveFileNm, fileSize));
				}
			}
		}

		return fileList;
	}

	public static String[] uploadToOneFile(HttpServletRequest req, String saveFileLocation, MultipartFile file, String saveFileName, long fileSize) {
		String[] fileName = new String[] { saveFileLocation, null, null, String.valueOf(file.getSize()) };
		File dir = null;
		FileOutputStream fout = null;
		if (file.getSize() > fileSize) {
			throw new CUserDefinedException("fileSizeExceed");
		} else {
			String tempSaveFileLocation = saveFileLocation;
			if (!"".equals(file.getOriginalFilename())) {
				fileName[1] = file.getOriginalFilename();
				if (checkFileExtension(saveFileName)) {
					throw new CUserDefinedException("noUploadFile");
				}

				dir = new File(saveFileLocation);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				try {
					fileName[2] = saveFileName;
					fout = new FileOutputStream(tempSaveFileLocation + File.separator + saveFileName);
					FileCopyUtils.copy(file.getInputStream(), fout);
				} catch (Exception var10) {
					throw new CUserDefinedException("fileUploadFail");
				} finally {
					try {
				        if (fout != null) {
				        	fout.close();
				        }
					}catch (IOException e) {
						LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "OS -> IOException", e.getMessage(), null);
					}
				}
			}

			return fileName;
		}
	}

	public static String changeToSaveFileName(String fileName) {
		try {
			String[] fileNameSplit = fileName.split("\\.");			
			Random numGen = SecureRandom.getInstance("SHA1PRNG");
			long seed = System.currentTimeMillis();
			numGen.setSeed(seed);
			return "" + System.currentTimeMillis() + Math.round(numGen.nextDouble() * 10000.0D) + "." + fileNameSplit[fileNameSplit.length - 1];
		} catch (NoSuchAlgorithmException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Change To Save FileName -> NoSuchAlgorithmException", e.getMessage(), null);
			return null;
		}
	}

	public static boolean checkFileExtension(String fileName) {
		String[] fileSplit = fileName.split("\\.");
		return fileExtensions.contains(fileSplit[fileSplit.length - 1].toLowerCase());
	}

	@SuppressWarnings("deprecation")
	public static void fileDownload(HttpServletRequest request, HttpServletResponse response, String filePath, String fileNm, String realFileNm) {
		String realFilePath = "";
		realFilePath = filePath + File.separator + fileNm;
		File file = new File(realFilePath);
		if (file.exists()) {
			String header = request.getHeader("User-Agent");
			String docName = "";
			OutputStream os = null;
			FileInputStream is = null;

			try {
				if (header.contains("MSIE")) {
					docName = URLEncoder.encode(realFileNm, "UTF-8").replaceAll("\\+", "%20");
				} else if (header.contains("Trident")) {
					docName = URLEncoder.encode(realFileNm, "UTF-8").replaceAll("\\+", "%20");
				} else if (!header.contains("Chrome")) {
					if (header.contains("Opera")) {
						docName = new String(realFileNm.getBytes("UTF-8"), "8859_1");
					} else if (header.contains("Safari")) {
						docName = new String(realFileNm.getBytes("UTF-8"), "8859_1");
						docName = URLDecoder.decode(docName);
					} else {
						docName = new String(realFileNm.getBytes("UTF-8"), "8859_1");
						docName = URLDecoder.decode(docName);
					}
				} else {
					StringBuilder sb = new StringBuilder();

					for (int i = 0; i < realFileNm.length(); ++i) {
						char c = realFileNm.charAt(i);
						if (c > 126) {
							sb.append(URLEncoder.encode(Character.toString(c), "UTF-8"));
						} else {
							sb.append((char) c);
						}
					}

					docName = sb.toString();
				}
				String safeDocName = docName.replace("\n", "").replace("\r", "");

				response.setContentType("application/octet-stream; charset=utf-8");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", safeDocName);
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setCharacterEncoding("UTF-8");
				os = response.getOutputStream();
				is = null;
				is = new FileInputStream(file);
				FileCopyUtils.copy(is, os);
				os.flush();
			} catch (FileNotFoundException var25) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "File Download -> FileNotFoundException", var25.getMessage(), null);
				throw new CUserDefinedException("fileNotFound");
			} catch (IOException var26) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "File Download -> IOException", var26.getMessage(), null);
				throw new CUserDefinedException("fileDownloadFail");
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException var24) {
						LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "OS -> IOException", var24.getMessage(), null);
					}
				}

				if (is != null) {
					try {
						is.close();
					} catch (IOException var23) {
						LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IS -> IOException", var23.getMessage(), null);
					}
				}
			}

		} else {
			throw new CUserDefinedException("fileNotFound");
		}
	}

	public static void deleteFile(HttpServletRequest request, String fullPath, String file) {
		FileDelete fileDeleteThread = new FileDelete(file);
		fileDeleteThread.run();
	}

	public static void copy(String fOrg, String fTarget, String fTargetName) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			File fsTarget = new File(fTarget);
			if (!fsTarget.exists()) {
				fsTarget.mkdirs();
			}

			String fsTargetName = fsTarget + File.separator + fTargetName;
			inputStream = new FileInputStream(fOrg);
			outputStream = new FileOutputStream(fsTargetName, true);
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
			long size = fcin.size();
			fcin.transferTo(0L, size, fcout);
			fcout.close();
			fcin.close();
			inputStream.close();
			outputStream.close();
		} catch (IOException var11) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "file Not Copy -> IOException", var11.getMessage(), null);
			throw new CUserDefinedException("fileNotCopy");
		} finally {
			try {
				if (fcin != null) {
					fcin.close();
		        }
		        if (fcout != null) {
		        	fcout.close();
		        }
		        if (inputStream != null) {
		        	inputStream.close();
		        }
		        if (outputStream != null) {
		        	outputStream.close();
		        }
			}catch (IOException e) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "File Copy -> IOException", e.getMessage(), null);
			}
		}
	}

	public static String createFileName(String fileName) {
		String[] fileSplit = fileName.split("\\.");
		String extension = fileSplit[fileSplit.length - 1].toLowerCase();
		UUID.randomUUID().toString();
		return extension;
	}

	public static String createFilePath(String prefix) {
		Calendar cal = Calendar.getInstance();
		// String year = Integer.toString(cal.get(1));
		// String month = Integer.toString(cal.get(2) + 1);
		String date = Integer.toString(cal.get(5));
		// String var10001 = File.separator;
		// String var10003 = File.separator;
		// String var10005 = File.separator;
		return (String) date;
	}

	/*
	 * InputStream 을 File로 변환
	 */
	public static File convertInputStreamToFile(InputStream in) throws IOException {

		File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
		// JVM이 끝나고 삭제됨
		tempFile.deleteOnExit();

		FileUtils.copyInputStreamToFile(in, tempFile);

		return tempFile;
	}

	static {
		fileExtensions.addAll(Arrays.asList("asp", "jsp", "html", "htm", "com", "exe", "sh", "php"));
	}

	// 파일 해싱
	public static String concatenateFiles(List<String> fileNames) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		FileReader fileReader = null;
		for (String fileName : fileNames) {
			try {
				fileReader = new FileReader(fileName);
				reader = new BufferedReader(fileReader);
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (FileNotFoundException e1) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Concatenate Files -> FileNotFoundException", e1.getMessage(), null);
			} catch (IOException e) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Concatenate Files -> IOException", e.getMessage(), null);
			} finally {
				try {
					if (fileReader != null) {
						fileReader.close();
			        }
			        if (reader != null) {
			        	reader.close();
			        }
				}catch (IOException e) {
					LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Concatenate Files -> IOException", e.getMessage(), null);
				}
			}
		}
		return sb.toString();
	}

	public static String calculateSHA256(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Calculate SHA256 -> NoSuchAlgorithmException", e.getMessage(), null);
			return null;
		}
	}
}
