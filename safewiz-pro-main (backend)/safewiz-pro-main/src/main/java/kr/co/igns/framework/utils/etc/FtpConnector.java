package kr.co.igns.framework.utils.etc;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import org.apache.commons.net.ftp.FTPReply;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import kr.co.igns.framework.config.log.LogUtil;

@Component
public class FtpConnector {

	public static FTPClient ftpClient = new FTPClient();

	private static int  MAX_ATTEMPTS = 5;

	public static void doConnect(String add, FTPClient client) {

		try { // 포트는 여기 메소드에서 밖에 쓰이지 않으므로 지역변수로 선언.
			String server = add; // FTPServer에 Connect서버 연결
			int port = 21;
			client.connect(server, port); // 응답이 정상적인지 확인 하기 위해 응답 받아오기
			int replyCode = client.getReplyCode(); // 서버와의 응답이 정상인지 확인
			if (FTPReply.isPositiveCompletion(replyCode)) {
				LogUtil.ConsoleLogInfo(HttpStatus.OK, "Success connection", "[" + client.getReplyCode() + "] " + server + "에 연결되었습니다.", null);
			}
		} catch (SocketException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Server connect failed (Socket Error)", e.getMessage(), null);			
			System.exit(1);
		} catch (IOException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Server connect failed", e.getMessage(), null);
			System.exit(1);
		}
	}

	// FTP서버 로그인
	public static boolean doLogin(String userId, String userPw, FTPClient client) {
		boolean isValid = false;
		int loginCount = 0;
		try {
			while(isValid == false && loginCount < MAX_ATTEMPTS) {
				String id = userId;
				String pw = userPw;
				isValid = client.login(id, pw);
				loginCount++;
			}
			if (!isValid && loginCount == MAX_ATTEMPTS) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Login failures exceeded", Integer.toString(client.getReplyCode()), null);
				System.exit(1);
			} else if (!isValid && loginCount < MAX_ATTEMPTS) {
				LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Login failures [" + loginCount + "]", Integer.toString(client.getReplyCode()), null);
			} else {
				LogUtil.ConsoleLogInfo(HttpStatus.OK, "Login success", Integer.toString(client.getReplyCode()), null);
			}
		} catch (IOException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Login incorrect", Integer.toString(ftpClient.getReplyCode()), null);
			return false;
		}
		return true;
	}

	// FTP서버 disconnect
	public static boolean doDisconnect(FTPClient client) {
		try {
			if (client.isConnected()) {
				// client.logout();
				client.disconnect();
				LogUtil.ConsoleLogInfo(HttpStatus.OK, "ftp disconnect success", "", null);
			}
		} catch (Throwable e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "ftp disconnect failed", e.getMessage(), null);
		}
		return true;
	}

	// 로컬의 파일 리스트와 디렉토리 정보를 취득하는 함수.
	public static void getUploadList(String root, List<String> files, List<String> directories) {
		File upload = new File(root);
		// root로 받은 경로의 파일 리스트를 받아 온다.
		for (File file : upload.listFiles()) {
			// 리스트의 객체가 파일이면
			if (file.isFile()) {
				// files 리스트에 경로를 추가한다.
				files.add(file.getAbsolutePath());
			} else {
				// 디렉토리리면 함수의 재귀적 방식으로 하위 탐색을 시작한다.
				getUploadList(file.getAbsolutePath(), files, directories);
				// directories 리스트에 디렉토리 경로를 추가한다.
				directories.add(file.getAbsolutePath().replace(root, ""));
			}
		}
	}

	// FTP의 파일 리스트와 디렉토리 정보를 취득하는 함수.
	public static boolean getFileList(FTPClient client, String cw, List<String> files, List<String> directories)
			throws IOException {
		// FTP의 디렉토리 커서를 이동한다.
		if (client.changeWorkingDirectory(cw)) {
			// 해당 디렉토리의 파일 리스트를 취득한다.
			for (FTPFile file : client.listFiles()) {
				// 리스트의 객체가 파일이면
				if (file.isFile()) {
					// files 리스트에 경로를 추가한다.
					files.add(cw + file.getName());
				} else {
					// 디렉토리리면 함수의 재귀적 방식으로 하위 탐색을 시작한다.
					if (!getFileList(client, cw + file.getName() + File.separator, files, directories)) {
						return false;
					} else {
						// directories 리스트에 디렉토리 경로를 추가한다.
						directories.add(cw + file.getName() + File.separator);
					}
				}
			}
			// 이건 FTP의 디렉토리 커서를 상위로 이동하는 함수입니다.(여기서는 사용하지 않았으나 자주 사용하는 함수입니다.)
			// client.changeToParentDirectory();
			// FTP의 디렉토리 커서를 이동한다.
			return client.changeWorkingDirectory(File.separator);
		}
		// 커서 이동에 실패하면 false를 리턴한다.
		return false;
	}
}
